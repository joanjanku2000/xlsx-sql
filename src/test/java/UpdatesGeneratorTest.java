import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.InsertsGenerator;
import service.SqlGenerator;
import service.UpdateGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class UpdatesGeneratorTest {
    private static final Logger log = LoggerFactory.getLogger(InsertsGeneratorTest.class);
    private static final char[] RANDOM_CHARS = {'a','b','c','d','e','f','g','h','i'};
    private static final String TABLE_NAME = "#TABLE_NAME";
    private static final String COLUMN_NAMES = "#COLUMN_NAMES";
    private static final String COMMA = ",";
    private static final String VALUES = "#VALUES";
    private static final String OPENING_PARANTHESIS = "(";
    private static final String CLOSING_PARANTHESIS = ")";
    private static final String EMPTY = "";
    private static final String COLUMNS = "username*,email,password";
    private static final String UPDATE_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_NAMES + ") VALUES " + VALUES;


    @Test
    public void testGenerateInserts() {
        SqlGenerator updateGenerator = new UpdateGenerator();
        String tableName = "users";
        String values = "";
        Map<Integer, List<String>> rowsMap = new HashMap<>();
        rowsMap.put(0, Arrays.asList(COLUMNS.split(COMMA)));
        for (int i = 1; i < 10; i++){
            String username = RandomStringUtils.random(5,RANDOM_CHARS);
            String email = RandomStringUtils.random(8,RANDOM_CHARS);
            String password = RandomStringUtils.random(10,RANDOM_CHARS);
            rowsMap.put(i,Arrays.asList(username,email,password));
        }
        log.info("Rows map {}", rowsMap);
        log.info("Final SQLs {}",updateGenerator.generate(tableName,rowsMap));
    }
}
