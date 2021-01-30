package miniproject;

import miniproject.model.Bill;
import miniproject.model.ShoppingItem;
import miniproject.repository.Bills.BillRepository;
import miniproject.repository.Bills.MysqlBillRepository;
import miniproject.repository.ShoppingItem.MysqlShoppingItemRepository;
import miniproject.repository.ShoppingItem.ShoppingItemRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        BillRepository billRepository = new MysqlBillRepository();

        ShoppingItemRepository shoppingItemRepository = new MysqlShoppingItemRepository();

        Map<ShoppingItem, Integer> items = new HashMap<>();

        for (ShoppingItem item : shoppingItemRepository.all()) {
            items.put(item, 1);
        }

        Bill bill = new Bill("Abc", items, 5);
        billRepository.add(bill);
        System.out.println(billRepository.todaysBills());
        System.out.println(billRepository.all());
    }
}