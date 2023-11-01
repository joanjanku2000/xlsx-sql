import org.apache.commons.io.FileUtils;
import reader.FileIO;
import service.generator.SqlGenerator;
import service.generator.UpdateGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        SqlGenerator sqlGenerator = new UpdateGenerator();
        Map<Integer, List<String>> fromExcel = FileIO.fromExcel("src/main/resources/data.xlsx");
        FileUtils.writeStringToFile(new File("certs.sql"), sqlGenerator.generate("certification", fromExcel).replace(";", ";\n"), Charset.defaultCharset());
//        Controller.runServer();
    }
}
