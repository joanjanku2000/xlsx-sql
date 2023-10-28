package api;

import reader.FileIO;
import service.generator.SqlGenerator;
import service.generator.UpdateGenerator;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        SqlGenerator sqlGenerator = new UpdateGenerator();
        Map<Integer, List<String>> fromExcel = FileIO.fromExcel("src/main/resources/data.xlsx");
        FileIO.toTxt("sqls.txt",sqlGenerator.generate("certs",fromExcel).replace(";", ";\n"));
    }
}
