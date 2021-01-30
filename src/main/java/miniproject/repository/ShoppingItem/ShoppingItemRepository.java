package miniproject.repository.ShoppingItem;

import miniproject.model.ShoppingItem;

import java.sql.SQLException;
import java.util.List;

public interface ShoppingItemRepository {
    List<ShoppingItem> all() throws SQLException;
}
