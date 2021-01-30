package miniproject.swing;

import miniproject.model.Bill;
import miniproject.model.ShoppingItem;
import miniproject.repository.Bills.BillRepository;
import miniproject.repository.ShoppingItem.ShoppingItemRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainScreen {
    public MainScreen(ShoppingItemRepository shoppingItemRepository, BillRepository billRepository) throws SQLException {
        List<ShoppingItem> allShoppingItems = shoppingItemRepository.all();

        DefaultTableModel tableModel = new DefaultTableModel(new String[] {"ID", "Name", "Quantity", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        JTable shoppingItemsTable = new JTable(tableModel);
        Map<ShoppingItem, Integer> selectedItems = new HashMap<>();

        AtomicBoolean programaticEdit = new AtomicBoolean(false);
        tableModel.addTableModelListener(l -> {
            if(shoppingItemsTable.isEditing() && !programaticEdit.get()) {
                int selectedRow = shoppingItemsTable.getSelectedRow();
                int quantity = Integer.parseInt(shoppingItemsTable.getValueAt(selectedRow,2).toString());
                int id = (int) shoppingItemsTable.getValueAt(selectedRow,0);
                String name = (String) shoppingItemsTable.getValueAt(selectedRow,1);
                double price = (double) shoppingItemsTable.getValueAt(selectedRow,3);
                ShoppingItem item = new ShoppingItem(id, name, price);
                int originalQuantity = selectedItems.get(item);
                price /= originalQuantity;
                selectedItems.put(item, quantity);
                programaticEdit.set(true);
                shoppingItemsTable.setValueAt(price * quantity, selectedRow, 3);
                programaticEdit.set(false);
            }
        });

        JComboBox<ShoppingItem> shoppingItemComboBox = new JComboBox<>(allShoppingItems.toArray(new ShoppingItem[0]));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton addButton = new JButton("Add");

        addButton.addActionListener(a -> {
            ShoppingItem item = (ShoppingItem) shoppingItemComboBox.getSelectedItem();
            assert item != null;

            int quantity = (int) quantitySpinner.getValue();

            if (selectedItems.get(item) == null) {
                tableModel.addRow(new Object[] {item.id, item.name, quantity, item.price});
                selectedItems.put(item, quantity);
            }
        });

        JLabel customerLabel = new JLabel("Customer Name: ");
        JTextField customerName = new JTextField(30);

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.add(customerLabel);
        horizontalPanel.add(customerName);

        JButton saveButton = new JButton("Save");

        JPanel horizontalPanel2 = new JPanel();
        horizontalPanel2.add(shoppingItemComboBox);
        horizontalPanel2.add(quantitySpinner);
        horizontalPanel2.add(addButton);

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.add(horizontalPanel);
        verticalPanel.add(horizontalPanel2);
        verticalPanel.add(new JScrollPane(shoppingItemsTable));
        verticalPanel.add(saveButton);

        JFrame frame = new JFrame();
        frame.add(verticalPanel);

        /// Added here cause we want to refer to frame
        saveButton.addActionListener(a -> {
            String customer = customerName.getText();
            if ("".equals(customer)) {
                JOptionPane.showMessageDialog(frame, "Customer cannot be empty");
                return;
            }

            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select at least one item");
                return;
            }

            Bill bill = new Bill(customer, selectedItems, 5);
            try {
                billRepository.add(bill);

                JOptionPane.showMessageDialog(frame, "Bill Saved");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
