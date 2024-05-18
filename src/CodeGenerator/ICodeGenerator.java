package CodeGenerator;
import Models.*;
import java.util.List;

public interface ICodeGenerator {
    void generateCode(String className, List<Column> columns, List<ForeignKey> foreignKeys);
    void writeToFile(String fileName, String code, String language); 
}
