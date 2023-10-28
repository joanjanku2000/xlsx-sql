public class BaseTest {
    protected static final char[] RANDOM_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
    protected static final String TABLE_NAME = "#TABLE_NAME";
    protected static final String COLUMN_NAMES = "#COLUMN_NAMES";
    protected static final String COMMA = ",";
    protected static final String VALUES = "#VALUES";
    protected static final String OPENING_PARANTHESIS = "(";
    protected static final String CLOSING_PARANTHESIS = ")";
    protected static final String EMPTY = "";
    protected static final String COLUMNS = "username,email,password";
    protected static final String PREDICATES = "#PREDICATES";
    protected static final String UPDATE_PAIRS = "#UPDATE_PAIRS";
    protected static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;
    protected static final String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET " + UPDATE_PAIRS + " WHERE " + PREDICATES;
    static final String SEMICOLON = ";";
    static final String EQUALS = "=";
    static final String ASTERISK = "*"; // the column which has this will be used in the predicate
}
