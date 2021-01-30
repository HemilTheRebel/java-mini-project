package miniproject.repository.Bills;

import miniproject.model.Bill;

import java.sql.SQLException;
import java.util.List;

public interface BillRepository {
    /**
     * Adds a bill to the database and returns its id
     * @param bill the bill to add
     * @return id of the newly added bill
     * @throws SQLException
     */
    int add(Bill bill) throws SQLException;


    /**
     * Get id, customer, gst_percent, priceExcludingTax and created for all bills to date.
     * @return List of all bills till date
     * @throws SQLException database query. Should never through but compiler complains
     */
    List<Bill> all() throws SQLException;


    /**
     * Get id, customer, gst_percent, priceExcludingTax and created for all bills for today.
     * @return List of all bills for today
     * @throws SQLException database query. Should never through but compiler complains
     */
    List<Bill> todaysBills() throws SQLException;
}
