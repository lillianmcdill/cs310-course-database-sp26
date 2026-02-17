package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsoner;

public class DAOUtility {

    // Term ID for Spring 2026
    public static final int TERMID_SP26 = 202601;

    // Convert a ResultSet to a JSON array string
    public static String getResultSetAsJson(ResultSet rs) {
        JsonArray jsonArray = new JsonArray();

        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new java.util.LinkedHashMap<>();
                for (int i = 1; i <= columns; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                jsonArray.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Jsoner.serialize(jsonArray);
    }
}
