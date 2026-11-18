# Software Requirements Specification – Fast Food Billing System (FFBS)
Version 1.0 · 14 November 2026 · Format: IEEE Std 830-1998

## 1. Introduction
- **Purpose:** specifies the functional and non-functional requirements of FFBS for developers, testers, the outlet owner (Admin) and evaluators.
- **Scope:** point-of-sale application for a single fast food outlet: order entry, bill calculation with GST and discount, payment recording, receipt generation and sales reporting. *Out of scope:* online ordering/delivery, raw-material inventory, staff payroll.
- **Definitions:** POS – Point of Sale; GST – Goods and Services Tax; UPI – Unified Payments Interface; SRS – Software Requirements Specification.

## 2. Overall Description
- **Perspective:** standalone system with a relational database; interfaces with a receipt printer and an external payment gateway.
- **Functions:** login/roles, menu management, order entry, automatic billing, multi-mode payment, receipt printing, order storage, sales reports.
- **User classes:** *Admin* (menu, prices, users, reports); *Cashier* (order entry, billing, payment, receipt only).
- **Environment:** PC/tablet (Windows/Linux), modern browser, MySQL/MariaDB, thermal printer.
- **Constraints:** GST shown on every bill; amounts in INR with 2 decimals.
- **Assumptions:** staff have basic computer skills; internet available for card/UPI; third-party gateway.

## 3. Specific Requirements
### 3.1 External interfaces
UI: touch-friendly menu buttons, cart panel, running total · Hardware: PC/tablet, thermal printer, optional cash drawer · Software: MySQL/MariaDB, browser · Communication: HTTPS to payment gateway.

### 3.2 Functional requirements
| ID | The system shall … | Priority |
|---|---|---|
| FR-01 | allow login with username/password and give access by role (Admin/Cashier) | High |
| FR-02 | show an error on wrong password and lock the account after 3 consecutive failures | Medium |
| FR-03 | allow Admin to add, edit, delete menu items (name, category, price) | High |
| FR-04 | allow Admin to mark items available/unavailable; unavailable items cannot be ordered | High |
| FR-05 | allow Cashier to add items to an order by selecting from the menu and entering quantity | High |
| FR-06 | allow Cashier to change quantity of, or remove, an item before payment | High |
| FR-07 | allow order type (Dine-in/Takeaway) selection | Medium |
| FR-08 | automatically calculate each line total and the order subtotal | High |
| FR-09 | calculate GST at the configured rate and apply an optional discount (percentage or flat) | High |
| FR-10 | display the final payable amount rounded to two decimals | High |
| FR-11 | accept payment by Cash, Card or UPI and record the mode | High |
| FR-12 | for cash, accept amount tendered and display change | Medium |
| FR-13 | on failed payment, allow retry or another mode without losing the order | High |
| FR-14 | generate a unique bill number and print an itemized receipt | High |
| FR-15 | store every completed order and its items in the database | High |
| FR-16 | allow an order to be cancelled before payment (not billed) | Medium |
| FR-17 | allow Admin to generate a daily sales report (orders, total sales, GST) | High |
| FR-18 | allow Admin to view item-wise and payment-mode-wise sales for a date range | Medium |
| FR-19 | allow Admin to create and deactivate Cashier accounts | Medium |

### 3.3 Non-functional requirements
| ID | Category | Requirement |
|---|---|---|
| NFR-01 | Performance | Bill for up to 20 line items within 2 seconds |
| NFR-02 | Usability | Cashier productive after at most 30 minutes of training |
| NFR-03 | Reliability | Order saved to DB before the receipt is printed |
| NFR-04 | Security | Hashed passwords; Cashiers cannot open reports or menu management |
| NFR-05 | Accuracy | Money in DECIMAL, exact to 2 decimals; PK/FK/CHECK constraints |
| NFR-06 | Maintainability | GST rate and discount rules changeable by Admin without code changes |
| NFR-07 | Portability | Runs on Windows and Linux via a modern browser |
| NFR-08 | Scalability | Handles at least 500 orders per day |
