import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static service.InsertsGenerator.generateInserts;

public class InsertsGeneratorTest {
    private static final Logger log = LoggerFactory.getLogger(InsertsGeneratorTest.class);

    private static final String TABLE_NAME = "#TABLE_NAME";
    private static final String COLUMN_NAMES = "#COLUMN_NAMES";
    private static final String COMMA = ",";
    private static final String VALUES = "#VALUES";
    private static final String OPENING_PARANTHESIS = "(";
    private static final String CLOSING_PARANTHESIS = ")";
    private static final String EMPTY = "";
    private static final String COLUMNS = "username,email,password";
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;


    @Test
    public void testGenerateInserts() {
        String tableName = "users";
        String inserts = INSERT_STATEMENT.replace(TABLE_NAME,tableName).replace(COLUMN_NAMES,COLUMNS);
        String values = "";
        Map<Integer, List<String>> rowsMap = new HashMap<>();
        rowsMap.put(0, Arrays.asList(COLUMNS.split(COMMA)));
        for (int i = 1; i < 10; i++){
            String username = RandomStringUtils.random(5);
            String email = RandomStringUtils.random(8);
            String password = RandomStringUtils.random(10);
            rowsMap.put(i,Arrays.asList(username,email,password));
            values+=COMMA +OPENING_PARANTHESIS + username + COMMA + email + COMMA + password + CLOSING_PARANTHESIS;
        }
        values = values.replaceFirst(COMMA,EMPTY);
        inserts = inserts.replace(VALUES,values);

        String generatedSql = generateInserts(tableName,rowsMap);
        log.info("Expected {}",inserts);
        log.info("Actual {}",generatedSql);
        assertEquals(inserts,generatedSql);
    }
}
