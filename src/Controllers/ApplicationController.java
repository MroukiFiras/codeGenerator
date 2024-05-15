package Controllers;

import Views.ApplicationFrame;

import javax.swing.*;
import CodeGenerator.CodeGenerator;
import CodeGenerator.JavaCodeGenerator;
// import CodeGenerator.PythonCodeGenerator;
import Models.Column;
import DAO.TableDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

public class ApplicationController implements ActionListener {
    private ApplicationFrame frame;
    private Connection connection;
    private String databaseName;

    public ApplicationController(Connection connection) {
        this.connection = connection;
        readDatabaseName();
        initialize();
    }

    private void readDatabaseName() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("connection.properties")) {
            properties.load(input);
            databaseName = properties.getProperty("database");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new ApplicationFrame(connection);
        frame.addGenerateButtonListener(this); // Pass the controller instance
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frame.getGenerateButton()) {
            System.out.println("Generate Code button clicked."); // Debugging statement
            generateCode();
        }
    }

    public void generateCode() {
        String[] selectedTables = frame.getSelectedTables();
        String selectedCode = frame.getSelectedCode();
        String selectedInterface = frame.getSelectedInterface();

        System.out.println("Selected code: " + selectedCode); // Debugging statement
        System.out.println("Selected interface: " + selectedInterface); // Debugging statement

        if (selectedCode != null && selectedInterface != null) {
            try {
                TableDAO tableDAO = new TableDAO();
                for (String tableName : selectedTables) {
                    List<Column> columns = tableDAO.getColumnsForTable(databaseName, tableName);
                    CodeGenerator codeGenerator = getCodeGenerator(selectedCode);
                    if (codeGenerator != null) {
                        codeGenerator.generateCode(tableName, columns);
                    }
                    // Handle interface generation (e.g., Swing, HTML) here if needed
                }
                JOptionPane.showMessageDialog(null, "Code generation complete.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error during code generation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select code and interface options.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private CodeGenerator getCodeGenerator(String selectedCode) {
        switch (selectedCode) {
            case "Java":
                return new JavaCodeGenerator();
            case "Python":
                // return new PythonCodeGenerator();
            // Add more cases here for other code generators
            default:
                JOptionPane.showMessageDialog(null, "Unsupported code generation language: " + selectedCode, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
        }
    }
}
