import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.generator.InsertsGenerator;
import service.generator.SqlGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static service.datatype.DataTypeConverter.adjustValue;


public class InsertsGeneratorTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(InsertsGeneratorTest.class);
    @Test
    public void testGenerateInserts() {
        String tableName = "users";
        String inserts = INSERT_STATEMENT.replace(TABLE_NAME,tableName).replace(COLUMN_NAMES,COLUMNS);
        StringBuilder values = new StringBuilder();
        Map<Integer, List<String>> rowsMap = new HashMap<>();
        rowsMap.put(0, Arrays.asList(COLUMNS.split(COMMA)));
        for (int i = 1; i < 10; i++){
            String username = RandomStringUtils.random(5,RANDOM_CHARS);
            String email = RandomStringUtils.random(8,RANDOM_CHARS);
            String password = RandomStringUtils.random(10,RANDOM_CHARS);
            rowsMap.put(i,Arrays.asList(username,email,password));
            values.append(COMMA + OPENING_PARANTHESIS).append(adjustValue(username)).append(COMMA).append(adjustValue(email)).append(COMMA).append(adjustValue(password)).append(CLOSING_PARANTHESIS);
        }
        values = new StringBuilder(values.toString().replaceFirst(COMMA, EMPTY));
        inserts = inserts.replace(VALUES, values.toString());

        SqlGenerator sqlGenerator = new InsertsGenerator();
        String generatedSql = sqlGenerator.generate(tableName,rowsMap);
        log.info("Expected {}",inserts);
        log.info("Actual {}",generatedSql);
        assertEquals(inserts,generatedSql);
    }
}
