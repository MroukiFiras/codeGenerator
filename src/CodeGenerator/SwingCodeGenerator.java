package CodeGenerator;

import Models.Column;
import Models.ForeignKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SwingCodeGenerator implements ICodeGenerator {

    @Override
    public void generateCode(String tableName, List<Column> columns, List<ForeignKey> foreignKeys) {
        StringBuilder code = new StringBuilder();

        // Add import statements
        code.append("import javax.swing.*;\n");
        code.append("import java.awt.*;\n");
        code.append("import java.sql.*;\n");
        code.append("import java.util.ArrayList;\n\n");

        // Generate class definition
        code.append("public class ").append(tableName).append("Form extends JFrame {\n\n");

        // Add class fields
        for (Column column : columns) {
            if (isForeignKey(column, foreignKeys)) {
                code.append("    private JComboBox<String> ").append(column.getColumnName()).append("ComboBox;\n");
            } else {
                code.append("    private JTextField ").append(column.getColumnName()).append("Field;\n");
            }
        }
        code.append("\n");

        // Generate constructor
        code.append("    public ").append(tableName).append("Form() {\n");
        code.append("        setTitle(\"").append(tableName).append(" Form\");\n");
        code.append("        setSize(400, 300);\n");
        code.append("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        code.append("        setLayout(new GridLayout(").append(columns.size() + 1).append(", 2));\n\n");

        // Add form components
        for (Column column : columns) {
            code.append("        add(new JLabel(\"").append(column.getColumnName()).append("\"));\n");
            if (isForeignKey(column, foreignKeys)) {
                code.append("        ").append(column.getColumnName()).append("ComboBox = new JComboBox<>();\n");
                code.append("        add(").append(column.getColumnName()).append("ComboBox);\n");
                code.append("        load").append(column.getColumnName()).append("ComboBox();\n");
            } else {
                code.append("        ").append(column.getColumnName()).append("Field = new JTextField(20);\n");
                code.append("        add(").append(column.getColumnName()).append("Field);\n");
            }
            code.append("\n");
        }

        // Add submit button
        code.append("        JButton submitButton = new JButton(\"Submit\");\n");
        code.append("        add(submitButton);\n\n");

        code.append("    }\n\n");

        // Generate methods to load JComboBox data
        for (ForeignKey foreignKey : foreignKeys) {
            code.append("    private void load").append(foreignKey.getColumnName()).append("ComboBox() {\n");
            code.append("        try {\n");
            code.append("            String propertiesFilePath = \"C:\\\\Users\\\\firas\\\\Desktop\\\\atelierProg\\\\Semestre2\\\\ProjectJava\\\\codeGenerator\\\\connection.properties\";\n");
            code.append("            Utilitaire.seConnecter(propertiesFilePath);\n");
            code.append("            Connection connection = Utilitaire.getConnection();\n");
            code.append("            Statement statement = connection.createStatement();\n");
            code.append("            ResultSet resultSet = statement.executeQuery(\"SELECT ").append(foreignKey.getReferencedColumnName()).append(" FROM ").append(foreignKey.getReferencedTableName()).append("\");\n");
            code.append("            while (resultSet.next()) {\n");
            code.append("                ").append(foreignKey.getColumnName()).append("ComboBox.addItem(resultSet.getString(\"").append(foreignKey.getReferencedColumnName()).append("\"));\n");
            code.append("            }\n");
            code.append("        } catch (SQLException e) {\n");
            code.append("            e.printStackTrace();\n");
            code.append("        }\n");
            code.append("    }\n\n");
        }

        // Generate main method
        code.append("    public static void main(String[] args) {\n");
        code.append("        SwingUtilities.invokeLater(() -> {\n");
        code.append("            ").append(tableName).append("Form form = new ").append(tableName).append("Form();\n");
        code.append("            form.setVisible(true);\n");
        code.append("        });\n");
        code.append("    }\n");

        // Close class definition
        code.append("}\n");

        // Write code to file
        writeToFile(tableName + "Form.java", code.toString(), "Java");
    }

    private boolean isForeignKey(Column column, List<ForeignKey> foreignKeys) {
        for (ForeignKey foreignKey : foreignKeys) {
            if (foreignKey.getColumnName().equals(column.getColumnName())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void writeToFile(String fileName, String code, String language) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(code);
            System.out.println(language + " class generated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing " + language + " class to file: " + e.getMessage());
        }
    }
    
}
