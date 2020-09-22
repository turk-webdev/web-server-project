package bin.obj.verb;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HTTPVerb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Get extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        // If we have a caching header, check to see if cached version is current
        if (requestObj.containsKey("If-Modified-Since")) {
            try {
                FileTime lastModifiedTime = Files.getLastModifiedTime(Paths.get(requestObj.getPathWithDest()));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date lastModifiedDate = new Date(lastModifiedTime.toMillis());
                Date cacheDate = formatter.parse(requestObj.get("If-Modified-Since"));

                if (lastModifiedDate.compareTo(cacheDate) < 0) {
                    responseObj.setStatusCode(304);
                    responseObj.sendResponse();
                    return;
                }
            } catch (IOException e) {
                System.out.println("ERR::Issue with getting GET file's last modified time");
                e.printStackTrace();
            } catch (ParseException e) {
                System.out.println("ERR::Issue with parsing If-Modified-Since date");
                e.printStackTrace();
            }
        }

        // If it isn't current, send the file contents
        try {
            byte[] fileContents = Files.readAllBytes(Paths.get(requestObj.getPathWithDest()));

            responseObj.setBody(fileContents);
            responseObj.putResponseHeader("Content-Type", worker.getMimeType(requestObj.getFileExt()));
            responseObj.putResponseHeader("Content-Length", Integer.toString(fileContents.length));
            responseObj.setStatusCode(200);
        } catch (Exception e) {
            String errMsgHtml = "<html>\n<body>\n\t<h1>500 - Internal Server Error</h1>\n\t<p>There was an error with reading the requested resource</p>\n</body>\n</html>";
            byte[] data = errMsgHtml.getBytes();

            responseObj.setBody(data);
            responseObj.putResponseHeader("Content-Type", worker.getMimeType("html"));
            responseObj.putResponseHeader("Content-Length", Integer.toString(data.length));
            responseObj.setStatusCode(500);
            e.printStackTrace();
        }

        responseObj.sendResponse();
    }
}
