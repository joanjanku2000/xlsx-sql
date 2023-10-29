package service.datatype;

import org.apache.commons.lang3.StringUtils;

import static service.datatype.DataType.*;

public class DataTypeConverter {
    private DataTypeConverter(){
        // hidden
    }

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String SINGLE_QUOTE = "'";

    public static String adjustValue(String value) {
        return value == null || value.isEmpty() ? "NULL" : switch (getDataType(value)) {
            case STRING -> StringUtils.wrap(value, SINGLE_QUOTE);
            case BOOLEAN, NUMBER -> value;
        };
    }

    private static DataType getDataType(String value) {
        return isNumeric(value) ? NUMBER : isBoolean(value) ? BOOLEAN : STRING;
    }

    private static boolean isBoolean(String value) {
        return value != null ? value.compareToIgnoreCase(TRUE) == 0 || value.compareToIgnoreCase(FALSE) == 0 : false;
    }

    private static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
