-- Fast Food Billing System - Initial Database Schema (Experiment 1)
CREATE DATABASE IF NOT EXISTS ffbs_db;
USE ffbs_db;

CREATE TABLE Menu_Items (
    item_id      VARCHAR(10) PRIMARY KEY,
    item_name    VARCHAR(80) NOT NULL,
    category     VARCHAR(30) NOT NULL,
    unit_price   DECIMAL(8, 2) NOT NULL CHECK (unit_price > 0),
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Orders (
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    order_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_type   ENUM('DINE_IN', 'TAKEAWAY') NOT NULL,
    payment_mode ENUM('CASH', 'CARD', 'UPI') NOT NULL,
    subtotal     DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (subtotal >= 0),
    gst_amount   DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0
);

CREATE TABLE Order_Items (
    order_id   INT NOT NULL,
    item_id    VARCHAR(10) NOT NULL,
    quantity   INT NOT NULL CHECK (quantity > 0),
    line_total DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (item_id)  REFERENCES Menu_Items(item_id)
);

-- Sample data: one dine-in order paid by UPI
INSERT INTO Menu_Items VALUES
    ('B01', 'Veg Burger',   'Burgers',   89.00, TRUE),
    ('F01', 'French Fries', 'Sides',     79.00, TRUE),
    ('D01', 'Cold Drink',   'Beverages', 49.00, TRUE);

INSERT INTO Orders (order_type, payment_mode) VALUES ('DINE_IN', 'UPI');

INSERT INTO Order_Items VALUES
    (1, 'B01', 2, 2 * 89.00),
    (1, 'F01', 1, 1 * 79.00),
    (1, 'D01', 2, 2 * 49.00);

-- Bill totals (GST taken as 5% for illustration)
UPDATE Orders
SET subtotal = (SELECT SUM(line_total) FROM Order_Items WHERE order_id = 1)
WHERE order_id = 1;

UPDATE Orders
SET gst_amount   = ROUND(subtotal * 0.05, 2),
    total_amount = ROUND(subtotal * 1.05, 2)
WHERE order_id = 1;

-- Generate the bill
SELECT m.item_name AS Item, oi.quantity AS Qty, m.unit_price AS Rate, oi.line_total AS Amount
FROM Order_Items oi JOIN Menu_Items m ON m.item_id = oi.item_id
WHERE oi.order_id = 1
ORDER BY m.item_id;

SELECT order_id, order_type, payment_mode, subtotal, gst_amount, total_amount
FROM Orders WHERE order_id = 1;

-- Expected: subtotal 355.00, GST 17.75, total 372.75
-- Validation test (must FAIL because of CHECK quantity > 0):
-- INSERT INTO Order_Items VALUES (1, 'D01', -3, -147.00);
