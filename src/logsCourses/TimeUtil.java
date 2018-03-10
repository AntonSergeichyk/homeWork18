package logsCourses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtil {

    public static final int MICSEC_IN_MIN = 60000;

    public static Integer convertTimeToMin(String startTime, String endTime){
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            startDate = format.parse(startTime);
            endDate = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differendTime = endDate.getTime() - startDate.getTime();
        Integer min = Math.toIntExact((differendTime / (MICSEC_IN_MIN)));

        return min;
    }
}
