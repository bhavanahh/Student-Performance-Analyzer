package spa;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MarksPanel extends JPanel {
    Connection conn;
    JTextField studentId, courseId, mark;
    JTable table;
    DefaultTableModel model;
    JButton add, view, clear;

    public MarksPanel(){
        setLayout(new BorderLayout());
        connectDB();

        JPanel form = new JPanel();
        studentId = new JTextField(10); 
        courseId = new JTextField(10); 
        mark = new JTextField(5);
        form.add(new JLabel("Student ID:"));
        form.add(studentId);
        form.add(new JLabel("Course ID:"));
        form.add(courseId);
        form.add(new JLabel("Mark:"));
        form.add(mark);

        JPanel buttons = new JPanel();
        add = new JButton("Add");
        view = new JButton("View All");
        clear = new JButton("Clear");
        buttons.add(add);
        buttons.add(view);
        buttons.add(clear);

        model = new DefaultTableModel(new String[]{"ID","Student","Course","Mark"},0);
        table = new JTable(model);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> addMark());
        view.addActionListener(e -> loadMarks());
        clear.addActionListener(e -> clearFields());

        loadMarks();
    }

    void connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb","root","");
        } catch(Exception e){ 
            e.printStackTrace(); 
        }
    }

    void addMark(){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO marks(student_id,course_id,mark) VALUES(?,?,?)");
            ps.setString(1,studentId.getText());
            ps.setString(2,courseId.getText());
            ps.setInt(3,Integer.parseInt(mark.getText()));
            ps.executeUpdate(); 
            loadMarks(); clearFields();
        } catch(Exception e){ 
            e.printStackTrace();
                            }
    }

    void loadMarks(){
        model.setRowCount(0);
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT m.id,s.name,c.name,m.mark FROM marks m JOIN students s ON m.student_id=s.id JOIN courses c ON m.course_id=c.id");
            while(rs.next()){
                model.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4)}); 
                            }
        } catch(Exception e){
            e.printStackTrace(); 
        }
    }

    void clearFields(){
        studentId.setText(""); 
        courseId.setText(""); 
        mark.setText(""); 
    }

