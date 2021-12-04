import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.instrument.Instrumentation;
import java.util.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox comboBox1;
    private JButton buttonStock;
    public static DefaultTableModel model;

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }

    public static Object[][] getTableData (JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0 ; i < nRow ; i++)
            for (int j = 0 ; j < nCol ; j++)
                tableData[i][j] = dtm.getValueAt(i,j);
        return tableData;
    }

    public static void getDistinctDepos(JComboBox comboBox, int column) {
        Set<String> set = new HashSet<String>();
        for(int i = 0; i < model.getRowCount(); i++){
            set.add(model.getValueAt(i, column).toString());
        }
        TreeSet<String> treeSet = new TreeSet<String>(set);
        for (int j = 0; j < treeSet.size(); j++) {
            comboBox.addItem(treeSet.toArray()[j].toString());
        }
    }

    public static void updateStock(JComboBox comboBox, JComboBox comboBox2, int value, Object[][] allItems) {
        String selectedDepo = comboBox.getSelectedItem().toString();
        String selectedStock = comboBox2.getSelectedItem().toString();
        boolean flag = true;
        for(int i = 0; i < allItems.length; i++){
            if(allItems[i][1].toString().equals(selectedDepo) && allItems[i][4].toString().equals(selectedStock)){
                model.setValueAt("Ürün Adet: " + value, i, 6);
                flag = false;
                break;
            }
        }
        if(flag){
            System.out.println("Ürün bulunamadı");
        }
    }

    public MainFrame() {
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,500);

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("Depo ID");
        fields.add("Depo Adı");
        fields.add("Depo Adresi");
        fields.add("Depo Telefon");
        fields.add("Ürün ID");
        fields.add("Ürün Adı");
        fields.add("Ürün Adet");
        fields.add("İşlem Tipi");
        fields.add("İşlem Tarihi");

        Object[] columnNames = fields.toArray();
        model = new DefaultTableModel(columnNames, 0);

        model.addRow(new Object[]{"1", "Depo 1", "Depo Adresi 1", "Depo Telefon 1", "Ürün ID: 1", "Ürün 1", "Ürün Adet 1", "Giriş", "İşlem Tarihi 1"});
        model.addRow(new Object[]{"2", "Depo 2", "Depo Adresi 2", "Depo Telefon 2", "Ürün ID: 2", "Ürün 2", "Ürün Adet 2", "Giriş", "İşlem Tarihi 2"});
        model.addRow(new Object[]{"2", "Depo 2", "Depo Adresi 2", "Depo Telefon 2", "Ürün ID: 3", "Ürün 3", "Ürün Adet 3", "Çıkış", "İşlem Tarihi 3"});
        model.addRow(new Object[]{"3", "Depo 3", "Depo Adresi 3", "Depo Telefon 3", "Ürün ID: 3", "Ürün 4", "Ürün Adet 4", "Çıkış", "İşlem Tarihi 4"});
        model.addRow(new Object[]{"3", "Depo 3", "Depo Adresi 3", "Depo Telefon 3", "Ürün ID: 4", "Ürün 5", "Ürün Adet 5", "Giriş", "İşlem Tarihi 5"});
        model.addRow(new Object[]{"4", "Depo 4", "Depo Adresi 4", "Depo Telefon 4", "Ürün ID: 5", "Ürün 6", "Ürün Adet 6", "Çıkış", "İşlem Tarihi 6"});

        Object[][] allItems =  getTableData(new JTable(model));
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);

        comboBox1 = new JComboBox();
        comboBox1.addItem("Bütün Depolar");
        getDistinctDepos(comboBox1, 1);
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements();
                String selectedItem = comboBox1.getSelectedItem().toString();
                if(selectedItem.equals("Bütün Depolar")){
                    for (Object[] allItem : allItems) {
                        model.addRow(allItem);
                    }
                }
                else{
                    for (Object[] allItem : allItems) {
                        if (allItem[1].equals(selectedItem)) {
                            model.addRow(allItem);
                        }
                    }
                }
            }
        });
        mainPanel.add(comboBox1);

        buttonStock = new JButton("Stok Güncelle");
        buttonStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockUpdate stockUpdate = new StockUpdate();
                stockUpdate.setVisible(true);
            }
        });
        mainPanel.add(buttonStock);

    }


}
