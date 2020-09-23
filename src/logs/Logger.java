package logs;

import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HttpdConf;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import static java.lang.String.format;

public class Logger {

    private HttpdConf httpdConf;

    public Logger(HttpdConf httpdConf) {
        this.httpdConf = httpdConf;
    }

    //TODO: add response functionality, implement in WebServer
    //List of needed variables for log
    //-request: InetAddress
    //-request: username
    //-request: method
    //-request: identifier
    //-request: version
    //-response: statuscode
    //-response: bytelength
    // example (from wiki): 127.0.0.1 user-identifier frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
    public void log(Socket client, HTTPRequest req, HTTPResponse resp, String username) {
        try {
            String logFile = httpdConf.getHttpd("LogFile");
            FileWriter fileWriter = new FileWriter(logFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String logMessage = String.format("%s %s %s [%s] %s %s %s %s %s\n",
                    client.getInetAddress(), "-", username, this.getDateTime(ZonedDateTime.now()),
                    req.getVerb(), req.getIdentifier(), req.getVersion(), resp.getStatusCode(), resp.getBodyLength());
            printWriter.printf("%s", logMessage);
            System.out.println(logMessage);
            printWriter.close();
        } catch (IOException e) {
            System.out.println("UNABLE TO WRITE TO LOG FILE");
            e.printStackTrace();
        }
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