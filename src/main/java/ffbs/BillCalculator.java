package ffbs;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BillCalculator {
    private final BigDecimal gstRate;

    public BillCalculator(BigDecimal gstRate) {
        if (gstRate.signum() < 0)
            throw new IllegalArgumentException("GST rate cannot be negative");
        this.gstRate = gstRate;
    }

    public BigDecimal gst(BigDecimal subtotal, BigDecimal discount) {
        return taxable(subtotal, discount).multiply(gstRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total(BigDecimal subtotal, BigDecimal discount) {
        return taxable(subtotal, discount).add(gst(subtotal, discount));
    }

    private BigDecimal taxable(BigDecimal subtotal, BigDecimal discount) {
        if (discount.signum() < 0 || discount.compareTo(subtotal) > 0)
            throw new IllegalArgumentException("Invalid discount");
        return subtotal.subtract(discount);
    }
}
