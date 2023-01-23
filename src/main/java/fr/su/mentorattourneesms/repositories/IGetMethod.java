package fr.su.mentorattourneesms.repositories;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

/*
 * The interface for get calls in the AbstractRepository (getOne, getMany...)
 */
public interface IGetMethod<T> {
    T op(CachedRowSet crs) throws SQLException;
}
