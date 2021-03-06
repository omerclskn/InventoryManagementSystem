import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox comboBox1;
    private JButton buttonStock;
    private JButton button1;
    public static DefaultTableModel model;
    public static Object[][] allItems;
    public static Object[] columnNames;
    public static String user = "postgres";
    public static String password = "root";


    public static void main(String[] args) throws SQLException {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
        frame.setTitle("Stock Management");
    }

    public static void logOperations(String type, Object[] props) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("Log.txt", true));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String dateTime = simpleDateFormat.format(calendar.getTime());
            writer.append("\n");
            if (type.equals("ekle")) {
                writer.append("||" + dateTime + "||" + " " + props[0] + " deposuna " + props[1] + " " + props[2] + " adet eklendi.");//depo adi urun adi adet
            }
            if (type.equals("cikar")) {
                writer.append("||" + dateTime + "||" + " " + props[0] + " deposuna " + props[1] + " " + props[2] + " adet silindi.");
            }
            if (type.equals("guncelle")) {
                writer.append("||" + dateTime + "||" + " " + props[0] + " deposuna " + props[1] + " " + props[2] + " adet olarak guncellendi.");
            }
            if (type.equals("sil")) {
                writer.append("||" + dateTime + "||" + " " + props[0] + " deposundan " + props[1] + " silindi.");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object[][] getTableData(DefaultTableModel dtm) {
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                tableData[i][j] = dtm.getValueAt(i, j);
        return tableData;
    }

    public static void getDistinctDepos(JComboBox comboBox, int column) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            set.add(model.getValueAt(i, column).toString());
        }
        TreeSet<String> treeSet = new TreeSet<>(set);
        for (int j = 0; j < treeSet.size(); j++) {
            comboBox.addItem(treeSet.toArray()[j].toString());
        }
    }

    public static void warningMessage() {
        JOptionPane optionPane = new JOptionPane("??r??n Bulunamad??", JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Uyari");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    public static void getLowStock(JComboBox comboBox) {
        if (comboBox.getSelectedIndex() == 0) {
            for (Object[] allItem : allItems) {
                if (Integer.parseInt(allItem[6].toString()) < 10) {
                    model.addRow(new Object[]{allItem[0], allItem[1], allItem[2], allItem[3], allItem[4], allItem[5], allItem[6]});
                }
            }
        } else {
            for (Object[] allItem : allItems) {
                if (Integer.parseInt(allItem[6].toString()) < 10 && allItem[1].toString().equals(comboBox.getSelectedItem().toString())) {
                    model.addRow(new Object[]{allItem[0], allItem[1], allItem[2], allItem[3], allItem[4], allItem[5], allItem[6]});
                }
            }
        }
    }

    public static void updateStock(String stockName, String productId, int value) throws SQLException {
        boolean flag = true;
        Object[][] temp = new Object[allItems.length][7];
        for (int i = 0; i < allItems.length; i++) {
            if (allItems[i][1].toString().equals(stockName) && allItems[i][4].toString().equals(productId)) {
                // UPDATE ????LEM?? YAPILACAK ADET DE??ER?? TAMAMEN DE??????ECEK

                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YBS",user,password);
                String query = "UPDATE products SET quantity = " + value + " WHERE depoAd?? = '" + allItems[i][1].toString() + "'"
                        + " AND pid = " + allItems[i][4].toString();

                java.sql.Statement s;
                s = conn.createStatement();
                s.execute(query);

                allItems[i][6] = value;
                flag = false;
                break;
            }
        }
        if (flag) {
            warningMessage();
        } else {
            model.setDataVector(new Object[][]{}, columnNames);
            for (int i = 0; i < allItems.length; i++) {
                if (allItems[i][1].equals(stockName)) {
                    if (allItems[i][4].equals(productId)) {
                        logOperations("guncelle", new Object[]{allItems[i][1], allItems[i][5], value});
                    }
                    model.addRow(new Object[]{allItems[i][0], allItems[i][1], allItems[i][2], allItems[i][3],
                            allItems[i][4], allItems[i][5], allItems[i][6]});
                }
                temp[i] = allItems[i];
            }
            allItems = temp;
        }
    }

    public static ArrayList<String> getInfoStocks(String stockName) {
        //depoid, adres, telefon dondurur.
        ArrayList<String> temp = new ArrayList<>();
        for (Object[] allItem : allItems) {
            if (allItem[1].toString().equals(stockName)) {
                temp.add((String) allItem[0]);//depoid
                temp.add((String) allItem[2]);//depoadres
                temp.add((String) allItem[3]);//depotelefon
                return temp;
            }
        }
        return null;
    }

    public static void addStock(String stockName, String productId, int value, int type) throws SQLException {
        boolean flag = true;
        Object[][] temp = new Object[allItems.length][7];
        for (int i = 0; i < allItems.length; i++) {
            if (allItems[i][1].toString().equals(stockName) && allItems[i][4].toString().equals(productId)) {
                // UPDATE SORGUSU YAPILACAK TYPE -1 VEYA +1 E G??RE ADET DE??ER?? EKLENECEK/??IKARILACAK

                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YBS",user,password);
                String query = "UPDATE products SET quantity = quantity + " + value + " WHERE depoAd?? = '" + allItems[i][1].toString() + "'"
                        + " AND pid = " + allItems[i][4].toString();

                java.sql.Statement s;
                s = conn.createStatement();
                s.execute(query);

                allItems[i][6] = Integer.parseInt(allItems[i][6].toString()) + (value * type);
                flag = false;
                break;
            }
        }
        String prodName = "placeholder";
        boolean existsKey = false;
        if (flag) {
            for (Object[] allItem : allItems) {
                if (allItem[4].toString().equals(productId)) {
                    prodName = allItem[5].toString();
                    existsKey = true;
                    break;
                }
            }
            if (!existsKey) {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Urun Kodu Bulunamadi...\nYeni urun ekleniyor");
                prodName = JOptionPane.showInputDialog(frame, "Urun Adi:");
            }
            if (prodName == null) {
                return;
            }

            temp = new Object[allItems.length + 1][7];
            for (int i = 0; i < allItems.length; i++) {
                System.arraycopy(allItems[i], 0, temp[i], 0, allItems[0].length);
            }
            temp[temp.length - 1][0] = getInfoStocks(stockName).get(0);//depoid
            temp[temp.length - 1][1] = stockName;//adi var zaten
            temp[temp.length - 1][2] = getInfoStocks(stockName).get(1);//adres
            temp[temp.length - 1][3] = getInfoStocks(stockName).get(2);//telefon
            temp[temp.length - 1][4] = productId;
            temp[temp.length - 1][5] = prodName;
            temp[temp.length - 1][6] = value;
            allItems = temp;
            model.addRow(temp[temp.length - 1]);
            // CREATE INSERT INTO ????LEM?? YAPILACAK

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YBS",user,password);
            String query = "INSERT INTO products values('" + temp[temp.length - 1][0] + "','" + temp[temp.length - 1][1]
                    + "','" + temp[temp.length - 1][2] + "','" + temp[temp.length - 1][3] + "','" + temp[temp.length - 1][4] + "','"
                    + temp[temp.length - 1][5] + "','" + temp[temp.length - 1][6] + "');";

            java.sql.Statement s;
            s = conn.createStatement();
            s.execute(query);

            logOperations("ekle", new Object[]{temp[temp.length - 1][1], temp[temp.length - 1][5], temp[temp.length - 1][6]});
        } else {
            model.setDataVector(new Object[][]{}, columnNames);
            for (int i = 0; i < allItems.length; i++) {
                if (allItems[i][1].equals(stockName)) {
                    if (allItems[i][4].equals(productId)) {
                        if (type < 0) {
                            logOperations("cikar", new Object[]{allItems[i][1], allItems[i][5], value});
                        } else {
                            logOperations("ekle", new Object[]{allItems[i][1], allItems[i][5], value});
                        }
                    }
                    model.addRow(new Object[]{allItems[i][0], allItems[i][1], allItems[i][2], allItems[i][3], allItems[i][4], allItems[i][5], allItems[i][6].toString()});
                }
                temp[i] = allItems[i];
            }
            allItems = temp;
        }
    }

    public static void DeleteStock(String stockName, String productId) throws SQLException {
        boolean flag = true;
        Object[][] temp = new Object[allItems.length - 1][7];
        for (int i = 0; i < allItems.length; i++) {
            if (allItems[i][1].toString().equals(stockName) && allItems[i][4].toString().equals(productId)) {
                // DELETE ????LEM?? YAPILACAK
                logOperations("sil", new Object[]{allItems[i][1], allItems[i][5], allItems[i][6]});

                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YBS",user,password);
                String query = "DELETE FROM products" + " WHERE depoAd?? = '" + allItems[i][1].toString() + "'"
                        + " AND pid = " + allItems[i][4].toString();

                java.sql.Statement s;
                s = conn.createStatement();
                s.execute(query);

                allItems[i] = null;
                flag = false;
                break;
            }
        }
        if (flag) {
            warningMessage();
        } else {
            model.setDataVector(new Object[][]{}, columnNames);
            for (int i = 0, j = 0; i < allItems.length; i++) {
                if (allItems[i] != null) {
                    if (allItems[i][1].equals(stockName)) {
                        model.addRow(new Object[]{allItems[i][0], allItems[i][1], allItems[i][2], allItems[i][3], allItems[i][4], allItems[i][5], allItems[i][6]});
                    }
                    temp[j] = allItems[i];
                    j++;
                }
            }
            allItems = temp;
        }
    }

    public MainFrame() throws SQLException {
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        ArrayList<String> fields = new ArrayList<>();
        fields.add("Depo ID");
        fields.add("Depo Ad??");
        fields.add("Depo Adresi");
        fields.add("Depo Telefon");
        fields.add("??r??n ID");
        fields.add("??r??n Ad??");
        fields.add("??r??n Adet");

        columnNames = fields.toArray();
        model = new DefaultTableModel(columnNames, 0);
        // SELECT SORGUSU ATIP T??M VER?? TABLE A ??EK??LECEK ARDINDAN gettabledata fonksiyonu ile table object[][] tipine ??evirilecek
//        model.addRow(new Object[]{"1", "Depo 1", "Depo Adresi 1", "Depo Telefon 1", "1", "??r??n 1", "1000"});
//        model.addRow(new Object[]{"2", "Depo 2", "Depo Adresi 2", "Depo Telefon 2", "2", "??r??n 2", "2000"});
//        model.addRow(new Object[]{"2", "Depo 2", "Depo Adresi 2", "Depo Telefon 2", "3", "??r??n 3", "3000"});
//        model.addRow(new Object[]{"3", "Depo 3", "Depo Adresi 3", "Depo Telefon 3", "4", "??r??n 4", "500"});
//        model.addRow(new Object[]{"4", "Depo 4", "Depo Adresi 4", "Depo Telefon 4", "5", "??r??n 5", "6"});

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/YBS",user,password);
        String query = "SELECT * FROM products";
        java.sql.Statement s;
        s = conn.createStatement();

        ResultSet r = s.executeQuery(query);

        while(r.next()) {
            model.addRow(new Object[]{r.getString(1), r.getString(2), r.getString(3),
            r.getString(4), r.getString(5), r.getString(6), r.getString(7)});
        }

        allItems = getTableData(model);
        JTable table = new JTable(model);
        table.setPreferredSize(new Dimension(800, 500));
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);

        comboBox1 = new JComboBox();
        comboBox1.addItem("B??t??n Depolar");
        getDistinctDepos(comboBox1, 1);
        comboBox1.addActionListener(e -> {
            model.setDataVector(new Object[][]{}, columnNames);
            String selectedItem = comboBox1.getSelectedItem().toString();
            if (selectedItem.equals("B??t??n Depolar")) {
                buttonStock.setEnabled(false);
                for (Object[] allItem : allItems) {
                    model.addRow(allItem);
                }
            } else {
                buttonStock.setEnabled(true);
                for (Object[] allItem : allItems) {
                    if (allItem[1].equals(selectedItem)) {
                        model.addRow(allItem);
                    }
                }
            }
        });
        mainPanel.add(comboBox1);

        buttonStock = new JButton("Stok G??ncelle");
        buttonStock.setEnabled(false);
        buttonStock.addActionListener(e -> {
            StockUpdate stockUpdate = new StockUpdate(comboBox1.getSelectedItem().toString());
            stockUpdate.setVisible(true);
        });
        mainPanel.add(buttonStock);

        button1 = new JButton("Stok Kontrol??");
        button1.addActionListener(e -> {
            model.setDataVector(new Object[][]{}, columnNames);
            getLowStock(comboBox1);
        });
        mainPanel.add(button1);
    }
}
