package miniproject.repository.Bills;


import miniproject.ProjectConfig;
import miniproject.model.Bill;
import miniproject.model.ShoppingItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlBillRepository implements BillRepository {
    public static final String INSERT_INTO_BILL = "insert into bill(customer, gst_percent, price) values (?, ?, ?)";
    public static final String INSERT_INTO_BILL_SHOPPING_ITEMS = "insert into bill_shopping_items values (?, ?, ?)";
    public static final String SELECT_ALL_BILLS = "select id, customer, gst_percent, price, created from bill";
    public static final String SELECT_BILLS_FOR_TODAY = "select id, customer, gst_percent, price, created from bill where date(created) = date(now())";

    @Override
    public int add(Bill bill) throws SQLException {

        try (Connection connection = DriverManager.getConnection(ProjectConfig.JDBC_CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_BILL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, bill.customer);
            statement.setDouble(2, bill.gstPercent);
            statement.setDouble(3, bill.priceExcludingTax);

            statement.executeUpdate();

            int billID;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new IllegalStateException();
                }

                billID = generatedKeys.getInt("id");

                try (PreparedStatement statement2 = connection.prepareStatement(INSERT_INTO_BILL_SHOPPING_ITEMS)) {
                    for (Map.Entry<ShoppingItem, Integer> entry : bill.items.entrySet()) {
                        statement2.setInt(1, billID);
                        statement2.setInt(2, entry.getKey().id);
                        statement2.setInt(3, entry.getValue());
                        statement2.addBatch();
                    }

                    statement2.executeBatch();
                    connection.commit();
                }
            }

            return 0;
        }
    }

    @Override
    public List<Bill> all() throws SQLException {
        return getBills(SELECT_ALL_BILLS);
    }

    @Override
    public List<Bill> todaysBills() throws SQLException {
        return getBills(SELECT_BILLS_FOR_TODAY);
    }

    private List<Bill> getBills(String sql) throws SQLException {
        try (Connection connection = DriverManager.getConnection(ProjectConfig.JDBC_CONNECTION_STRING);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            List<Bill> bills = new ArrayList<>();
            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("id"),
                        resultSet.getString("customer"),
                        resultSet.getDouble("gst_percent"),
                        resultSet.getDouble("price"),
                        resultSet.getDate("created")
                );

                bills.add(bill);
            }

            return bills;
        }
    }
}
