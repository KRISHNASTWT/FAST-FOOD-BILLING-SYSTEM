package ffbs;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
    private final Map<String, MenuItem> items = new LinkedHashMap<>();
    private final Map<String, Integer> qty = new LinkedHashMap<>();

    public void addItem(MenuItem item, int quantity) {
        if (!item.available())
            throw new IllegalStateException(item.name() + " is not available");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        items.putIfAbsent(item.id(), item);
        qty.merge(item.id(), quantity, Integer::sum);
    }

    public int quantityOf(String itemId) { return qty.getOrDefault(itemId, 0); }

    public boolean isEmpty() { return qty.isEmpty(); }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (var e : qty.entrySet())
            sum = sum.add(items.get(e.getKey()).price()
                    .multiply(BigDecimal.valueOf(e.getValue())));
        return sum;
    }
}
