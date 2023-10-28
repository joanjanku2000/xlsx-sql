package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.substringBefore;

public class UpdateGenerator implements SqlGenerator {
    private static final Logger log = LoggerFactory.getLogger(SqlGenerator.class);
    private static final String AND = " and ";
    private static final String SEMICOLON = ";";
    private static final String EQUALS = "=";
    private static final String ASTERISK = "*"; // the column which has this will be used in the predicate

    @Override
    public String generate(String tableName, Map<Integer, List<String>> rowsMap) {
        String updateStatementTemplate = UPDATE_STATEMENT.replace(TABLE_NAME, tableName);
        List<String> updates = new ArrayList<>();
        Map<String, String> updatesPair = new HashMap<>();
        Map<String, String> predicatesPair = new HashMap<>();
        List<UpdateWrap> updatesWraps = new ArrayList<>();
        List<String> columnNames = extractColumnNames(rowsMap);
        removeFirstRow(rowsMap);
        for (Map.Entry<Integer, List<String>> row : rowsMap.entrySet()) {
            List<String> totalValues = row.getValue();
            totalValues
                    .forEach(
                            value -> updatesPair.put (
                                    columnNames.get(totalValues.indexOf(value)).contains(ASTERISK) ? ASTERISK : columnNames.get(totalValues.indexOf(value)),
                                    value
                            )
                    );
            totalValues.forEach(
                    value -> predicatesPair.put(
                            columnNames.get(totalValues.indexOf(value)).contains(ASTERISK) ? substringBefore(columnNames.get(totalValues.indexOf(value)),ASTERISK) : ASTERISK,
                            value
                    )
            );
            predicatesPair.remove(ASTERISK);
            updatesPair.remove(ASTERISK);
            updatesWraps.add(new UpdateWrap(new HashMap<>(updatesPair),new HashMap<>(predicatesPair)));
            updatesPair.clear();
            predicatesPair.clear();
        }

        log.info("Res {}",updatesWraps);
        List<String> finalReturn = updatesWraps.stream().map(uw -> updateStatementTemplate.replace(UPDATE_PAIRS,uw.toUpdateValues()).replace(PREDICATES,uw.toPredicateValues())).collect(Collectors.toList());
        log.info("Final statements {}", String.join("\n", finalReturn));
        return updatesWraps.stream().map(uw -> updateStatementTemplate.replace(UPDATE_PAIRS,uw.toUpdateValues()).replace(PREDICATES,uw.toPredicateValues())).collect(Collectors.joining(SEMICOLON));
    }


    static class UpdateWrap {
        private final Map<String,String> updatesPair;
        private final Map<String,String> predicatesPair;

        public String toUpdateValues(){
            return collect(updatesPair, COMMA);
        }
        public String toPredicateValues(){
            return collect(predicatesPair, AND);
        }
        private static String collect(Map<String,String> map, String joining) {
            return map.entrySet().stream().map(e -> e.getValue() + UpdateGenerator.EQUALS + e.getKey()).collect(Collectors.joining(joining));
        }
        public UpdateWrap(Map<String, String> updatesPair, Map<String, String> predicatesPair) {
            this.updatesPair = updatesPair;
            this.predicatesPair = predicatesPair;
        }

        public Map<String, String> getUpdatesPair() {
            return updatesPair;
        }

        public Map<String, String> getPredicatesPair() {
            return predicatesPair;
        }

        @Override
        public String toString() {
            return "UpdateWrap{" +
                    "updatesPair=" + updatesPair +
                    ", predicatesPair=" + predicatesPair +
                    '}';
        }
    }
}


