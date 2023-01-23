package fr.su.mentorataffectationportems.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * The interface for the translation function of the ResultSet to the objects (see AbstractRepository).
 */
public interface IPopulateParentFromRs<T, V> {
    T op(ResultSet rs, T ressource, T parent, V child) throws SQLException;
}
