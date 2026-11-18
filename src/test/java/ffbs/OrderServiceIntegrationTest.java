package ffbs;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Integration test: Order + BillCalculator + OrderService + PaymentGateway (stub)
class OrderServiceIntegrationTest {
    private boolean gatewayApproves;
    private OrderService service;
    private final MenuItem burger = new MenuItem("B01", "Veg Burger", new BigDecimal("89.00"), true);
    private final MenuItem fries = new MenuItem("F01", "French Fries", new BigDecimal("79.00"), true);
    private final MenuItem drink = new MenuItem("D01", "Cold Drink", new BigDecimal("49.00"), true);

    private static BigDecimal d(String s) { return new BigDecimal(s); }

    @BeforeEach
    void setUp() {
        gatewayApproves = true;
        service = new OrderService(new BillCalculator(d("0.05")), (mode, amount) -> gatewayApproves);
    }

    private Order sampleOrder() {                          // subtotal 355.00
        Order o = new Order();
        o.addItem(burger, 2); o.addItem(fries, 1); o.addItem(drink, 2);
        return o;
    }

    @Test @DisplayName("TC10 Cash payment: total 372.75, change 27.25, bill saved")
    void cashPayment() {
        Bill b = service.checkout(sampleOrder(), d("0.00"), "CASH", d("400.00"));
        assertEquals(d("372.75"), b.total());
        assertEquals(d("27.25"), b.change());
        assertEquals(1, service.billCount());
    }

    @Test @DisplayName("TC11 UPI payment approved by gateway")
    void upiPayment() {
        Bill b = service.checkout(sampleOrder(), d("0.00"), "UPI", null);
        assertEquals(d("372.75"), b.total());
        assertEquals(1, service.billCount());
    }

    @Test @DisplayName("TC12 Card declined: no bill is saved")
    void cardDeclined() {
        gatewayApproves = false;
        assertThrows(IllegalStateException.class,
                () -> service.checkout(sampleOrder(), d("0.00"), "CARD", null));
        assertEquals(0, service.billCount());
    }

    @Test @DisplayName("TC13 Insufficient cash: no bill is saved")
    void insufficientCash() {
        assertThrows(IllegalArgumentException.class,
                () -> service.checkout(sampleOrder(), d("0.00"), "CASH", d("300.00")));
        assertEquals(0, service.billCount());
    }

    @Test @DisplayName("TC14 Empty order cannot be checked out")
    void emptyOrder() {
        assertThrows(IllegalStateException.class,
                () -> service.checkout(new Order(), d("0.00"), "CASH", d("100.00")));
    }

    @Test @DisplayName("TC15 Two bills: numbered 1, 2 and daily sales = 466.20")
    void dailySales() {
        service.checkout(sampleOrder(), d("0.00"), "UPI", null);
        Order second = new Order();
        second.addItem(burger, 1);
        assertEquals(2, service.checkout(second, d("0.00"), "UPI", null).billNo());
        assertEquals(d("466.20"), service.dailySales());
    }
}
