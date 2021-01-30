package miniproject;

import miniproject.repository.Bills.MysqlBillRepository;
import miniproject.repository.ShoppingItem.MysqlShoppingItemRepository;
import miniproject.swing.MainScreen;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        new MainScreen(new MysqlShoppingItemRepository(), new MysqlBillRepository());
    }
}