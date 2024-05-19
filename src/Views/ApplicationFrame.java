package Views;

import DAO.TableDAO;
import Models.Table;
import utilitaires.Utilitaire;

import javax.swing.*;
import Controllers.ApplicationController;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFrame extends JFrame {
    private JList<String> tableList;
    private JRadioButton javaRadioButton;
    private JRadioButton pythonRadioButton;
    private JRadioButton swingRadioButton;
    private JRadioButton htmlRadioButton;
    private JButton generateButton;

    public ApplicationFrame(Connection connection) {
        setTitle("CodeGenius");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        TableDAO tableDAO = new TableDAO();
        List<Table> tableNames = tableDAO.getAllTables();
        List<String> tableNamesList = new ArrayList<>();
        for (Table table : tableNames) {
            tableNamesList.add(table.getTableName());
        }

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table selection panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableLabel = new JLabel("Select Table(s):");
        tableList = new JList<>(tableNamesList.toArray(new String[0]));
        tableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(tableList);
        tablePanel.add(tableLabel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        inputPanel.add(tablePanel); 

        // Code selection panel
        JPanel codePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel codeLabel = new JLabel("Select Code:");
        javaRadioButton = new JRadioButton("Java");
        pythonRadioButton = new JRadioButton("Python");
        ButtonGroup codeGroup = new ButtonGroup();
        codeGroup.add(javaRadioButton);
        codeGroup.add(pythonRadioButton);
        codePanel.add(codeLabel);
        codePanel.add(javaRadioButton);
        codePanel.add(pythonRadioButton);
        inputPanel.add(codePanel);

        // Interface selection panel
        JPanel interfacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel interfaceLabel = new JLabel("Select Interface:");
        swingRadioButton = new JRadioButton("Swing");
        htmlRadioButton = new JRadioButton("HTML");
        ButtonGroup interfaceGroup = new ButtonGroup();
        interfaceGroup.add(swingRadioButton);
        interfaceGroup.add(htmlRadioButton);
        interfacePanel.add(interfaceLabel);
        interfacePanel.add(swingRadioButton);
        interfacePanel.add(htmlRadioButton);
        inputPanel.add(interfacePanel);


        // Generate button
        generateButton = new JButton("Generate Code");
        generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.add(generateButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);
    }



    public void addGenerateButtonListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public String[] getSelectedTables() {
        return tableList.getSelectedValuesList().toArray(new String[0]);
    }

    public String getSelectedCode() {
        if (javaRadioButton.isSelected()) {
            return "Java";
        } else if (pythonRadioButton.isSelected()) {
            return "Python";
        } else {
            return null;
        }
    }

    public String getSelectedInterface() {
        if (swingRadioButton.isSelected()) {
            return "Swing";
        } else if (htmlRadioButton.isSelected()) {
            return "HTML";
        } else {
            return null;
        }
    }

    public JButton getGenerateButton() {
        return generateButton;
    }



    public static void main(String[] args) {
        // Load connection properties
        String propertiesFilePath = "C:\\Users\\firas\\Desktop\\atelierProg\\Semestre2\\ProjectJava\\codeGenerator\\connection.properties"; 
        Utilitaire.seConnecter(propertiesFilePath);
        Connection connection = Utilitaire.getConnection();
    
        // Create the application frame
        ApplicationFrame frame = new ApplicationFrame(connection);
        frame.setVisible(true);
    
        // Create the application controller
        ApplicationController controller = new ApplicationController(connection);
    
        // Add the action listener to the generate button
        frame.addGenerateButtonListener(controller);
    }
}
