# Diagrams with explanation (Mermaid – rendered automatically on GitHub)

## 1. System flowchart (Exp 1)
```mermaid
flowchart TD
  S([Start]) --> L[Cashier logs in] --> O[Select items and quantity]
  O --> A{Item available?}
  A -- No --> M[Show out-of-stock] --> O
  A -- Yes --> C[Add to cart, update total]
  C --> N{More items?}
  N -- Yes --> O
  N -- No --> B[Subtotal - discount + GST]
  B --> P[Take payment: Cash / Card / UPI]
  P --> OK{Payment OK?}
  OK -- No --> P
  OK -- Yes --> R[Save order, print receipt, update sales report] --> E([End])
```
**Explanation:** shows the six stages of billing; the availability check and payment retry are the two decision loops.

## 2. Use case diagram (Exp 2 / 5)
```mermaid
flowchart LR
  Cashier((Cashier)) --- U1[Login] & U2[Place / modify order] & U3[Generate bill] & U4[Take payment] & U5[Print receipt]
  Admin((Admin)) --- U1 & V1[Manage menu and prices] & V2[Manage users] & V3[View sales reports]
  U4 --- PG((Payment Gateway))
```
**Explanation:** Cashier handles the sale; Admin manages data and reports; the external Payment Gateway processes card/UPI.

## 3. Context diagram – DFD Level 0 (Exp 3)
```mermaid
flowchart LR
  C[Cashier] -- order, payment details --> FFBS((Fast Food Billing System))
  FFBS -- bill, receipt --> C
  A[Admin] -- menu, price, user updates --> FFBS
  FFBS -- sales reports --> A
  FFBS -- payment request --> PG[Payment Gateway]
  PG -- success / failure --> FFBS
```
**Explanation:** one process with three external entities; no internal detail.

## 4. DFD Level 1 (Exp 3)
```mermaid
flowchart LR
  C[Cashier] --> P1((1 Authenticate)) --> D1[(Users)]
  C --> P2((2 Take order)) --> D2[(Menu_Items)]
  P2 --> P3((3 Calculate bill)) --> P4((4 Process payment))
  P4 <--> PG[Payment Gateway]
  P4 --> P5((5 Store order and receipt)) --> D3[(Orders)]
  D3 --> P6((6 Generate reports)) --> A[Admin]
```
**Explanation:** breaks the system into six processes and three data stores.

## 5. ER diagram (Exp 4) – 7 entities, 8 tables
```mermaid
erDiagram
  CATEGORY ||--o{ MENU_ITEM : has
  CUSTOMER |o--o{ ORDERS : places
  EMPLOYEE ||--o{ ORDERS : takes
  ORDERS ||--|{ ORDER_ITEM : contains
  MENU_ITEM ||--o{ ORDER_ITEM : "appears in"
  ORDERS ||--|| BILL : generates
  BILL ||--|| PAYMENT : "paid by"
  MENU_ITEM { string item_code PK
    string item_name
    decimal price
    char availability }
  ORDERS { int order_id PK
    timestamp order_datetime
    string status }
  ORDER_ITEM { int order_id PK
    string item_code PK
    int quantity }
  BILL { int bill_id PK
    decimal subtotal
    decimal gst
    decimal total }
```
**Explanation:** M:N relation *contains* becomes the ORDER_ITEM table; 1:1 relations use FK + UNIQUE. SQL: `sql/02_schema_er.sql`.

## 6. Class diagram (Exp 6)
```mermaid
classDiagram
  class MenuItem { +String id +String name +BigDecimal price +boolean available }
  class Order { +addItem(item, qty) +quantityOf(id) +subtotal() +isEmpty() }
  class BillCalculator { -BigDecimal gstRate +gst(subtotal, discount) +total(subtotal, discount) }
  class OrderService { +checkout(order, discount, mode, tendered) Bill +billCount() +dailySales() }
  class PaymentGateway { <<interface>> +charge(mode, amount) boolean }
  class Bill { +billNo +subtotal +gst +total +mode +change }
  Order o-- MenuItem
  OrderService --> BillCalculator
  OrderService --> PaymentGateway
  OrderService --> Bill
```

## 7. State-chart of an order (Exp 7)
```mermaid
stateDiagram-v2
  [*] --> Placed
  Placed --> Cancelled : cancel
  Placed --> Billed : generate bill
  Billed --> Paid : payment success
  Billed --> Billed : payment failed / retry
  Paid --> [*]
  Cancelled --> [*]
```

## 8. Sequence diagram – checkout (Exp 8)
```mermaid
sequenceDiagram
  actor Cashier
  Cashier->>OrderService: checkout(order, discount, mode, tendered)
  OrderService->>Order: subtotal()
  OrderService->>BillCalculator: gst(), total()
  alt CASH
    OrderService->>OrderService: check tendered, compute change
  else CARD / UPI
    OrderService->>PaymentGateway: charge(mode, total)
    PaymentGateway-->>OrderService: approved / declined
  end
  OrderService-->>Cashier: Bill (saved) or error
```

## 9. Component diagram (Exp 9)
```mermaid
flowchart LR
  UI[Web UI] --> OS[Order Service] --> BC[Bill Calculator]
  OS --> PG[Payment Gateway Adapter] --> EXT((External Gateway))
  OS --> DB[(MySQL / MariaDB)]
  UI --> RP[Report Module] --> DB
```

## 10. Deployment diagram (Exp 10)
```mermaid
flowchart LR
  subgraph Counter PC / Tablet
    B[Browser]
    PR[Thermal Printer]
  end
  subgraph Server
    APP[FFBS Application]
    DB[(MySQL / MariaDB)]
  end
  B -- HTTP --> APP --> DB
  APP -- HTTPS --> GW((Payment Gateway))
  B --- PR
```

## 11. Gantt / PERT (Exp 13)
Critical path **A → B → D → E → F → H → I → J = 21 weeks** (see `python/pert_schedule.py`).
```mermaid
gantt
  dateFormat X
  axisFormat %s
  section Weeks
  A Requirements & SRS :0, 2
  B Design (UML, DB) :2, 5
  C Database creation :5, 7
  D Menu & order module :5, 9
  G User interface :5, 9
  E Billing & tax module :9, 12
  F Payment & receipt :12, 15
  H Integration :15, 17
  I Testing :17, 20
  J Deployment & training :20, 21
```
