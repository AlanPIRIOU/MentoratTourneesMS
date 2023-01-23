package fr.su.mentorataffectationportems.repositories;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.Map;

/*
 * The interface for get calls in the AbstractRepository (getOne, getMany...)
 */
public interface IGetMethodInOut<T> {
    T op(CachedRowSet crs, Map<String, Object> objetsInOut, Map<String, Object> objetsOut) throws SQLException;
}
