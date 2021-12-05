import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockUpdate extends JFrame {
    private JButton doneButton;
    private ButtonGroup group;
    private JTextField adetTextField;
    private JTextField idTextField;

    public static void main(String stockName) {
        StockUpdate frame = new StockUpdate(stockName);
    }

    public void clearInputs(){
        adetTextField.setText("");
        idTextField.setText("");
        group.clearSelection();
        doneButton.setEnabled(false);
    }

    public StockUpdate(String stockName) {
        setSize(300,441);
        setLayout(null);
        setVisible(true);

        //Logo Panel
        JPanel topLogoPanel=new JPanel();
        topLogoPanel.setBackground(new Color(92,61,188));
        topLogoPanel.setBounds(0,0,300,75);
        //Logo islemleri
        ImageIcon img=new ImageIcon(new ImageIcon("assets/getir-unicorn.jpeg").getImage().getScaledInstance(150,75,Image.SCALE_SMOOTH));
        JLabel picLabel=new JLabel();
        picLabel.setSize(150, 75);
        picLabel.setIcon(img);
        picLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLogoPanel.add(picLabel);
        add(topLogoPanel);

        //Logic Panel
        JPanel midPanel=new JPanel();
        midPanel.setBounds(0,75,300,250);

        JLabel idLabel=new JLabel();
        idLabel.setText("Urun ID:");

        idTextField=new JTextField();
        idTextField.setColumns(15);

        JPanel row1=new JPanel();
        row1.setBounds(0,0,300,100);
        row1.add(idLabel);
        row1.add(idTextField);
        row1.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        midPanel.add(row1);


        JPanel row2=new JPanel();
        JLabel adetLabel=new JLabel();
        adetLabel.setText("Urun Adet:");

        adetTextField=new JTextField();
        adetTextField.setColumns(15);

        row2.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        row2.setBounds(0,100,300,100);
        row2.add(adetLabel);
        row2.add(adetTextField);
        midPanel.add(row2);

        JPanel row3=new JPanel();
        row3.setBounds(0,200,300,50);
        row3.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        group=new ButtonGroup();

        JRadioButton ekleRad=new JRadioButton("Ekle");
        ekleRad.setActionCommand("ekle");
        ekleRad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doneButton.setEnabled(true);
            }
        });
        group.add(ekleRad);
        row3.add(ekleRad);

        JRadioButton cikarRad=new JRadioButton("Cikar");
        cikarRad.setActionCommand("cikar");
        cikarRad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doneButton.setEnabled(true);
            }
        });
        group.add(cikarRad);
        row3.add(cikarRad);

        JRadioButton guncelleRad=new JRadioButton("Guncelle");
        guncelleRad.setActionCommand("guncelle");
        guncelleRad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doneButton.setEnabled(true);
            }
        });
        group.add(guncelleRad);
        row3.add(guncelleRad);

        JRadioButton silRad=new JRadioButton("Sil");
        silRad.setActionCommand("sil");
        silRad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doneButton.setEnabled(true);
            }
        });
        group.add(silRad);
        row3.add(silRad);

        midPanel.add(row3);
        //buton textfield ekleme
        add(midPanel);

        JPanel row4=new JPanel();
        doneButton=new JButton();
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (group.getSelection().getActionCommand()){
                    case "ekle" -> MainFrame.addStock(stockName, idTextField.getText(), Integer.parseInt(adetTextField.getText()), 1);
                    case "cikar" -> MainFrame.addStock(stockName, idTextField.getText(), Integer.parseInt(adetTextField.getText()), -1);
                    case "guncelle" -> MainFrame.updateStock(stockName, idTextField.getText(), Integer.parseInt(adetTextField.getText()));
                    case "sil" -> MainFrame.DeleteStock(stockName, idTextField.getText());
                }
                clearInputs();
            }
        });
        doneButton.setText("Uygula");
        doneButton.setEnabled(false);
        row4.add(doneButton);
        midPanel.add(row4);

        //Done Panel
        JPanel donePanel=new JPanel();
        donePanel.setBackground(new Color(92,61,188));
        donePanel.setBounds(0,325,300,80);
        add(donePanel);
    }
}
