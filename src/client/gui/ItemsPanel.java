package client.gui;

import client.Workers.ItemLookup;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import mutualModels.Item;
//import sun.awt.X11.XConstants;

public class ItemsPanel extends Parent {
    private final Transaction t;
    private final Button b;
    private int itemX, itemY, itemWidth, itemHeight;
    private final LinkedBlockingQueue<Item> items;
    private JTextArea itemList;
    private JTextField itemInput;
    private JLabel totalLabel;
    private JLabel taxLabel;
    private JLabel btLabel; //total before tax
    private double btCost;
    private double totalCost;
    private double tax;

    public ItemsPanel(Frame f, Transaction t) {
        super(f);
        this.t = t;
        this.items = new LinkedBlockingQueue<>();
        
        itemX = 100;
        itemY = 375;
        itemWidth = 200;
        itemHeight = 12;
        
        //This will be replaced by a screenshot of item table
        itemList = new JTextArea("Item name \t Price \t item ID \n");
        itemList.setBounds(0,150,this.getWidth(),200);
        //itemList.setBackground(Color.blue);
        add(itemList);
        
        itemInput = new JTextField("Enter Item #");
        itemInput.setBounds(190,100,140,50);
        add(itemInput);
        
        b = new Button(this, "click to generate an item", 340, 100, 300, 50);
        
        btLabel = new JLabel("Cost: $" + btCost);
        btLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        btLabel.setForeground(Color.WHITE);
        btLabel.setBounds(340, 500, 250, 50);
        add(btLabel);
        
        taxLabel = new JLabel("Tax: $" + tax);
        taxLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        taxLabel.setForeground(Color.WHITE);
        taxLabel.setBounds(340, 550, 250, 50);
        add(taxLabel);
        
        
        totalLabel = new JLabel("Total: $" + totalCost);
        totalLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setBounds(340, 600, 250, 50);
        add(totalLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        
        if(obj == b) {
            int id = Integer.parseInt(itemInput.getText());
            new ItemLookup(id, this).execute();
            
        }
        
    }
    
    public void addLabel(String label) {
        itemList.append(label);
        
    }
    
    public void addItem(Item item) {
        synchronized(items) {
            System.out.println("client: adding item to cart: " + item.getItem_name());
            items.add(item);
            
            
            btCost+=item.getItem_price();
            tax = btCost*.06;
            totalCost = btCost + tax;
            btLabel.setText("Cost: $"+btCost);
            totalLabel.setText("Total: $" + totalCost);
            taxLabel.setText("Tax: $"+ tax);
        }
    }
    
    public void removeItem(Item item) {
        synchronized(items) {
            System.out.println("client: removing item to cart: " + item.getItem_name());
            items.remove(item);
            totalCost-=item.getItem_price();
            totalLabel.setText("Total: $" + totalCost);
           
        }
    }
    
    public LinkedBlockingQueue<Item> getItems() {
        synchronized(items) {
            return items;
        }
    }

}
