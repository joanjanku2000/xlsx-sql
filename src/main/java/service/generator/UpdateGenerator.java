package service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.substringBefore;
import static service.datatype.DataTypeConverter.adjustValue;

public class UpdateGenerator implements SqlGenerator {
    private static final Logger log = LoggerFactory.getLogger(UpdateGenerator.class);


    @Override
    public String generate(String tableName, Map<Integer, List<String>> rowsMap) {
        String updateStatementTemplate = UPDATE_STATEMENT.replace(TABLE_NAME, tableName);
        Map<String, String> updatesPair = new HashMap<>();
        Map<String, String> predicatesPair = new HashMap<>();
        List<UpdateWrap> updatesWrap = new ArrayList<>();
        List<String> columnNames = extractColumnNames(rowsMap);
        removeFirstRow(rowsMap);
        for (Map.Entry<Integer, List<String>> row : rowsMap.entrySet()) {
            List<String> totalValues = row.getValue();
            totalValues
                    .forEach(
                            value -> updatesPair.put (
                                    columnNames.get(totalValues.indexOf(value)).contains(ASTERISK) ? ASTERISK : columnNames.get(totalValues.indexOf(value)),
                                    adjustValue(value)
                            )
                    );
            totalValues.forEach(
                    value -> predicatesPair.put(
                            columnNames.get(totalValues.indexOf(value)).contains(ASTERISK) ? substringBefore(columnNames.get(totalValues.indexOf(value)),ASTERISK) : ASTERISK,
                            adjustValue(value)
                    )
            );
            // clean-up
            predicatesPair.remove(ASTERISK);
            updatesPair.remove(ASTERISK);
            updatesWrap.add(new UpdateWrap(new HashMap<>(updatesPair),new HashMap<>(predicatesPair)));
            updatesPair.clear();
            predicatesPair.clear();
        }
        log.info("Res {}", updatesWrap);
        return updatesWrap.stream().map(uw -> updateStatementTemplate.replace(UPDATE_PAIRS,uw.toUpdateValues()).replace(PREDICATES,uw.toPredicateValues())).collect(Collectors.joining(SEMICOLON)) + SEMICOLON;
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
            return map.entrySet().stream().map(e -> e.getKey() + EQUALZ + e.getValue()).collect(Collectors.joining(joining));
        }
        public UpdateWrap(Map<String, String> updatesPair, Map<String, String> predicatesPair) {
            this.updatesPair = updatesPair;
            this.predicatesPair = predicatesPair;
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


