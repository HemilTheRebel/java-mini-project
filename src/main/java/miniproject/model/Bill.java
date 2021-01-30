package miniproject.model;

import java.util.Date;
import java.util.Map;


/**
 * Represents a bill. If you store data
 */
public class Bill {
    public int id;
    public String customer;
    public Map<ShoppingItem, Integer> items;
    public double gstPercent, priceExcludingTax;
    public Date created = new Date();

    public Bill(String customer, Map<ShoppingItem, Integer> items, double gstPercent) {
        this.customer = customer;
        this.items = items;
        this.gstPercent = gstPercent;

        priceExcludingTax = 0;

        for (Map.Entry<ShoppingItem, Integer> entry : items.entrySet()) {
            ShoppingItem item = entry.getKey();
            int quantity = entry.getValue();
            priceExcludingTax += item.price * quantity;
        }
    }

    public Bill(int id, String customer, double gstPercent, double priceExcludingTax, Date created) {
        this.id = id;
        this.customer = customer;
        this.gstPercent = gstPercent;
        this.priceExcludingTax = priceExcludingTax;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", items=" + items +
                ", gstPercent=" + gstPercent +
                ", priceExcludingTax=" + priceExcludingTax +
                ", created=" + created +
                '}';
    }
}
