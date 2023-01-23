package fr.su.mentorattourneesms.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/*
 * The interface for the translation function of the ResultSet to the objects (see AbstractRepository).
 */
public interface IPopulateFromRsInOut<T> {
    T op(ResultSet rs, Map<String, Object> objetsInOut, Map<String, Object> objetsOut, T base) throws SQLException;
}
