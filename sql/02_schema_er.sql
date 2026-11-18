-- Fast Food Billing System : 8 tables derived from the ER diagram (Experiment 4)
CREATE TABLE CATEGORY (
    category_id   INT PRIMARY KEY,
    category_name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE MENU_ITEM (
    item_code    VARCHAR(10) PRIMARY KEY,
    item_name    VARCHAR(50) NOT NULL,
    price        DECIMAL(8,2) NOT NULL CHECK (price > 0),
    availability CHAR(1) DEFAULT 'Y' CHECK (availability IN ('Y', 'N')),
    category_id  INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id)
);

CREATE TABLE CUSTOMER (
    customer_id INT PRIMARY KEY,
    cust_name   VARCHAR(50),
    phone       VARCHAR(15)
);

CREATE TABLE EMPLOYEE (
    emp_id   INT PRIMARY KEY,
    emp_name VARCHAR(50) NOT NULL,
    role     VARCHAR(10) CHECK (role IN ('Cashier', 'Manager'))
);

CREATE TABLE ORDERS (
    order_id       INT PRIMARY KEY,
    order_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status         VARCHAR(10) DEFAULT 'Placed' CHECK (status IN ('Placed', 'Billed', 'Paid')),
    customer_id    INT,                 -- NULL for a walk-in customer
    emp_id         INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
    FOREIGN KEY (emp_id)      REFERENCES EMPLOYEE(emp_id)
);

-- M:N relationship "contains" becomes a table of its own
CREATE TABLE ORDER_ITEM (
    order_id  INT,
    item_code VARCHAR(10),
    quantity  INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, item_code),
    FOREIGN KEY (order_id)  REFERENCES ORDERS(order_id),
    FOREIGN KEY (item_code) REFERENCES MENU_ITEM(item_code)
);

-- 1:1 relationships: foreign key + UNIQUE
CREATE TABLE BILL (
    bill_id   INT PRIMARY KEY,
    order_id  INT NOT NULL UNIQUE,
    bill_date DATE,
    subtotal  DECIMAL(10,2) NOT NULL,
    discount  DECIMAL(10,2) DEFAULT 0,
    gst       DECIMAL(10,2) NOT NULL,
    total     DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES ORDERS(order_id)
);

CREATE TABLE PAYMENT (
    payment_id INT PRIMARY KEY,
    bill_id    INT NOT NULL UNIQUE,
    mode       VARCHAR(5) CHECK (mode IN ('CASH', 'CARD', 'UPI')),
    amount     DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES BILL(bill_id)
);

-- Sample data and a join query
INSERT INTO CATEGORY VALUES (1, 'Burgers'), (2, 'Sides'), (3, 'Beverages');
INSERT INTO MENU_ITEM VALUES
    ('B01', 'Veg Burger',   89.00, 'Y', 1),
    ('F01', 'French Fries', 79.00, 'Y', 2),
    ('D01', 'Cold Drink',   49.00, 'Y', 3);
INSERT INTO EMPLOYEE VALUES (1, 'Cashier One', 'Cashier');
INSERT INTO ORDERS (order_id, emp_id) VALUES (1, 1);
INSERT INTO ORDER_ITEM VALUES (1, 'B01', 2), (1, 'F01', 1), (1, 'D01', 2);

SELECT m.item_name, c.category_name, oi.quantity, m.price * oi.quantity AS line_total
FROM ORDER_ITEM oi
JOIN MENU_ITEM m ON m.item_code = oi.item_code
JOIN CATEGORY  c ON c.category_id = m.category_id
WHERE oi.order_id = 1;
