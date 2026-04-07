INSERT INTO employees VALUES (1, 'Alice Johnson', 'Engineering', 95000, DATE '2020-03-15');
INSERT INTO employees VALUES (2, 'Bob Smith', 'Marketing', 72000, DATE '2019-07-01');
INSERT INTO employees VALUES (3, 'Carol White', 'Engineering', 105000, DATE '2018-11-20');
INSERT INTO employees VALUES (4, 'David Brown', 'Sales', 68000, DATE '2021-01-10');
INSERT INTO employees VALUES (5, 'Eve Davis', 'Marketing', 78000, DATE '2022-05-25');
INSERT INTO employees VALUES (6, 'Frank Miller', 'Engineering', 112000, DATE '2017-09-03');
INSERT INTO employees VALUES (7, 'Grace Lee', 'Sales', 71000, DATE '2023-02-14');
INSERT INTO employees VALUES (8, 'Henry Wilson', 'HR', 65000, DATE '2020-08-30');

INSERT INTO sales VALUES (1, 'Laptop', 'Electronics', 'North', 1200, 5, DATE '2024-01-15');
INSERT INTO sales VALUES (2, 'Phone', 'Electronics', 'South', 800, 12, DATE '2024-01-20');
INSERT INTO sales VALUES (3, 'Desk', 'Furniture', 'North', 450, 3, DATE '2024-02-10');
INSERT INTO sales VALUES (4, 'Chair', 'Furniture', 'East', 250, 8, DATE '2024-02-15');
INSERT INTO sales VALUES (5, 'Monitor', 'Electronics', 'West', 600, 7, DATE '2024-03-01');
INSERT INTO sales VALUES (6, 'Keyboard', 'Electronics', 'South', 100, 25, DATE '2024-03-10');
INSERT INTO sales VALUES (7, 'Bookshelf', 'Furniture', 'North', 350, 2, DATE '2024-03-20');
INSERT INTO sales VALUES (8, 'Tablet', 'Electronics', 'East', 500, 10, DATE '2024-04-05');

INSERT INTO order_hdr VALUES (1001, 'Acme Corp', DATE '2024-03-15', 'Shipped');
INSERT INTO order_hdr VALUES (1002, 'Globex Inc', DATE '2024-03-16', 'Pending');
INSERT INTO order_hdr VALUES (1003, 'Acme Corp', DATE '2024-03-17', 'Delivered');

INSERT INTO order_dtl (order_id, product, qty, unit_px) VALUES (1001, 'Widget', 10, 12.50);
INSERT INTO order_dtl (order_id, product, qty, unit_px) VALUES (1001, 'Gadget', 5, 24.00);
INSERT INTO order_dtl (order_id, product, qty, unit_px) VALUES (1002, 'Spring', 100, 2.25);
INSERT INTO order_dtl (order_id, product, qty, unit_px) VALUES (1003, 'Widget', 20, 12.50);
INSERT INTO order_dtl (order_id, product, qty, unit_px) VALUES (1003, 'Spring', 50, 2.25);

INSERT INTO temperatures VALUES (1, 'New York', 'Jan', 0.5, -5.2, 6.1);
INSERT INTO temperatures VALUES (2, 'New York', 'Apr', 12.3, 6.1, 18.5);
INSERT INTO temperatures VALUES (3, 'New York', 'Jul', 25.1, 20.3, 30.8);
INSERT INTO temperatures VALUES (4, 'New York', 'Oct', 14.2, 8.5, 19.9);
INSERT INTO temperatures VALUES (5, 'London', 'Jan', 5.2, 1.3, 8.1);
INSERT INTO temperatures VALUES (6, 'London', 'Apr', 9.8, 5.6, 14.1);
INSERT INTO temperatures VALUES (7, 'London', 'Jul', 18.7, 14.2, 23.5);
INSERT INTO temperatures VALUES (8, 'London', 'Oct', 11.5, 7.3, 15.8);
INSERT INTO temperatures VALUES (9, 'Tokyo', 'Jan', 5.8, 1.2, 10.3);
INSERT INTO temperatures VALUES (10, 'Tokyo', 'Apr', 14.6, 9.8, 19.4);
INSERT INTO temperatures VALUES (11, 'Tokyo', 'Jul', 27.3, 23.1, 31.5);
INSERT INTO temperatures VALUES (12, 'Tokyo', 'Oct', 18.2, 13.7, 22.8);
