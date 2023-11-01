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
            totalValues.forEach(value -> {
                String columnName = columnNames.get(totalValues.indexOf(value));
                if (!mustBeIgnored(columnName))
                    updatesPair.put(columnName.contains(ASTERISK) ? ASTERISK : pureColumnName(columnName), getValue(columnName, value));
            });
            totalValues.forEach(value -> {
                String columnName = columnNames.get(totalValues.indexOf(value));
                if (!mustBeIgnored(columnName))
                    predicatesPair.put(columnName.contains(ASTERISK) && !mustBeIgnored(columnName) ? pureColumnName(substringBefore(columnName, ASTERISK)) : ASTERISK, foreignKey(columnName) ? wrapInBrackets(selectStatement(columnName, adjustValue(value))) : adjustValue(adjust(needsAdapting(substringBefore(columnName, ASTERISK)) ? adaptValue(substringBefore(columnName, ASTERISK), extractColumnTranslation(substringBefore(columnName, ASTERISK))) : value)));
            });
            // clean-up
            predicatesPair.remove(ASTERISK);
            predicatesPair.remove(EMPTY);
            updatesPair.remove(EMPTY);
            updatesPair.remove(ASTERISK);
            updatesWrap.add(new UpdateWrap(new HashMap<>(updatesPair), new HashMap<>(predicatesPair)));
            updatesPair.clear();
            predicatesPair.clear();
        }
        return updatesWrap.stream().filter(uw -> uw.updatesPair.size() > 0).map(uw -> updateStatementTemplate.replace(UPDATE_PAIRS, uw.toUpdateValues()).replace(PREDICATES, uw.toPredicateValues())).collect(Collectors.joining(SEMICOLON)) + SEMICOLON;
    }


    static class UpdateWrap {
        private final Map<String, String> updatesPair;
        private final Map<String, String> predicatesPair;

        public UpdateWrap(Map<String, String> updatesPair, Map<String, String> predicatesPair) {
            this.updatesPair = updatesPair;
            this.predicatesPair = predicatesPair;
        }

        private static String collect(Map<String, String> map, String joining) {
            return map.entrySet().stream().filter(e -> e.getKey() != null && !e.getKey().isEmpty()).map(e -> e.getKey() + EQUALZ + e.getValue()).collect(Collectors.joining(joining));
        }

        public String toUpdateValues() {
            return collect(updatesPair, COMMA);
        }

        public String toPredicateValues() {
            return collect(predicatesPair, AND);
        }
    }
}


