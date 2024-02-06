package reader;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

public class FileIO {

    static Logger logger = LoggerFactory.getLogger("test");
    private FileIO() {
        // hidden
    }

    /**
     * Returns a Map containing the row index as a key
     * and a List of values in each column for each row
     * with the first row being the column name row
     *
     * @param path File Path
     * @return Map
     * @throws IOException
     */
    public static Map<Integer, List<String>> fromExcel(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            return extractMapFromSheet(sheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Integer, List<String>> fromExcel(InputStream inputStream) {
        logger.info("{}",inputStream);
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            return extractMapFromSheet(sheet);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Map<Integer, List<String>> extractMapFromSheet(Sheet sheet) {
        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
           // if (i == 500) break;
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                if (requireNonNull(cell.getCellType()).equals(CellType.NUMERIC)) {
                    data.get(i).add(valueOf(cell.getNumericCellValue()));
                    continue;
                }
                data.get(i).add(cell.getStringCellValue());
            }
            i++;
        }

        return data;
    }
}
