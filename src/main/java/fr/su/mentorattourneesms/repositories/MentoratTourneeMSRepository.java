package fr.su.mentorattourneesms.repositories;

import fr.su.back.common.exception.BusinessException;
import fr.su.mentorattourneesms.model.Tournee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class MentoratTourneeMSRepository extends AbstractRepository {

    @PersistenceContext
    EntityManager entityManager;

    Logger logger = LogManager.getLogger(MentoratTourneeMSRepository.class);

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<Tournee> findTournees() throws BusinessException {
        return this.getMany("EE0002SQ09", List.of("0"), (ResultSet rs, Tournee base) -> {

            Translater.Populate(base, rs);

            return base;
        }, Tournee::new);
    }

}
