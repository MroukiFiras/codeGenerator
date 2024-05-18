package Controllers;

import Views.ApplicationFrame;
import CodeGenerator.HTMLCodeGenerator;
import CodeGenerator.JavaCodeGenerator;
import CodeGenerator.PythonCodeGenerator;
import CodeGenerator.SwingCodeGenerator;
import Models.Column;
import Models.ForeignKey;
import DAO.TableDAO;
import javax.swing.*;
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
        frame.addGenerateButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frame.getGenerateButton()) {
            System.out.println("Generate Code button clicked.");
            generateCode();
        }
    }

    public void generateCode() {
        String[] selectedTables = frame.getSelectedTables();
        String selectedCode = frame.getSelectedCode();
        String selectedInterface = frame.getSelectedInterface();
    
        System.out.println("Selected code: " + selectedCode);
        System.out.println("Selected interface: " + selectedInterface);
    
        if (selectedCode != null && selectedInterface != null) {
            try {
                TableDAO tableDAO = new TableDAO();
                List<Column> columns;
                List<ForeignKey> foreignKeys;
    
                for (String tableName : selectedTables) {
                    columns = tableDAO.getColumnsForTable(databaseName, tableName);
                    foreignKeys = tableDAO.getForeignKeysForTable(databaseName, tableName);
    
                    if ("Java".equals(selectedCode)) {
                        JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator();
                        javaCodeGenerator.generateCode(tableName, columns, foreignKeys);
                    } else if ("Python".equals(selectedCode)) {
                        PythonCodeGenerator pythonCodeGenerator = new PythonCodeGenerator();
                        pythonCodeGenerator.generateCode(tableName, columns, foreignKeys);
                    }
    
                    if ("HTML".equals(selectedInterface)) {
                        HTMLCodeGenerator htmlCodeGenerator = new HTMLCodeGenerator();
                        htmlCodeGenerator.generateCode(tableName, columns, foreignKeys);
                    } else if ("Swing".equals(selectedInterface)) {
                        SwingCodeGenerator swingCodeGnerator = new SwingCodeGenerator();
                        swingCodeGnerator.generateCode(tableName, columns, foreignKeys);
                    }
                    // Handle other code generation options here
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
    
    
}


