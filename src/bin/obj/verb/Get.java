package bin.obj.verb;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HTTPVerb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Get extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        StringBuilder body = new StringBuilder();
        File file = new File(requestObj.getPathWithDest());

        // TODO: Caching

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            responseObj.setBody(body.toString());
            // TODO: Dynamic time acquisition
            responseObj.putResponseHeader("Content-Type", worker.getMimeType("html"));
            responseObj.putResponseHeader("Content-Length", Integer.toString(body.toString().length()));
            responseObj.setStatusCode(200);
        } catch (Exception e) {
            String html = "<html><body><h1>There was an error with reading the resource</h1></body></html>";
            responseObj.setBody(html);
            responseObj.putResponseHeader("Content-Type", worker.getMimeType("html"));
            responseObj.putResponseHeader("Content-Length", Integer.toString(html.length()));
            responseObj.setStatusCode(500);
            e.printStackTrace();
        }

        responseObj.sendResponse();
    }
}
