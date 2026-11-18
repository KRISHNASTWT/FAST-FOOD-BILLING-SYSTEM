package ffbs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final BillCalculator calc;
    private final PaymentGateway gateway;
    private final List<Bill> savedBills = new ArrayList<>();

    public OrderService(BillCalculator calc, PaymentGateway gateway) {
        this.calc = calc;
        this.gateway = gateway;
    }

    public Bill checkout(Order order, BigDecimal discount, String mode, BigDecimal tendered) {
        if (order.isEmpty()) throw new IllegalStateException("Order is empty");
        BigDecimal subtotal = order.subtotal();
        BigDecimal gst = calc.gst(subtotal, discount);
        BigDecimal total = calc.total(subtotal, discount);
        BigDecimal change = new BigDecimal("0.00");
        if (mode.equals("CASH")) {
            if (tendered.compareTo(total) < 0)
                throw new IllegalArgumentException("Insufficient cash");
            change = tendered.subtract(total);
        } else if (!gateway.charge(mode, total)) {
            throw new IllegalStateException("Payment failed");
        }
        Bill bill = new Bill(savedBills.size() + 1, subtotal, gst, total, mode, change);
        savedBills.add(bill);
        return bill;
    }

    public int billCount() { return savedBills.size(); }

    public BigDecimal dailySales() {
        return savedBills.stream().map(Bill::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
