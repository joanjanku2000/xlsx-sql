package service.generator;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public interface SqlGenerator {
    Integer COLUMN_NAMES_INDEX = 0;
    String TABLE_NAME = "#TABLE_NAME";
    String COLUMN_NAMES = "#COLUMN_NAMES";
    String COMMA = ",";
    String VALUES = "#VALUES";
    String OPENING_PARANTHESIS = "(";
    String CLOSING_PARANTHESIS = ")";
    String EMPTY = "";
    String UPDATE_PAIRS = "#UPDATE_PAIRS";
    String PREDICATES = "#PREDICATES";
    String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;
    String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET " + UPDATE_PAIRS + " WHERE " + PREDICATES;

    String generate(String tableName, Map<Integer, List<String>> rowsMap);

    default void removeFirstRow(Map<Integer, List<String>> rowsMap) {
        rowsMap.remove(0);
    }


    default List<String> extractColumnNames(Map<Integer, List<String>> rowsMap) {
        return requireNonNull(rowsMap.get(COLUMN_NAMES_INDEX));
    }
}
