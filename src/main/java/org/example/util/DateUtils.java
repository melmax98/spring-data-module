package org.example.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    private static final DateUtils dateUtils = new DateUtils();

    public static synchronized DateUtils getInstance() {
        return dateUtils;
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");

}