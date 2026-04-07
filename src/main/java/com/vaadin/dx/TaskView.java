package com.vaadin.dx;

import javax.sql.DataSource;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * DX Test — implement each task here.
 *
 * The application has an H2 in-memory database with these tables:
 * - employees(id, name, department, salary, hire_date)
 * - sales(id, product, category, region, amount, quantity, sale_date)
 * - order_hdr(order_id, cust_name, order_dt, status)
 * - order_dtl(id, order_id, product, qty, unit_px)
 * - temperatures(id, city, month, avg_temp, min_temp, max_temp)
 *
 * You can inject the DataSource to query the database.
 *
 * For state persistence, use the {@link StateStorage} helper:
 * - {@code StateStorage.persist("myKey", data)} to save
 * - {@code StateStorage.retrieve("myKey")} to load
 */
@Route("")
public class TaskView extends VerticalLayout {

    public TaskView(DataSource dataSource) {
        // Start implementing here
    }
}
