package service.generator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static service.datatype.DataTypeConverter.adjustValue;
import static service.generator.ColumnsAdapter.simpleListToMap;

public class InsertsGenerator implements SqlGenerator {

    public String generate(String tableName, Map<Integer, List<String>> rowsMap) {
        Map<Integer, String> columnsMap = simpleListToMap(extractColumnNames(rowsMap)); // <columnId,columnName>
        removeFirstRow(rowsMap);
        StringBuilder values = new StringBuilder();
        for (Map.Entry<Integer, List<String>> e : rowsMap.entrySet()) {
            List<String> valuesList = e.getValue();
            String value = OPENING_BRACKET + valuesList.stream().map(val -> getValue(columnsMap.get(valuesList.indexOf(val)), val)).collect(Collectors.joining(COMMA)) + CLOSING_BRACKET;
            values.append(COMMA).append(value);
        }
        return INSERT_STATEMENT.replace(TABLE_NAME, tableName).replace(COLUMN_NAMES, columnsMap.values().stream().map(this::getColumnName).filter(Objects::nonNull).collect(Collectors.joining(COMMA))).replace(VALUES, values.toString().replaceFirst(COMMA, EMPTY));
    }



    private String getColumnName(String columnName) {
        if (!mustBeIgnored(columnName)) {
            return pureColumnName(columnName);
        }
        return null;
    }


}
