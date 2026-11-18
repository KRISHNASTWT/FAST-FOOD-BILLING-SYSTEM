package ffbs;

import java.math.BigDecimal;

public interface PaymentGateway {
    boolean charge(String mode, BigDecimal amount);   // true = payment approved
}
