# MINI PROJECT – SE

## Project Title
**Fast Food Billing System (FFBS)**

---

## Student Details
- **Student Name:** Krishna Rawat
- **Enrollment Number:** 00614803124
- **Course / Semester:** B.Tech (IT) / 5th Semester
- **Subject:** Software Engineering (CIC-357)
- **Faculty:** Nidhi Sengar
- **Institute:** Maharaja Agrasen Institute of Technology, Rohini, New Delhi

---

## Abstract 
Small fast food outlets still prepare bills by hand, which causes calculation, price and GST errors, long queues and no reliable sales record. This project applies software engineering practices to a Fast Food Billing System (FFBS), a point-of-sale application for order entry, automatic billing with GST and discount, multi-mode payment (cash, card, UPI) and sales reports. The system was developed through the SDLC: problem statement, IEEE-830 SRS, DFD, ER, UML diagrams, a Java implementation of the billing core, JUnit unit and integration testing, Function Point estimation and PERT/Gantt scheduling. All 15 test cases pass. Estimated size is 113.36 FP (11.34 person-months) and the critical path is 21 weeks.

---

## Problem Statement
Small and medium fast food outlets depend on manual or semi-manual billing. Bills are slow to prepare and prone to calculation, price and tax errors, and the owner has no accurate sales records. A simple, fast and reliable system is needed that lets a cashier enter an order, generates a correct itemized bill with GST, records the payment and stores every transaction so daily sales can be reviewed.

---

## Objectives
1. Reduce billing time and remove manual calculation errors (exact `BigDecimal` / `DECIMAL` arithmetic, GST and discount rules).
2. Support Cash, Card and UPI payments with role-based access (Admin / Cashier) and a reliable database of orders and sales.
3. Apply the full software engineering life cycle: requirements, design (DFD, ER, UML), testing, estimation and scheduling.

---

## Methodology / Approach
All diagrams are in [`docs/DIAGRAMS.md`](docs/DIAGRAMS.md) (Mermaid, rendered by GitHub), with the explanation for each.

| Phase | Work done | Where |
|---|---|---|
| Problem formulation | Problem statement, workflow flowchart, base schema (Exp 1) | `sql/01_schema_basic.sql` |
| Requirements | IEEE-830 SRS: 19 functional + 8 non-functional requirements (Exp 2) | `docs/SRS.md` |
| Function-oriented design | Context diagram, Level-1 DFD, structure chart (Exp 3) | `docs/DIAGRAMS.md` |
| Data design | ER diagram → 8 tables (Exp 4) | `sql/02_schema_er.sql` |
| OO design (UML) | Use case, class, object, state, activity, sequence, collaboration, component, deployment (Exp 5–10) | `docs/DIAGRAMS.md` |
| Testing | JUnit 5 unit + integration tests, stub payment gateway (Exp 11) | `src/test/java/ffbs` |
| Estimation | Function Point estimation (Exp 12) | `python/fp_estimate.py` |
| Scheduling | Timeline / Gantt / PERT, critical path (Exp 13) | `python/pert_schedule.py` |

**Workflow:** Login → select items & quantity → availability check → cart → subtotal – discount + GST → payment (Cash/Card/UPI, retry on failure) → save order → print receipt → daily sales report.

---

## Implementation & Output
**Key features**
- Role-based login (Admin / Cashier) and menu management
- Order entry with availability check; merge of repeated items
- Automatic bill: subtotal, discount (before tax), GST (rounded half-up to 2 decimals)
- Cash change calculation; card/UPI via payment gateway interface (stubbed in tests)
- Failed payment never saves a bill; every sale is stored; daily sales total
- Relational schema with primary key, foreign key and CHECK constraints

**Run it**
```bash
# Java 21 + Maven
mvn test                                   # runs TC01-TC15
mvn -q compile exec:java -Dexec.mainClass=ffbs.Main   # console demo bill

# Python
python python/fp_estimate.py
python python/pert_schedule.py

# MySQL / MariaDB
mysql -u root -p < sql/01_schema_basic.sql
```

**Sample output (demo bill):** 2 Veg Burger + 1 French Fries + 2 Cold Drink → Subtotal 355.00, GST (5%) 17.75, **Total 372.75**.

---

## Results
- **Achieved:** a working billing core (Java), database schema, complete SE documentation and models.
- **Testing:** 15/15 test cases designed from the SRS (TC01–TC09 unit, TC10–TC15 integration).
- **Estimation:** UFP = 104, VAF = 1.09, **FP = 113.36**, effort = **11.34 person-months** (at 10 FP/person-month).
- **Schedule:** critical path A→B→D→E→F→H→I→J = **21 weeks**; probability of finishing within 23 weeks ≈ **95 %**.

---

## Conclusion
- **Learning outcomes:** applied SDLC, SRS writing, structured and UML modelling, ER-to-table mapping, unit/integration testing with JUnit, FP estimation and PERT/CPM scheduling to one real-world case (CO1–CO4).
- **Summary:** FFBS shows that a small outlet's billing can be made fast, accurate and traceable using a systematic engineering process.

---

## Future Scope
- Web/mobile front end and real payment-gateway integration
- Inventory tracking, online ordering and delivery integration
- Kitchen display, customer loyalty/discounts and GST invoice export
- Analytics dashboards for best-selling items and peak hours

---

## Mapped SDG
- **SDG 9** – Industry, Innovation and Infrastructure (digital system for small businesses)
- **SDG 8** – Decent Work and Economic Growth (efficiency and accurate revenue for small outlets)
- **SDG 12** – Responsible Consumption and Production (digital records reduce paper bill books)

---

## GitHub Repository
- **Project Link:** https://github.com/KRISHNASTWT/fast-food-billing-system

---


## Repository Structure
```
fast-food-billing-system/
├── README.md
├── pom.xml
├── docs/        SRS.md, DIAGRAMS.md, EXPERIMENTS.md
├── sql/         01_schema_basic.sql, 02_schema_er.sql
├── python/      fp_estimate.py, pert_schedule.py
└── src/
    ├── main/java/ffbs/   MenuItem, Order, BillCalculator, OrderService, Bill, PaymentGateway, Main
    └── test/java/ffbs/   BillCalculatorTest, OrderTest, OrderServiceIntegrationTest
```
