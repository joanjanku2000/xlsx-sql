package service;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static service.DataType.*;

public class DataTypeConverter {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String SINGLE_QUOTE = "'";

    public static String adjustValue(String value) {
        return switch (getDataType(value)) {
            case STRING -> StringUtils.wrap(value, SINGLE_QUOTE);
            case BOOLEAN, NUMBER -> value;
        };
    }

    private static DataType getDataType(String value) {
        return isNumeric(value) ? NUMBER : isBoolean(value) ? BOOLEAN : STRING;
    }

    private static boolean isBoolean(String value) {
        return value.compareToIgnoreCase(TRUE) == 0 || value.compareToIgnoreCase(FALSE) == 0;
    }
}
