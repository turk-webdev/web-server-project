package bin.obj.verb;

import bin.HTTPRequestThread;
import bin.obj.CacheHelper;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HTTPVerb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {// If it isn't current, send the file contents
        try {
            byte[] fileContents = Files.readAllBytes(Paths.get(requestObj.getPathWithDest()));

            responseObj.setBody(fileContents);
            responseObj.putResponseHeader("Content-Type", worker.getMimeType(requestObj.getFileExt()));
            responseObj.putResponseHeader("Content-Length", Integer.toString(fileContents.length));
            responseObj.setStatusCode(200);
        } catch (Exception e) {
            String errMsgHtml = "<html>\n<body>\n\t<h1>500 - Internal Server Error</h1>\n\t<p>There was some internal error</p>\n</body>\n</html>";
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
