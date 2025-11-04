package com.accessManagementSystem.utilities;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j
public class DateUtil {

    public static boolean isExpired(LocalDate validityTo) {
        if (validityTo == null) return false;
        boolean expired = validityTo.isBefore(LocalDate.now());
        if (expired) log.warn("📅 Validity expired on {}", validityTo);
        return expired;
    }
}
