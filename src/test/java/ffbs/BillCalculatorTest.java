package ffbs;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BillCalculatorTest {
    private final BillCalculator calc = new BillCalculator(new BigDecimal("0.05"));
    private static BigDecimal d(String s) { return new BigDecimal(s); }

    @Test @DisplayName("TC01 GST on 355.00 is 17.75")
    void gstOnSubtotal() { assertEquals(d("17.75"), calc.gst(d("355.00"), d("0.00"))); }

    @Test @DisplayName("TC02 GST is rounded half-up (33.30 -> 1.67)")
    void gstRounding() { assertEquals(d("1.67"), calc.gst(d("33.30"), d("0.00"))); }

    @Test @DisplayName("TC03 Discount is applied before GST")
    void discountBeforeGst() { assertEquals(d("315.00"), calc.total(d("355.00"), d("55.00"))); }

    @Test @DisplayName("TC04 Discount greater than subtotal is rejected")
    void invalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> calc.total(d("100.00"), d("150.00")));
    }

    @Test @DisplayName("TC05 Negative GST rate is rejected")
    void negativeRate() {
        assertThrows(IllegalArgumentException.class, () -> new BillCalculator(d("-0.05")));
    }
}
