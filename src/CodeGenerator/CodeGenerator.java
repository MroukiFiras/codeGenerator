package CodeGenerator;
import Models.*;
import java.util.List;

public interface CodeGenerator {
    void generateCode(String className, List<Column> columns);
    String getDataType(String sqlType); 
    void writeToFile(String fileName, String code, String language); 
}
