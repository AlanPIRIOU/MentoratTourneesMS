package fr.su.mentorattourneesms.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * The interface for the translation function of the ResultSet to the objects (see AbstractRepository).
 */
public interface IPopulateFromRs<T> {
    T op(ResultSet rs, T base) throws SQLException;
}
