package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static service.ColumnsAdapter.simpleListToMap;

public class InsertsGenerator {

    private static final Integer COLUMN_NAMES_INDEX = 0;
    private static final String TABLE_NAME = "#TABLE_NAME";
    private static final String COLUMN_NAMES = "#COLUMN_NAMES";
    private static final String COMMA = ",";
    private static final String VALUES = "#VALUES";
    private static final String OPENING_PARANTHESIS = "(";
    private static final String CLOSING_PARANTHESIS = ")";
    private static final String EMPTY = "";
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;

    public static String generateInserts(String tableName, Map<Integer, List<String>> rowsMap) {
        List<String> generatedInserts = new ArrayList<>();
        Map<Integer, String> columnsMap = simpleListToMap(extractColumnNames(rowsMap)); // <columnId,columnName>
        String columnValues = columnsMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.joining(COMMA));
        removeFirstRow(rowsMap);
        StringBuilder values = new StringBuilder();
        for (Map.Entry<Integer, List<String>> e : rowsMap.entrySet()) {
            String value = OPENING_PARANTHESIS + e.getValue().stream().collect(Collectors.joining(COMMA)) + CLOSING_PARANTHESIS;
            values.append(COMMA).append(value);
        }
        return INSERT_STATEMENT.replace(TABLE_NAME, tableName).replace(COLUMN_NAMES, columnValues).replace(VALUES, values.toString().replaceFirst(COMMA, EMPTY));
    }

    private static void removeFirstRow(Map<Integer, List<String>> rowsMap) {
        rowsMap.remove(0);
    }


    private static List<String> extractColumnNames(Map<Integer, List<String>> rowsMap) {
        return requireNonNull(rowsMap.get(COLUMN_NAMES_INDEX));
    }


}
