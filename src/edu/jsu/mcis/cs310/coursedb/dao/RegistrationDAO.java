package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO {

    private final DAOFactory daoFactory;

    public RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Register a student for a course (prevents duplicates)
    public boolean create(int studentId, int termId, int crn) {

    String checkSql =
        "SELECT COUNT(*) FROM registration " +
        "WHERE studentid = ? AND termid = ? AND crn = ?";

    String insertSql =
        "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";

    try (Connection conn = daoFactory.getConnection();
         PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

        checkPs.setInt(1, studentId);
        checkPs.setInt(2, termId);
        checkPs.setInt(3, crn);

        try (ResultSet rs = checkPs.executeQuery()) {

            if (rs.next() && rs.getInt(1) > 0) {
                return false; // already registered
            }
        }

        // Only prepare insert AFTER confirming no duplicate
        try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

            insertPs.setInt(1, studentId);
            insertPs.setInt(2, termId);
            insertPs.setInt(3, crn);

            return insertPs.executeUpdate() == 1;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // Drop a single course registration
    public boolean delete(int studentId, int termId, int crn) {
        return executeDelete("DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?", studentId, termId, crn);
    }

    // Withdraw from all courses in a term
    public boolean delete(int studentId, int termId) {
        return executeDelete("DELETE FROM registration WHERE studentid = ? AND termid = ?", studentId, termId);
    }

    // Helper method to execute a DELETE statement
    private boolean executeDelete(String sql, int... params) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setInt(i + 1, params[i]);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // List all registrations for a student in a term
    public String list(int studentId, int termId) {
        String sql = "SELECT studentid, termid, crn FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, termId);

            try (ResultSet rs = ps.executeQuery()) {
                return DAOUtility.getResultSetAsJson(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
