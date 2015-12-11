/*
 * Title        : Core Java JDBC Implementation 
 * Author       : Getha Jagannathan
 * Date Created : 10/16/2015
 * Description  : Simple implementation of CRUD operations using Java and MySql database. The data is displayed in a table format
 * Date Modified: 11/22/2015
 * Modified By  : Getha Jagannathan
 * Modifications: 
 */

package javajdbc;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

public class JavaJDBC extends JFrame
{
        //Declare all global variables
        
    JPanel pFull = new JPanel(new BorderLayout());
    JPanel pLabel = new JPanel(new GridLayout(4,3,10,10));
    JLabel ID = new JLabel("Enter ID");
    JTextField IDText = new JTextField(10);
    JButton bInsert = new JButton("Insert");
    JLabel Name = new JLabel("Enter Name");
    JTextField NameText = new JTextField(10);
    JButton bUpdate = new JButton("Update");
    JLabel Vendor = new JLabel("Enter Vendor");
    JTextField VendorText = new JTextField(10);
    JButton bDelete = new JButton("Delete");
    JLabel Type = new JLabel("Enter Type");
    JTextField TypeText = new JTextField(10);
    JButton bClear = new JButton("Clear");
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;
      
    Connection connection=null;
    Statement stmt = null;
    ResultSet rs = null;
        
    //Constructor
    public JavaJDBC()
    {
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();

        //  Connect to a MySQL Database
        String url = "jdbc:mysql://localhost:3306/learnjava";
        String userid = "root";
        String password = "*******";
        //Query to populate the table - select all the cars
        String sql = "SELECT * FROM cars";

        //Setup MySql Connection, execute query and populate the table
        try 
        {   connection = DriverManager.getConnection( url, userid, password );
            stmt = connection.createStatement();
            rs = stmt.executeQuery( sql );
            table = new JTable(buildTableModel(rs));
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            scrollPane = new JScrollPane( table );
            //getContentPane().add(scrollPane);
            pFull.add(scrollPane,BorderLayout.PAGE_START);     
        }
        catch (SQLException e)
        {
            
        }
       createNewGUI();
       
       // Define Action Listeners for the Insert, Update, Delete buttons
       bInsert.addActionListener(new  ActionListener() 
       {
            public void actionPerformed(ActionEvent e) {
            try
            {
                theQuery("insert into cars(carid,name,vendor,type) values('"+IDText.getText()+"','"+NameText.getText()+"','"+VendorText.getText()+"','"+TypeText.getText()+"')");
                clearText();
            }
            catch(Exception ex)
            {
            }
            }
        });
       //Delete button action
       bDelete.addActionListener(new  ActionListener() 
       {
            public void actionPerformed(ActionEvent e) 
            {
               int response = JOptionPane.showConfirmDialog(null, "Do you want to Delete the record?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               if ((response==JOptionPane.YES_OPTION))
               {
                   try
                   {
                        theQuery("delete from cars where carid='"+IDText.getText()+"' and name='"+NameText.getText()+"'");
                        clearText();
                    }
                    catch(Exception ex){}
                }
               else if ((response == JOptionPane.NO_OPTION) || (response==JOptionPane.CLOSED_OPTION))
               {
                   
               }
            }});
       //Update Button Action
       bUpdate.addActionListener(new  ActionListener() 
       {
            public void actionPerformed(ActionEvent e) {
            try{
                theQuery("update cars set name = '"+NameText.getText()+"',vendor='"+VendorText.getText()+"',type='"+TypeText.getText()+"' where carid='"+IDText.getText()+"'");
                clearText();
            }
            catch(Exception ex){}
            }
        });
       //When a row in the table is selected the values in the TextFields are populated
     table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                IDText.setText(model.getValueAt(row, 0).toString());
                NameText.setText(model.getValueAt(row, 1).toString());
                VendorText.setText(model.getValueAt(row, 2).toString());
                TypeText.setText(model.getValueAt(row, 3).toString());
                IDText.setEditable(false);
            }
        });
     //Clear Button clears the textbox contents
     bClear.addActionListener(new  ActionListener() 
       {
            public void actionPerformed(ActionEvent e) 
            {
                clearText();
            }
       });
    }
    
    public void clearText()
    {
        IDText.setText(null);
        NameText.setText(null);
        VendorText.setText(null);
        TypeText.setText(null);
        IDText.setEditable(true);
    }
    public void theQuery(String Query)//
    {
        Connection con = null;
        Statement st = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost/learnjava","root","nandhu1983");
            st = con.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null,"The table has been updated");
            //re-populate the table
            rs = stmt.executeQuery("select * from cars" );
            table.setModel(buildTableModel(rs));  
            ((DefaultTableModel)table.getModel()).fireTableDataChanged(); 
        }
        catch(Exception ex)
        {
          JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    
    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException 
    {
        ResultSetMetaData metaData = rs.getMetaData();
        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        //for (int column = 1; column <= columnCount; column++) 
        //{columnNames.add(metaData.getColumnName(column));}
        columnNames.add("ID");
        columnNames.add("Car Name");
        columnNames.add("Manufacturer");
        columnNames.add("Model");
    
        // Move the resultset into a vector
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) 
        {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
            {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        
        model = new DefaultTableModel(data, columnNames);
        return model;
    }
    public void createNewGUI()
    {
        //JPanel pButton = new JPanel(new GridLayout(3,3,10,10));
        pFull.add(pLabel,BorderLayout.CENTER);
        
       // ID.setForeground(Color.red);
       // ID.setFont(new Font("Courier New", Font.ITALIC, 20));
        pLabel.add(ID);
        pLabel.add(IDText);
        pLabel.add(bInsert);
        pLabel.add(Name);
        pLabel.add(NameText);
        pLabel.add(bUpdate);
        pLabel.add(Vendor);
        pLabel.add(VendorText);
        pLabel.add(bDelete);
        pLabel.add(Type);
        pLabel.add(TypeText);
        pLabel.add(bClear);
        getContentPane().add(pFull);
        //pFull.add(pButton,BorderLayout.CENTER);
    }

    public static void main(String[] args)
    {
        JavaJDBC frame = new JavaJDBC();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible(true);
        
    }
}