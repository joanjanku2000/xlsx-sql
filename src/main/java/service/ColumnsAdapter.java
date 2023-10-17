package service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColumnsAdapter {

    /**
     * Returns the list in MAP format where key = index, value = list value in the specified index
     * @param list
     * @return
     * @param <T>
     */
    public static <T> Map<Integer, T> simpleListToMap(List<T> list) {
        return list.stream().collect(Collectors.toMap(list::indexOf, c -> c));
    }
}
