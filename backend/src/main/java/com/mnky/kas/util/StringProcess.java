package com.mnky.kas.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringProcess {
    public static List<String> splitStringByDelimiter(String input, String delimiter) {
        if (input == null || delimiter == null || delimiter.isEmpty()) {
            return null;
        }
        return Arrays.asList(input.split(delimiter));
    }

}
