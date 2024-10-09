import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StudentDataClass extends JFrame {

    private JLabel jtitle;
    private JLabel studentName, studentID, studentGrade, dobLabel, genderLabel, contactLabel, emailLabel;
    private JTextField jstudentName, jsStudentIDNumber, jstudentGrade, dobField, contactField, emailField, searchField;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JButton addStudent, reset, deleteRecord, searchButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private static final String FILE_PATH = "../DATAS/student_data.txt";

    public StudentDataClass() {
        setTitle("Student Data Management");
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(70, 130, 180));
        jtitle = new JLabel("STUDENT DATA MANAGEMENT");
        jtitle.setFont(new Font("Gaseok One", Font.BOLD, 30));
        jtitle.setForeground(Color.WHITE);
        titlePanel.add(jtitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        studentName = new JLabel("Student Name:");
        inputPanel.add(studentName, gbc);
        gbc.gridx++;
        jstudentName = new JTextField(15);
        inputPanel.add(jstudentName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        studentID = new JLabel("Student ID Number:");
        inputPanel.add(studentID, gbc);
        gbc.gridx++;
        jsStudentIDNumber = new JTextField(15);
        inputPanel.add(jsStudentIDNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        studentGrade = new JLabel("Student Grade:");
        inputPanel.add(studentGrade, gbc);
        gbc.gridx++;
        jstudentGrade = new JTextField(15);
        inputPanel.add(jstudentGrade, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        dobLabel = new JLabel("Date of Birth:");
        inputPanel.add(dobLabel, gbc);
        gbc.gridx++;
        dobField = new JTextField(15);
        inputPanel.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        genderLabel = new JLabel("Gender:");
        inputPanel.add(genderLabel, gbc);
        gbc.gridx++;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        inputPanel.add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        contactLabel = new JLabel("Contact Number:");
        inputPanel.add(contactLabel, gbc);
        gbc.gridx++;
        contactField = new JTextField(15);
        inputPanel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        emailLabel = new JLabel("Email:");
        inputPanel.add(emailLabel, gbc);
        gbc.gridx++;
        emailField = new JTextField(15);
        inputPanel.add(emailField, gbc);

        add(inputPanel, BorderLayout.WEST);
        jstudentName.addActionListener(e -> jsStudentIDNumber.requestFocus());
        jsStudentIDNumber.addActionListener(e -> jstudentGrade.requestFocus());
        jstudentGrade.addActionListener(e -> dobField.requestFocus());
        dobField.addActionListener(e -> contactField.requestFocus());
        contactField.addActionListener(e -> emailField.requestFocus());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        addStudent = new JButton("Add Student");
        reset = new JButton("Reset");
        deleteRecord = new JButton("Delete Record");
        searchField = new JTextField(10);
        searchButton = new JButton("Search by Student ID Number");
        buttonPanel.add(addStudent);
        buttonPanel.add(reset);
        buttonPanel.add(deleteRecord);
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Student Name", "Student ID Number", "Student Grade", "Date of Birth", "Gender", "Contact Number", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        studentTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentActionPerformed();
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetActionPerformed();
            }
        });
        deleteRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecordActionPerformed();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonActionPerformed();
            }
        });

        loadStudentDataFromFile();

        setVisible(true);
    }

    private void addStudentActionPerformed() {
        String name = jstudentName.getText();
        String id = jsStudentIDNumber.getText();
        String grade = jstudentGrade.getText();
        String dob = dobField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String gender = maleRadio.isSelected() ? "Male" : "Female";

        if (name.isEmpty() || grade.isEmpty() || dob.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidDate(dob)) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth. Use the format 'dd-MM-yyyy'.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidGrade(grade)) {
            JOptionPane.showMessageDialog(this, "Invalid student grade. It should be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidContactNumber(contact)) {
            JOptionPane.showMessageDialog(this, "Invalid contact number. It should be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String[] data = {name, id, grade, dob, gender, contact, email};
            tableModel.addRow(data);

            jstudentName.setText("");
            jsStudentIDNumber.setText("");
            jstudentGrade.setText("");
            dobField.setText("");
            genderGroup.clearSelection();
            contactField.setText("");
            emailField.setText("");

            saveStudentDataToFile();
        }
    }

    private void resetActionPerformed() {
        jstudentName.setText("");
        jsStudentIDNumber.setText("");
        jstudentGrade.setText("");
        dobField.setText("");
        genderGroup.clearSelection();
        contactField.setText("");
        emailField.setText("");
    }

    private void deleteRecordActionPerformed() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            saveStudentDataToFile();
        }
    }

    private void searchButtonActionPerformed() {
        String searchId = searchField.getText();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (tableModel.getValueAt(row, 1).equals(searchId)) {
                studentTable.setRowSelectionInterval(row, row);
                studentTable.setSelectionBackground(Color.YELLOW);
                studentTable.setSelectionForeground(Color.BLACK);
                break;
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidGrade(String grade) {
        return grade.matches("\\d+");
    }

    private boolean isValidContactNumber(String contact) {
        return contact.matches("\\d+");
    }

    private void loadStudentDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tableModel.addRow(data);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void saveStudentDataToFile() {
        File directory = new File("../DATAS");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    
        File file = new File(directory, "student_data.txt");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    writer.write(tableModel.getValueAt(row, col).toString());
                    if (col < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        new StudentDataClass();
    }
}
