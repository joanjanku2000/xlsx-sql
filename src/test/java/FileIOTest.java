import org.junit.Assert;
import org.junit.Test;
import reader.FileIO;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileIOTest extends BaseTest {
    static final String XLSX_PATH = "src/test/resources/data.xlsx";
    static final String TEST = "src/test/resources/rowsMap.ser";

    @Test
    public void testRead() throws IOException, ClassNotFoundException {
        Map<Integer, List<String>> rowsMap;
        rowsMap = FileIO.fromExcel(XLSX_PATH);
        FileInputStream fis = new FileInputStream(TEST);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Map<?, List<?>> whatShouldBeMap = (Map<?, List<?>>) ois.readObject();
        ois.close();
        Assert.assertEquals(whatShouldBeMap, rowsMap);
    }
    @Test
    public void exceptionTest() {
        Assert.assertThrows(RuntimeException.class, () -> FileIO.fromExcel(XLSX_PATH + "t"));
    }
}
