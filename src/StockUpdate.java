import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockUpdate extends JFrame {
    private JPanel stockPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField1;
    private JButton button1;
    private JLabel labelStock;
    private JLabel labelName;
    private JButton button2;

    public static void main(String[] args) {
        StockUpdate frame = new StockUpdate();
        frame.setVisible(true);
    }

    public StockUpdate() {
        stockPanel = new JPanel();
        stockPanel.setSize(200,300);
        setContentPane(stockPanel);

        Object[][] allItems = MainFrame.getTableData(new JTable(MainFrame.model));

        comboBox1 = new JComboBox();
        MainFrame.getDistinctDepos(comboBox1, 1);
        stockPanel.add(comboBox1);

        comboBox2 = new JComboBox();
        MainFrame.getDistinctDepos(comboBox2, 4);
        stockPanel.add(comboBox2);

        textField1 = new JTextField();
        textField1.setSize(200,30);
        stockPanel.add(textField1);

        button1 = new JButton("Getir");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Object[] allItem : allItems) {
                    if (allItem[1].equals(comboBox1.getSelectedItem()) && allItem[4].equals(comboBox2.getSelectedItem())) {
                        labelStock.setText(allItem[6].toString());
                        labelName.setText(allItem[5].toString());
                        button2.setVisible(true);
                    }
                }
            }
            });
        stockPanel.add(button1);

        labelStock = new JLabel();
        stockPanel.add(labelStock);

        labelName = new JLabel();
        stockPanel.add(labelName);

        button2 = new JButton("Güncelle");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.updateStock(comboBox1, comboBox2, Integer.parseInt(textField1.getText()), allItems);
            }
        });
        button2.setVisible(true);
        stockPanel.add(button2);

    }
}
