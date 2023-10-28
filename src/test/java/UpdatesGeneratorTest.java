import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.generator.SqlGenerator;
import service.generator.UpdateGenerator;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class UpdatesGeneratorTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UpdatesGeneratorTest.class);
    protected static final String COLUMNS_UPDATE = "username*,email,password,age";
    @Test
    public void testGenerateUpdates() {
        SqlGenerator updateGenerator = new UpdateGenerator();
        String tableName = "users";
        String values = "";
        Map<Integer, List<String>> rowsMap = new HashMap<>();
        rowsMap.put(0, Arrays.asList(COLUMNS_UPDATE.split(COMMA)));
        for (int i = 1; i < 20; i++) {
            String username = RandomStringUtils.random(5,RANDOM_CHARS);
            String email = RandomStringUtils.random(8,RANDOM_CHARS);
            String password = RandomStringUtils.random(10,RANDOM_CHARS);
            Integer age = new Random().nextInt() ;
            rowsMap.put(i,Arrays.asList(username,email,password,String.valueOf(age)));
            values+=UPDATE_STATEMENT.replace(TABLE_NAME,tableName).replace(UPDATE_PAIRS, "password='"+password+"'," + "email='" + email + "',"+"age="+age).replace(PREDICATES,"username='"+username + "';");
        }
        log.info("Rows map {}", rowsMap);
     //   log.info("Final SQLs {}",updateGenerator.generate(tableName,rowsMap));
        Assert.assertEquals(values,updateGenerator.generate(tableName,rowsMap));
    }
}