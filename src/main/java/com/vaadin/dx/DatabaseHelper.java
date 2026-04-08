package com.vaadin.dx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Utility for executing SQL queries against the H2 database.
 *
 * <pre>
 * var results = DatabaseHelper.query(dataSource, "SELECT * FROM employees");
 * </pre>
 */
public class DatabaseHelper {

    private DatabaseHelper() {
    }

    /**
     * Executes a SQL query and returns the results as a list of maps,
     * where each map represents a row with column names as keys.
     *
     * @param dataSource
     *            the data source to query
     * @param sql
     *            the SQL query to execute
     * @return a list of rows, each row as a column-name-to-value map
     */
    public static List<Map<String, Object>> query(DataSource dataSource,
            String sql) {
        try (var conn = dataSource.getConnection();
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery(sql)) {
            return resultSetToList(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + sql, e);
        }
    }

    private static List<Map<String, Object>> resultSetToList(
            ResultSet rs) throws SQLException {
        var meta = rs.getMetaData();
        var cols = meta.getColumnCount();
        var result = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            var row = new LinkedHashMap<String, Object>();
            for (var i = 1; i <= cols; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            result.add(row);
        }
        return result;
    }
}
