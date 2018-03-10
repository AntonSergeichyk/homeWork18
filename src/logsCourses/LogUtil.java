package logsCourses;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class LogUtil {

    public static final String NEXT_DAY = "";

    public static Deque<String> getTimeIntervalLogs(List<String> logs) {
        String regex = "(?<startTime>\\d{2}:\\d{2})\\s(?<employment>\\W*)(?<endTime>\\d{2}:\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Deque<String> timeIntervalLogs = new LinkedList<>();
        String logsString = logs.stream()
                .collect(Collectors.joining());
        Matcher matcher = pattern.matcher(logsString);
        String format = "%s-%s %s";

        int start = 0;
        while (matcher.find(start)) {
            String startTime = matcher.group("startTime");
            String employment = matcher.group("employment");
            String endTime = matcher.group("endTime");
            String formattedNumber = String.format(format, startTime, endTime, employment);
            if (employment.equals("Конец")) {
                timeIntervalLogs.addLast("");
            } else {
                timeIntervalLogs.addLast(formattedNumber);
            }
            start = matcher.start(3);

        }

        return timeIntervalLogs;
    }

    public static Deque<String> getReportCommonTime(List<String> logs) {
        String REGEX = "(?<startTime>\\d{2}:\\d{2})-(?<endTime>\\d{2}:\\d{2})\\s(?<employment>\\W*)";
        Pattern PATTERN = Pattern.compile(REGEX);
        Map<String, Integer> logsTotalTime = new LinkedHashMap<>();
        Map<String, Integer> detailedLogs = new LinkedHashMap<>();
        Deque<String> resultLog = new LinkedList<>();

        for (String value : logs) {
            if (!value.equals(NEXT_DAY)) {
                Matcher matcher = PATTERN.matcher(value);
                while (matcher.find()) {
                    String startTime = matcher.group("startTime");
                    String endTime = matcher.group("endTime");
                    String employment = matcher.group("employment");
                    Integer differentTime = TimeUtil.convertTimeToMin(startTime, endTime);
                    if (isValid(employment)) {
                        logsTotalTime.merge(employment, differentTime, (a, b) -> a + b);
                    } else {
                        logsTotalTime.merge("Лекции", differentTime, (a, b) -> a + b);
                        detailedLogs.put(employment, differentTime);
                    }
                }
            } else {
                compouseLog(resultLog, logsTotalTime, detailedLogs);
            }
        }

        return resultLog;
    }

    private static void compouseLog(Deque<String> logs, Map<String, Integer> logsTotalTime, Map<String, Integer> detailedLogs) {
        String format = "%s: %s минут %s %%";
        Optional<Integer> totalTime = logsTotalTime.values().stream()
                .reduce((s1, s2) -> s1 + s2);
        for (String key : logsTotalTime.keySet()) {
            logs.addLast(String.format(format, key, logsTotalTime.get(key), logsTotalTime.get(key) * 100 / totalTime.get()));
        }
        logs.addLast("Лекции:");
        for (String key : detailedLogs.keySet()) {
            logs.addLast(String.format(format, key, detailedLogs.get(key), detailedLogs.get(key) * 100 / totalTime.get()));
        }
        logs.addLast("\n");
    }

    private static boolean isValid(String employment) {
        String regex = "^(Упражнения|Перерыв|Обеденный перерыв|Решения)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(employment);

        return matcher.matches();
    }
}
