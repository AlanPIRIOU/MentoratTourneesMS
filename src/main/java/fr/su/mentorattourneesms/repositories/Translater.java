package fr.su.mentorattourneesms.repositories;

import fr.su.mentorattourneesms.model.Tournee;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Translater {

    private Translater() {
    }

    public static void Populate(Tournee base, ResultSet rs) throws SQLException {

        base.setCode(rs.getString(Tournee.COL_CODE));
        base.setLibelle(rs.getString(Tournee.COL_LIBELLE));

    }
}
