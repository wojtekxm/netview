package zesp03.servlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.DefaultTableModel;



public class Controllers extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    private Connection conn = null;
    private PreparedStatement pstmt = null;
    ResultSet rset = null;
    String idg;
    DefaultTableModel model;

    private void updateTable()
    {
        String sql = "SELECT Id, Name, IPv4, Description FROM Controller Order by Id";

        try
        {
            pstmt = conn.prepareStatement(sql);
            rset = pstmt.executeQuery();

            while ( rset.next() )
            {
                model.addRow(new Object[] { rset.getString("Id"), rset.getString("Name"), rset.getString("IPv4"), rset.getString("Description")});
            }
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }

    public Controllers() {
        initComponents();
    }
    public void pack() { }

    private void initComponents() {
        JFrame Frame = new JFrame("Controllers");
        JPanel Panel1 = new JPanel();
        JLabel Label1 = new JLabel("Contorller's name");
        JTextField TextField1 = new JTextField();
        JLabel Label2 = new JLabel("Controller's IPv4");
        JTextField TextField2 = new JTextField();
        JLabel Label3 = new JLabel("Description");
        JTextField TextField3 = new JTextField();
        JButton Button1 = new JButton("Add");
        JButton Button2 = new JButton("Delete");
        JTable Table1 = new JTable();
        JScrollPane ScrollPane1 = new JScrollPane();
        Container Frame1 = Frame.getContentPane();

        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        Table1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Controller's name", "Controller's IPv4", "Description"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Byte.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                    false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ScrollPane1.setViewportView(Table1);

        /*Button2.setText("jButton2");
        Button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });*/

        GroupLayout Panel1Layout = new GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
                Panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(Panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(Panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(Label1)
                                        .addComponent(TextField1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Label2)
                                        .addComponent(TextField2, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(Panel1Layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(Label3))
                                        .addComponent(TextField3, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Button1)
                                        .addComponent(Button2))
                                .addContainerGap(95, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
                Panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(Panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Label1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Label2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Label3)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TextField3, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Button1)
                                .addComponent(Button2)
                                .addContainerGap(56, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(Frame1);
        Frame1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(Panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        }
    private void AddController(String a, String b, String c)
    {
        try
        {
            pstmt = conn.prepareStatement("INSERT INTO Controller VALUES(seq.nextval, ?, ?, ?)");
            pstmt.setString(1, a);
            pstmt.setString(2, b);
            pstmt.setString(3, c);
            idg=c;

            pstmt.executeUpdate();
            conn.commit();

        }
        catch(SQLException e) {
            System.out.println(c);
        }
    }

    /*private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {

        int row = Table1.getSelectedRow();
        if (row < 0)
            return;
        row = Table1.convertRowIndexToModel(row);

        DefaultTableModel model = (DefaultTableModel) Table1.getModel();
        String title = model.getValueAt(row, 0).toString();
        System.out.println(title);
        try
        {
            pstmt = conn.prepareStatement("DELETE Id, name, ipv4, description FROM Controllers WHERE Id = ?");
            pstmt.setString(1, a);
            pstmt.setString(2, b);
            pstmt.setString(3, c);
            idg=c;

            pstmt.executeUpdate();
            conn.commit();

        }
        catch(SQLException e) {
            System.out.println(c);
        }

        try {
            model.setRowCount(0);
            updateTable();
            JOptionPane.showMessageDialog(new JOptionPane(), "Controller was deleted!", "Information", JOptionPane.INFORMATION_MESSAGE);


        } catch (Exception e) {
        }
    }*/
    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            String a = TextField1.getText();
            String b = TextField2.getText();
            String c = TextField3.getText();
            AddController(a, b, c);
            model.setRowCount(0);
            updateTable();
            JOptionPane.showMessageDialog(new JOptionPane(), "Controller was added!", "Information", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(Exception e) {
        }
    }

    public static void main(String args[]) {
        new Controllers();
        System.out.println("jebany projekt");
    }
        private JLabel Lable1;
        private JLabel Label2;
        private JLabel Label3;
        private JPanel Panel1;
        private JTextField TextField1;
        private JTextField TextField2;
        private JTextField TextField3;
        private JButton JButton1;
        private JTable Table1;
        private JScrollPane ScrollPane1;
        private JFrame Frame;



}
