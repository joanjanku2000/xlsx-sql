package service;

import java.util.List;
import java.util.Map;

public class UpdateGenerator implements SqlGenerator {
    private static final String ASTERISK = "*"; // the column which has this will be used in the predicate

    @Override
    public String generate(String tableName, Map<Integer, List<String>> rowsMap) {
        List<String> columnNames = extractColumnNames(rowsMap);
        return null;
    }
}
