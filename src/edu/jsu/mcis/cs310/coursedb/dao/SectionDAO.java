package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionDAO {
    
    private static final String QUERY_FIND = 
        "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termId, String subjectId, String num) {
        String result = "[]";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_FIND)) {

            if (conn.isValid(0)) {
                ps.setInt(1, termId);
                ps.setString(2, subjectId);
                ps.setString(3, num);

                try (ResultSet rs = ps.executeQuery()) {
                    result = DAOUtility.getResultSetAsJson(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}