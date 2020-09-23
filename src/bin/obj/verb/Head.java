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

public class Head extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        // If we have a caching header, check to see if cached version is current
        if (requestObj.containsKey("If-Modified-Since")) {
            if (CacheHelper.cacheIsOk(requestObj.getPathWithDest(), requestObj.get("If-Modified-Since")) == 0) {
                responseObj.setStatusCode(304);
            }
        }

        // If it isn't current, send the file contents
        try {
            responseObj.setStatusCode(200);
            // Lastly, we want to put the Last-Modified header to allow for caching
            responseObj.putResponseHeader("Last-Modified", CacheHelper.getLastModifiedDate(requestObj.getPathWithDest()));
        } catch (IOException e) {
            // If putting the Last-Modified header threw an error, just use the current date
            responseObj.putResponseHeader("Last-Modified", CacheHelper.getCurrentDate());
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
