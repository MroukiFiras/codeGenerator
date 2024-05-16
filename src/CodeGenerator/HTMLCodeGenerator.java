package CodeGenerator;

import Models.Column;
import Models.ForeignKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HTMLCodeGenerator implements CodeGenerator {

    @Override
    public void generateCode(String tableName, List<Column> columns, List<ForeignKey> foreignKeys) {
        StringBuilder form = new StringBuilder();
        form.append("<form action=\"submit_form.php\" method=\"post\">\n");
        form.append("<h2>").append(tableName).append(" Form</h2>\n");

        // Generate form fields
        for (Column column : columns) {
            String columnName = column.getColumnName();
            String capitalizedColumnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            form.append("    <label for=\"").append(columnName).append("\">").append(capitalizedColumnName).append(":</label><br>\n");
            form.append("    <input type=\"text\" id=\"").append(columnName).append("\" name=\"").append(columnName).append("\"><br>\n");
        }

        form.append("    <input type=\"submit\" value=\"Submit\">\n");
        form.append("</form>");

        // Write form to file
        writeToFile(tableName + "Form.html", form.toString(), "HTML");
    }

    @Override
    public String getDataType(String sqlType) {
        // HTML form generator does not require data type mapping
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
