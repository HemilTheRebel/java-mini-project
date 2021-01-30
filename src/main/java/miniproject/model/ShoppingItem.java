package miniproject.model;

public class ShoppingItem {
    public int id;
    public String name;
    public double price;

    public ShoppingItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingItem that = (ShoppingItem) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
