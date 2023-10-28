package service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static service.ColumnsAdapter.simpleListToMap;

public class InsertsGenerator implements SqlGenerator {

    public String generate(String tableName, Map<Integer, List<String>> rowsMap) {
        Map<Integer, String> columnsMap = simpleListToMap(extractColumnNames(rowsMap)); // <columnId,columnName>
        String columnValues = String.join(COMMA, columnsMap.values());
        removeFirstRow(rowsMap);
        StringBuilder values = new StringBuilder();
        for (Map.Entry<Integer, List<String>> e : rowsMap.entrySet()) {
            String value = OPENING_PARANTHESIS + e.getValue().stream().map(DataTypeConverter::adjustValue).collect(Collectors.joining(COMMA)) + CLOSING_PARANTHESIS;
            values.append(COMMA).append(value);
        }
        return INSERT_STATEMENT.replace(TABLE_NAME, tableName).replace(COLUMN_NAMES, columnValues).replace(VALUES, values.toString().replaceFirst(COMMA, EMPTY));
    }


}
