package service.generator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static service.datatype.DataTypeConverter.adjustValue;

public interface SqlGenerator {
    Integer COLUMN_NAMES_INDEX = 0;
    String TABLE_NAME = "#TABLE_NAME";
    String COLUMN_NAMES = "#COLUMN_NAMES";
    String COMMA = ",";
    String VALUES = "#VALUES";
    String OPENING_BRACKET = "(";
    String CLOSING_BRACKET = ")";
    String EMPTY = "";
    String UPDATE_PAIRS = "#UPDATE_PAIRS";
    String PREDICATES = "#PREDICATES";
    String WHERE = " WHERE ";
    String FOREIGN_KEY_INDICATOR = "<=>";
    String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;
    String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET " + UPDATE_PAIRS + WHERE + PREDICATES;
    String SELECT_STATEMENT = "SELECT distinct " + COLUMN_NAMES + " FROM " + TABLE_NAME + WHERE + PREDICATES;
    String AND = " and ";
    String SEMICOLON = ";";
    String EQUALZ = "=";
    String ASTERISK = "*"; // the column which has this will be used in the predicate
    String GREATER_THAN = ">";
    String QUESTION_MARK = "?";
    String AMPERSAND = "&";
    String MINUS = "-";
    String PIPE = "|";

    String generate(String tableName, Map<Integer, List<String>> rowsMap);

    default void removeFirstRow(Map<Integer, List<String>> rowsMap) {
        rowsMap.remove(0);
    }

    default List<String> extractColumnNames(Map<Integer, List<String>> rowsMap) {
        return requireNonNull(rowsMap.get(COLUMN_NAMES_INDEX));
    }

    default Map<String, String> extractColumnTranslation(String adaptedColumnName) {
        Map<String, String> toReturn = new HashMap<>();
        String adaptions = substringAfter(adaptedColumnName, GREATER_THAN);
        String[] parts = adaptions.split(AMPERSAND);
        stream(parts).forEach(s -> toReturn.put(s.split(EQUALZ)[0], s.split(EQUALZ)[1]));
        return toReturn;
    }

    default String pureColumnName(String columnName) {
        return needsAdapting(columnName) ? foreignKey(columnName) ? substringBefore(columnName,FOREIGN_KEY_INDICATOR) : substringBefore(columnName, GREATER_THAN) : columnName;
    }
    default String removeNewLines(String value){
        return StringUtils.remove(value,"\n");
    }
    default String adjustApostrophes(String value){
        return StringUtils.replace(value,"'","");
    }
    default String adjust(String value){
        return adjustApostrophes(removeNewLines(value));
    }

    default boolean mustBeIgnored(String columnName) {
        return columnName.contains(OPENING_BRACKET) && columnName.contains(CLOSING_BRACKET);
    }

    default boolean foreignKey(String columnName) {
        return columnName.contains(FOREIGN_KEY_INDICATOR);
    }
    default String selectStatement(String columnName,String value){
        String targetColumnProjection = substringBefore(substringAfter(columnName,FOREIGN_KEY_INDICATOR),MINUS);
        String targetColumnPredicate = substringBefore(substringAfter(columnName,MINUS),PIPE);
        String targetTableName = substringAfter(columnName,PIPE);
        return SELECT_STATEMENT.replace(COLUMN_NAMES,targetColumnProjection).replace(TABLE_NAME,targetTableName).replace(PREDICATES,
                 targetColumnPredicate +  EQUALZ + value);
    }

    default boolean needsAdapting(String columnName) {
        return columnName.contains(GREATER_THAN) ;
    }
    default String adaptValue(String value,Map<String ,String> adaptions) {
        for (Map.Entry<String,String> pair : adaptions.entrySet()) {
            if (pair.getKey().compareToIgnoreCase(value) == 0) {
                return pair.getValue();
            }
        }
        return adaptions.isEmpty() ? value : adaptions.get(QUESTION_MARK);
    }
    default String wrapInBrackets(String str){
        return OPENING_BRACKET + str + CLOSING_BRACKET;
    }
    default  String getValue(String columnName, String value) {
        if (!mustBeIgnored(columnName)) {
            return foreignKey(columnName) ? wrapInBrackets(selectStatement(columnName, adjustValue(value))) : adjustValue(adjust(needsAdapting(columnName) ? adaptValue(value, extractColumnTranslation(columnName)) : value));
        }
        return null;
    }
}
