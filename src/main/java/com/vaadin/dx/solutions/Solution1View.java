package com.vaadin.dx.solutions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import com.vaadin.flow.component.ai.provider.DatabaseProvider;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Solution 1: Implement a DatabaseProvider.
 */
@Route("solution1")
public class Solution1View extends VerticalLayout {

    public Solution1View(DataSource dataSource) {
        var db = new H2DatabaseProvider(dataSource);
        add(new com.vaadin.flow.component.html.Span(
                "Schema: " + db.getSchema()));
    }

    /**
     * DatabaseProvider backed by the H2 DataSource.
     */
    static class H2DatabaseProvider implements DatabaseProvider {

        private static final String SCHEMA = """
                Tables:
                - employees(id INT, name VARCHAR, department VARCHAR, salary DECIMAL, hire_date DATE)
                - sales(id INT, product VARCHAR, category VARCHAR, region VARCHAR, amount DECIMAL, quantity INT, sale_date DATE)
                - order_hdr(order_id INT, cust_name VARCHAR, order_dt DATE, status VARCHAR)
                - order_dtl(id INT, order_id INT, product VARCHAR, qty INT, unit_px DECIMAL)
                - temperatures(id INT, city VARCHAR, month VARCHAR, avg_temp DECIMAL, min_temp DECIMAL, max_temp DECIMAL)
                """;

        private final DataSource dataSource;

        H2DatabaseProvider(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public String getSchema() {
            return SCHEMA;
        }

        @Override
        public List<Map<String, Object>> executeQuery(String sql) {
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
            int cols = meta.getColumnCount();
            var result = new ArrayList<Map<String, Object>>();
            while (rs.next()) {
                var row = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                result.add(row);
            }
            return result;
        }
    }
}
