package ffbs;

import java.math.BigDecimal;

public record MenuItem(String id, String name, BigDecimal price, boolean available) { }
