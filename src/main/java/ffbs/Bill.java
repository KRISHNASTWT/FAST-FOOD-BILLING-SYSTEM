package ffbs;

import java.math.BigDecimal;

public record Bill(int billNo, BigDecimal subtotal, BigDecimal gst, BigDecimal total,
                   String mode, BigDecimal change) { }
