package ffbs;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    private final MenuItem burger = new MenuItem("B01", "Veg Burger", new BigDecimal("89.00"), true);
    private final MenuItem fries = new MenuItem("F01", "French Fries", new BigDecimal("79.00"), true);
    private final MenuItem drink = new MenuItem("D01", "Cold Drink", new BigDecimal("49.00"), true);
    private final MenuItem shake = new MenuItem("S01", "Seasonal Shake", new BigDecimal("120.00"), false);

    @Test @DisplayName("TC06 Subtotal of 2 burgers, 1 fries, 2 drinks is 355.00")
    void subtotal() {
        Order o = new Order();
        o.addItem(burger, 2); o.addItem(fries, 1); o.addItem(drink, 2);
        assertEquals(new BigDecimal("355.00"), o.subtotal());
    }

    @Test @DisplayName("TC07 Adding the same item twice merges the quantity")
    void mergesQuantity() {
        Order o = new Order();
        o.addItem(burger, 1); o.addItem(burger, 2);
        assertEquals(3, o.quantityOf("B01"));
    }

    @Test @DisplayName("TC08 Unavailable item cannot be added")
    void unavailableItem() {
        assertThrows(IllegalStateException.class, () -> new Order().addItem(shake, 1));
    }

    @Test @DisplayName("TC09 Zero quantity is rejected")
    void zeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new Order().addItem(burger, 0));
    }
}
