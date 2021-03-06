import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class StockControl extends JFrame {
    private JPanel mainPanel;
    public static DefaultTableModel model;

    public static void main(String[] args) {
        StockControl frame = new StockControl();
        frame.setVisible(true);
    }

    public static void getLowStock(Object [][] allItems) {
        for (Object[] allItem : allItems) {
            if (Integer.parseInt(allItem[6].toString()) < 20) {
                model.addRow(allItem);
            }
        }
    }

    public StockControl() {
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        ArrayList<String> fields = new ArrayList<>();
        fields.add("Depo ID");
        fields.add("Depo Adı");
        fields.add("Ürün ID");
        fields.add("Ürün Adı");
        fields.add("Ürün Adet");
        fields.add("İşlem Tipi");
        fields.add("İşlem Tarihi");

        Object[] columnNames = fields.toArray();
        model = new DefaultTableModel(columnNames, 0);

        Object[][] allItems = MainFrame.getTableData(MainFrame.model);
        getLowStock(allItems);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);

    }
}
