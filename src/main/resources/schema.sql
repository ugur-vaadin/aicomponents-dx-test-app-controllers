CREATE TABLE IF NOT EXISTS employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE
);

CREATE TABLE IF NOT EXISTS sales (
    id INT PRIMARY KEY,
    product VARCHAR(100),
    category VARCHAR(50),
    region VARCHAR(50),
    amount DECIMAL(10,2),
    quantity INT,
    sale_date DATE
);

CREATE TABLE IF NOT EXISTS order_hdr (
    order_id INT PRIMARY KEY,
    cust_name VARCHAR(100),
    order_dt DATE,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS order_dtl (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product VARCHAR(100),
    qty INT,
    unit_px DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS temperatures (
    id INT PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(50),
    month VARCHAR(10),
    avg_temp DECIMAL(5,2),
    min_temp DECIMAL(5,2),
    max_temp DECIMAL(5,2)
);
