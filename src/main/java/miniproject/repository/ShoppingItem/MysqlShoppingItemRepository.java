package miniproject.repository.ShoppingItem;

import miniproject.ProjectConfig;
import miniproject.model.ShoppingItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlShoppingItemRepository implements ShoppingItemRepository {
    @Override
    public List<ShoppingItem> all() throws SQLException {
        try (Connection connection = DriverManager.getConnection(ProjectConfig.JDBC_CONNECTION_STRING);
             Statement statement = connection.createStatement()) {
            statement.execute("select id, name, price from shopping_item");
            ResultSet resultSet = statement.getResultSet();

            List<ShoppingItem> items = new ArrayList<>();
            while (resultSet.next()) {
                ShoppingItem item = new ShoppingItem(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price")
                );
                items.add(item);
            }
            return items;
        }
    }
}
