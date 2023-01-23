package fr.su.mentorattourneesms.repositories;

import fr.su.back.common.exception.BusinessException;
import fr.su.back.common.exception.TechnicalException;
import fr.su.back.common.objects.error.Error;
import fr.su.back.jpa.db2.DB2400StoredProcedureManager;
import fr.su.back.jpa.db2.HTTP_CODE;
import fr.su.back.jpa.db2.utils.ProcStocResult;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractRepository {

    protected abstract Logger getLogger();

    protected abstract EntityManager getEntityManager();

    /*
     * Get the first returned value from the called procedure.
     *
     * @param procedure     the name of the AS400 procedure.
     * @param parameters    the parameters to be passed to the procedure.
     * @param populate      the translation function of the ResultSet to the object (see IPopulateFromRs).
     * @param factory       the factory function fot the object default value.
     *
     * @return              the first value from the procedure.
     */
    protected <T> T getOne(String procedure, List<String> parameters, IPopulateFromRs<T> populate, Supplier<T> factory) throws BusinessException {

        // To avoid Sonar error, we have to close the crs in the same method,
        // so we have to pass the data extraction as a lambda function
        return this.callProcedure(procedure, parameters, (CachedRowSet ccrs) -> {
            T retour = factory.get();

            if (ccrs.next()) {
                retour = populate.op(ccrs, factory.get());
            }

            return retour;
        });
    }

    /*
     * Get all the returned values from the called procedure.
     *
     * @param procedure     the name of the AS400 procedure.
     * @param parameters    the parameters to be passed to the procedure.
     * @param populate      the translation function of the ResultSet to the objects (see IPopulateFromRs).
     * @param factory       the factory function fot the objects default value.
     *
     * @return              all the values from the procedure.
     */
    protected <T> List<T> getMany(String procedure, List<String> parameters, IPopulateFromRs<T> populate, Supplier<T> factory) throws BusinessException {

        // To avoid Sonar error, we have to close the crs in the same method,
        // so we have to pass the data extraction as a lambda function
        return this.callProcedure(procedure, parameters, (CachedRowSet ccrs) -> {
            List<T> retour = new ArrayList<>();

            while (ccrs.next()) {
                T populated = populate.op(ccrs, factory.get());

                if (populated != null) {
                    retour.add(populated);
                }
            }

            return retour;
        });
    }

    /*
     * Get the first returned value from the called procedure.
     *
     * @param procedure         the name of the AS400 procedure.
     * @param parameters        the parameters to be passed to the procedure.
     * @param parametersInOut   the parameters InOut to be passed to the procedure.
     * @param populate          the translation function of the ResultSet to the object (see IPopulateFromRs).
     * @param factory           the factory function fot the object default value.
     *
     * @return                  the first value from the procedure.
     */
    protected <T> T getOneInOut(String procedure, List<String> parameters, List<Triple<String, Integer, String>> parametersInOut, IPopulateFromRsInOut<T> populate, Supplier<T> factory) throws BusinessException {

        // To avoid Sonar error, we have to close the crs in the same method,
        // so we have to pass the data extraction as a lambda function
        return this.callProcedureInOut(procedure, parameters, parametersInOut, (CachedRowSet ccrs, Map<String, Object> objetsInOut, Map<String, Object> objetsOut) -> {
            T retour = factory.get();

            if (ccrs.next()) {
                retour = populate.op(ccrs, objetsInOut, objetsOut, factory.get());
            }

            return retour;
        });
    }

    /*
     * Get all the returned values from the called procedure.
     *
     * @param procedure         the name of the AS400 procedure.
     * @param parameters        the parameters to be passed to the procedure.
     * @param parametersInOut   the parameters InOut to be passed to the procedure.
     * @param populate          the translation function of the ResultSet to the objects (see IPopulateFromRs).
     * @param factory           the factory function fot the objects default value.
     *
     * @return                  all the values from the procedure.
     */
    protected <T> List<T> getManyInOut(String procedure, List<String> parameters, List<Triple<String, Integer, String>> parametersInOut, IPopulateFromRsInOut<T> populate, Supplier<T> factory) throws BusinessException {

        // To avoid Sonar error, we have to close the crs in the same method,
        // so we have to pass the data extraction as a lambda function
        return this.callProcedureInOut(procedure, parameters, parametersInOut, (CachedRowSet ccrs, Map<String, Object> objetsInOut, Map<String, Object> objetsOut) -> {
            List<T> retour = new ArrayList<>();

            while (ccrs.next()) {
                T populated = populate.op(ccrs, objetsInOut, objetsOut, factory.get());

                if (populated != null) {
                    retour.add(populated);
                }
            }

            return retour;
        });
    }

    /*
     * Call the procedure with parameters and return a CachedRowSet.
     *
     * @param procedure     the name of the AS400 procedure.
     * @param parameters    the parameters to be passed to the procedure.
     *
     * @return              the response of the procedure as a CachedRowSet.
     */
    protected <T> T callProcedure(String procedure, List<String> parameters, IGetMethod<T> method) throws BusinessException {
        ResultSet rs = null;

        try (DB2400StoredProcedureManager manager = new DB2400StoredProcedureManager(this.getEntityManager()); CachedRowSet crs = RowSetProvider.newFactory()
                .createCachedRowSet()) {

            manager.prepareStoredProcedure(procedure).withoutToken().withoutTraceId();

            this.getLogger().info("Appel [proc: " + procedure + ", params: " + parameters + "]");

            for (String parameter : parameters) {
                manager.withParameter(parameter);
            }

            rs = manager.callStoredProcedure();
            crs.populate(rs);

            T object = method.op(crs);

            this.getLogger().debug("Retour [proc: " + procedure + ", objet: " + object.toString() + "]");

            return object;

        } catch (SQLException e) {
            this.throwTechnicalException(e);
        } finally {
            this.closeRowSet(rs);
        }

        return null;
    }

    /*
     * Call the procedure with InOut parameters and return a CachedRowSet.
     *
     * @param procedure     the name of the AS400 procedure.
     * @param parameters    the parameters to be passed to the procedure.
     *
     * @return              the response of the procedure as a CachedRowSet.
     */
    protected <T> T callProcedureInOut(String procedure, List<String> parameters, List<Triple<String, Integer, String>> parametersInOut, IGetMethodInOut<T> method) throws BusinessException {
        ResultSet rs = null;

        try (DB2400StoredProcedureManager manager = new DB2400StoredProcedureManager(this.getEntityManager()); CachedRowSet crs = RowSetProvider.newFactory()
                .createCachedRowSet()) {

            manager.prepareStoredProcedure(procedure).withoutToken().withoutTraceId();

            this.getLogger().info("Appel [proc: " + procedure + ", params: " + parameters + ", inOut: " + parametersInOut + "]");

            for (String parameter : parameters) {
                manager.withParameter(parameter);
            }

            for (Triple<String, Integer, String> parameterInOut : parametersInOut) {
                manager.withParameterInOut(parameterInOut.getLeft(), parameterInOut.getMiddle(), parameterInOut.getRight());
            }

            ProcStocResult res = manager.callStoredProcedureAndReturnInOutParameters();
            rs = res.getResultSet();
            crs.populate(rs);

            T object = method.op(crs, res.getObjetsInOut(), res.getObjetsOut());

            this.getLogger().debug("Retour [proc: " + procedure + ", objet: " + object.toString() + "]");

            return object;

        } catch (SQLException e) {
            this.throwTechnicalException(e);
        } finally {
            this.closeRowSet(rs);
        }

        return null;
    }

    /*
     * Permit to aggregate multiple objects by difference and fill their sub-class accordingly.
     *
     * @param rs            the name result set from the db.
     * @param parent        the parent object to fill.
     * @param references    the precedents parent object to check for diff.
     * @param factory       the factory function fot the objects default value.
     *
     * @return              all the values from the procedure.
     */
    protected <T, V> T aggregateDeep(ResultSet rs, T parent, HashMap<Integer, T> references, IPopulateParentFromRs<T, V> populate, Supplier<V> factoryV) {
        try {

            int hash = this.limitedDepthHash(parent); // hash des champs constants de l'objet

            boolean referenced = references.containsKey(hash);

            if (!referenced) {
                populate.op(rs, parent, parent, factoryV.get());        // L'objet est nouveau, On initialise la liste de sous objets
                references.put(hash, parent);                           // avec une première occurence, on le renseigne dans les références
                return parent;                                          // et on le retourne pour l'ajouter à la liste.
            } else {
                T reference = references.get(hash);                     // L'objet est similaire à un précédent, on met seulement a jour la liste de sous objets
                populate.op(rs, reference, parent, factoryV.get());     // dans l'objet "reference" en se basant sur les données de l'objet "parent"
            }

        } catch (Exception exception) {
            this.throwTechnicalException(exception);
        }

        return null; // On n'ajoute pas d'objet à la liste.
    }

    /*
     * Hash an object based on only it's own fields
     * and not on it's childs objects (in lists).
     *
     * @param object        the object to hash
     *
     * @return              the produced hash
     */
    protected <T> int limitedDepthHash(T object) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<?> classe = object.getClass();
        List<String> valeurs = new ArrayList<>();

        for (Field field : classe.getDeclaredFields()) {

            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (!isStatic) {

                Method method = classe.getMethod("get" + StringUtils.capitalize(field.getName()));

                Object value = method.invoke(object);

                boolean isList = value instanceof List;
                if (!isList) {
                    valeurs.add(value.toString());
                }
            }

        }

        return Objects.hashCode(valeurs);
    }

    protected void closeRowSet(ResultSet rs) throws BusinessException {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception exception) {
            this.throwTechnicalException(exception);
        }
    }

    protected void throwTechnicalException(Exception e) throws TechnicalException {
        Error error = new Error();
        error.setCode(HTTP_CODE.HTTP_ERREUR_SERVEUR.getCode());
        error.setLabel(e.getMessage());
        throw new TechnicalException(error);
    }
}
