package ffbs;

import java.math.BigDecimal;

/** Small console demo: bills the sample order from the lab file (2 burgers, 1 fries, 2 drinks, UPI). */
public class Main {
    public static void main(String[] args) {
        MenuItem burger = new MenuItem("B01", "Veg Burger", new BigDecimal("89.00"), true);
        MenuItem fries  = new MenuItem("F01", "French Fries", new BigDecimal("79.00"), true);
        MenuItem drink  = new MenuItem("D01", "Cold Drink", new BigDecimal("49.00"), true);

        Order order = new Order();
        order.addItem(burger, 2);
        order.addItem(fries, 1);
        order.addItem(drink, 2);

        OrderService service = new OrderService(
                new BillCalculator(new BigDecimal("0.05")),
                (mode, amount) -> true);            // demo gateway: always approves
        Bill b = service.checkout(order, new BigDecimal("0.00"), "UPI", null);

        System.out.println("===== FAST FOOD BILLING SYSTEM =====");
        System.out.println("Bill No  : " + b.billNo());
        System.out.println("Subtotal : " + b.subtotal());
        System.out.println("GST (5%) : " + b.gst());
        System.out.println("Total    : " + b.total());
        System.out.println("Paid by  : " + b.mode());
    }
}
