package com.fc.shimpyo_be.global.util;

import com.fc.shimpyo_be.domain.room.entity.Room;
import java.time.LocalDate;

public class PricePickerByDateUtil {

    private final static LocalDate today = LocalDate.now();


    public static long getPrice(Room room) {

        boolean isPeakTime = isPeakTime();
        boolean isWeekend = isWeekend();
        if (isPeakTime && isWeekend) {
            return room.getPrice().getPeakWeekendMinFee();
        } else if (isPeakTime && !isWeekend) {
            return room.getPrice().getPeakWeekDaysMinFee();
        } else if (!isPeakTime && isWeekend) {
            return room.getPrice().getOffWeekendMinFee();
        } else {
            return room.getPrice().getOffWeekDaysMinFee();
        }
    }


    public static boolean isWeekend() {
        boolean isWeekend = false;

        isWeekend = switch (today.getDayOfWeek().getValue()) {
            case 6, 7 -> true;
            default -> isWeekend;
        };

        return isWeekend;

    }

    public static boolean isPeakTime() {
        boolean isPeakMonth = true;

        isPeakMonth = switch (today.getMonth()) {
            //성수기
            case MARCH, APRIL, MAY, JUNE, SEPTEMBER -> false;
            default -> isPeakMonth;
        };

        return isPeakMonth;
    }

}
