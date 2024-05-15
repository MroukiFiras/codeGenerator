package CodeGenerator;

import Models.Column;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JavaCodeGenerator implements CodeGenerator {

    @Override
    public void generateCode(String className, List<Column> columns) {
        StringBuilder code = new StringBuilder();
        code.append("public class ").append(className).append(" {\n");

        // Generate attributes
        for (Column column : columns) {
            code.append("    private ").append(getDataType(column.getColumnType())).append(" ").append(column.getColumnName()).append(";\n");
        }

        // Generate parameterized constructor
        code.append("\n    public ").append(className).append("(");
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            code.append(getDataType(column.getColumnType())).append(" ").append(column.getColumnName());
            if (i < columns.size() - 1) {
                code.append(", ");
            }
        }
        code.append(") {\n");
        for (Column column : columns) {
            code.append("        this.").append(column.getColumnName()).append(" = ").append(column.getColumnName()).append(";\n");
        }
        code.append("    }\n");

        // Generate getters and setters
        for (Column column : columns) {
            String javaDataType = getDataType(column.getColumnType());
            String columnName = column.getColumnName();
            String capitalizedColumnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

            code.append("\n    public ").append(javaDataType).append(" get").append(capitalizedColumnName).append("() {\n");
            code.append("        return ").append(columnName).append(";\n");
            code.append("    }\n\n");

            code.append("    public void set").append(capitalizedColumnName).append("(").append(javaDataType).append(" ").append(columnName).append(") {\n");
            code.append("        this.").append(columnName).append(" = ").append(columnName).append(";\n");
            code.append("    }\n");
        }

        code.append("}");

        // Write code to file
        writeToFile(className + ".java", code.toString(), "Java");
    }

    @Override
    public String getDataType(String sqlType) {
        switch (sqlType.toLowerCase()) {
            case "varchar":
            case "char":
            case "text":
                return "String";
            case "int":
            case "integer":
            case "smallint":
            case "tinyint":
                return "int";
            case "bigint":
                return "long";
            case "boolean":
            case "bit":
                return "boolean";
            case "float":
                return "float";
            case "double":
            case "real":
                return "double";
            case "date":
            case "time":
            case "timestamp":
            case "datetime":
                return "java.util.Date";
            default:
                return "String"; // Default to String for unknown types
        }
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
