package logsCourses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        Path pathLog = Paths.get("resources", "log.txt");
        Path pathTimeIntervalLog = Paths.get("resources", "timeIntervalsLog.txt");

        List<String> logs = Files.readAllLines(pathLog);
        Files.write(pathTimeIntervalLog, LogUtil.getTimeIntervalLogs(logs));

        Path pathTotalTimeLog = Paths.get("resources", "totalTimeLog.txt");
        logs = Files.readAllLines(pathTimeIntervalLog);
        Files.write(pathTotalTimeLog, LogUtil.getReportCommonTime(logs));

    }
}

