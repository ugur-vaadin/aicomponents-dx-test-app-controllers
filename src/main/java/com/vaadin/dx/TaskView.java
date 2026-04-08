package com.vaadin.dx;

import javax.sql.DataSource;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * DX Test — implement each task here.
 * <p>
 * The application has an H2 in-memory database with these tables:
 * - employees(id, name, department, salary, hire_date)
 * - sales(id, product, category, region, amount, quantity, sale_date)
 * - order_hdr(order_id, cust_name, order_dt, status)
 * - order_dtl(id, order_id, product, qty, unit_px)
 * - temperatures(id, city, month, avg_temp, min_temp, max_temp)
 * <p>
 * You can inject the DataSource to access the database.
 * <p>
 * For running SQL queries, use the {@link DatabaseHelper} helper:
 * - {@code DatabaseHelper.query(dataSource, "SELECT * FROM employees")} returns List&lt;Map&lt;String, Object&gt;&gt;
 * <p>
 * For state persistence, use the {@link StateStorage} helper:
 * - {@code StateStorage.persist("myKey", data)} to save
 * - {@code StateStorage.retrieve("myKey")} to load
 * <p>
 * For view utilities, use the {@link ViewHelper} helper:
 * - {@code ViewHelper.getStringProperty(json, "propertyName")} to extract a value from JSON
 * - {@code ViewHelper.setBackgroundColor(component, "blue")} to set a CSS background color
 */
@Route("")
public class TaskView extends VerticalLayout {

    public TaskView(DataSource dataSource) {
        // Start implementing here
    }
}
