package bin;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class Logger {

    private HttpdConf httpdConf;

    public Logger(HttpdConf httpdConf) {
        this.httpdConf = httpdConf;
    }

    //TODO: ad
    public void log(HTTPRequest request, String username)
            throws IOException {
        String logFile = httpdConf.getHttpd("LogFile");
        FileWriter fileWriter = new FileWriter(logFile, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        String logMessage = request.getIdentifier() + "-" +
                username + " " + this.getDateTime(ZonedDateTime.now());
        printWriter.printf("%s", logMessage);

        printWriter.close();
    }

    private String getDateTime(ZonedDateTime timeStamp) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(timeStamp.getDayOfMonth());
        strBuilder.append("/");
        strBuilder.append(timeStamp.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        strBuilder.append("/");
        strBuilder.append(timeStamp.getYear());
        strBuilder.append(":");
        strBuilder.append(timeStamp.getHour());
        strBuilder.append(":");
        strBuilder.append(timeStamp.getMinute());
        strBuilder.append(":");
        strBuilder.append(timeStamp.getSecond());
        strBuilder.append(" ");
        strBuilder.append(timeStamp.getOffset());
        return strBuilder.toString();
    }
}
