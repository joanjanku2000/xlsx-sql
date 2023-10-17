package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static service.ColumnsAdapter.simpleListToMap;

public class InsertsGenerator {

    private static final Integer COLUMN_NAMES_INDEX = 0;

    public static List<String> generateInserts(Map<Integer, List<String>> rowsMap) {
        List<String> generatedInserts = new ArrayList<>();
        Map<Integer, String> columnsMap = simpleListToMap(extractColumnNames(rowsMap)); // <columnId,columnName>
        Map<Integer, List<String>> valuesMap = extractValues(rowsMap);
        return generatedInserts;
    }


    private static List<String> extractColumnNames(Map<Integer, List<String>> rowsMap) {
        return requireNonNull(rowsMap.get(COLUMN_NAMES_INDEX));
    }

    private static Map<Integer, List<String>> extractValues(Map<Integer, List<String>> rowsMap) {
        Map<Integer, List<String>> extractedValues = new HashMap<>(); // <columnId,values>

        for (Map.Entry<Integer, List<String>> mapEntry : rowsMap.entrySet()) {
            List<String> vals = mapEntry.getValue();
            for (String value : mapEntry.getValue()) {
                int i = vals.indexOf(value);
                extractedValues.computeIfAbsent(i, k -> new ArrayList<>());
                extractedValues.get(i).add(value);
            }
        }
        return extractedValues;
    }

}
