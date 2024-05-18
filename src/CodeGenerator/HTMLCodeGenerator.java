package CodeGenerator;

import Models.Column;
import Models.ForeignKey;
import utilitaires.Utilitaire;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HTMLCodeGenerator implements ICodeGenerator {

    @Override
    public void generateCode(String tableName, List<Column> columns, List<ForeignKey> foreignKeys) {
        StringBuilder code = new StringBuilder();

        // Generate HTML form
        code.append("<form action=\"submit_form.php\" method=\"post\">\n");
        code.append("<h2>").append(tableName).append(" Form</h2>\n");

        // Add form fields
        for (Column column : columns) {
            if (isForeignKey(column, foreignKeys)) {
                code.append("    <label for=\"").append(column.getColumnName()).append("\">").append(column.getColumnName()).append(":</label><br>\n");
                code.append("    <select id=\"").append(column.getColumnName()).append("\" name=\"").append(column.getColumnName()).append("\">\n");

                // Fetch options from the database
                List<String> options = fetchOptionsFromDatabase(column, foreignKeys);
                for (String option : options) {
                    code.append("        <option value=\"").append(option).append("\">").append(option).append("</option>\n");
                }
                code.append("    </select><br>\n");
            } else {
                code.append("    <label for=\"").append(column.getColumnName()).append("\">").append(column.getColumnName()).append(":</label><br>\n");
                code.append("    <input type=\"text\" id=\"").append(column.getColumnName()).append("\" name=\"").append(column.getColumnName()).append("\"><br>\n");
            }
        }

        // Add submit button
        code.append("    <input type=\"submit\" value=\"Submit\">\n");
        code.append("</form>\n");

        // Write code to file
        writeToFile(tableName + "Form.html", code.toString(), "HTML");
    }

    private boolean isForeignKey(Column column, List<ForeignKey> foreignKeys) {
        for (ForeignKey foreignKey : foreignKeys) {
            if (foreignKey.getColumnName().equals(column.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    private List<String> fetchOptionsFromDatabase(Column column, List<ForeignKey> foreignKeys) {
        List<String> options = new ArrayList<>();
        Connection connection = null;

        try {
            // Establish database connection using Utilitaire
            String propertiesFilePath = "C:\\Users\\firas\\Desktop\\atelierProg\\Semestre2\\ProjectJava\\codeGenerator\\connection.properties";
            Utilitaire.seConnecter(propertiesFilePath);
            connection = Utilitaire.getConnection();

            // Find the referenced column name and table name for the given column
            ForeignKey foreignKey = getForeignKey(column, foreignKeys);
            if (foreignKey != null) {
                String referencedColumnName = foreignKey.getReferencedColumnName();
                String referencedTableName = foreignKey.getReferencedTableName();
                String query = "SELECT " + referencedColumnName + " FROM " + referencedTableName;
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        options.add(resultSet.getString(referencedColumnName));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return options;
    }

    private ForeignKey getForeignKey(Column column, List<ForeignKey> foreignKeys) {
        for (ForeignKey foreignKey : foreignKeys) {
            if (foreignKey.getColumnName().equals(column.getColumnName())) {
                return foreignKey;
            }
        }
        return null;
    }

    @Override
    public void writeToFile(String fileName, String code, String language) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(code);
            System.out.println(language + " form generated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing " + language + " form to file: " + e.getMessage());
        }
    }
}
