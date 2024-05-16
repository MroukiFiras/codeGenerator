package CodeGenerator;

import Models.Column;
import Models.ForeignKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PythonCodeGenerator implements CodeGenerator {

    @Override
    public void generateCode(String tableName, List<Column> columns, List<ForeignKey> foreignKeys) {
        StringBuilder code = new StringBuilder();

        // Generate class definition
        code.append("class ").append(tableName).append(":\n\n");

        // Generate __init__ method
        code.append("    def __init__(self");
        for (Column column : columns) {
            code.append(", ").append(column.getColumnName());
        }
        code.append("):\n");
        for (Column column : columns) {
            code.append("        self.").append(column.getColumnName()).append(" = ").append(column.getColumnName()).append("\n");
        }
        code.append("\n");

        // Handle foreign keys
        for (ForeignKey foreignKey : foreignKeys) {
            code.append("        # Foreign Key: ").append(foreignKey.getColumnName()).append("\n");
            code.append("        self.").append(foreignKey.getColumnName()).append(" = ").append(foreignKey.getReferencedTableName()).append("()\n");
        }

        // Generate getter and setter methods
        for (Column column : columns) {
            String columnName = column.getColumnName();
            String capitalizedColumnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            code.append("    def get_").append(columnName).append("(self):\n");
            code.append("        return self.").append(columnName).append("\n\n");

            code.append("    def set_").append(columnName).append("(self, ").append(columnName).append("):\n");
            code.append("        self.").append(columnName).append(" = ").append(columnName).append("\n\n");
        }

        // Write code to file
        writeToFile(tableName + ".py", code.toString(), "Python");
    }

    @Override
    public String getDataType(String sqlType) {
        // Implement this method based on your requirements
        return null;
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
