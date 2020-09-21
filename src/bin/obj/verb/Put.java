/**********************************************************************
 * File: Put.java
 * Description: If the target resource does not have a current
 * representation, return a 201. If there is a current representation,
 * replace the contents of the existing file with the Put, and return 204.
 *********************************************************************/

package bin.obj.verb;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HTTPVerb;

import java.io.*;
import java.util.stream.Stream;

public class Put extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        String requestBody[] = requestObj.getBody().split(System.getProperty("line.separator"));
        File dest = new File(requestObj.getPathWithDest());
        int re;

        if (dest.exists()) {
            responseObj.setStatusCode(204);
            re = overwrite(dest, requestBody);
        } else {
            responseObj.setStatusCode(201);
            re = write(dest, requestBody);
        }

        if (re < 0) {
            // TODO: Send 500 response
            String html = "<html><body><h1>There was an error with creating the resource</h1></body></html>";
            responseObj.setBody(html);
            responseObj.putResponseHeader("Content-Type", worker.getMimeType("html"));
            responseObj.putResponseHeader("Content-Length", Integer.toString(html.length()));
            responseObj.setStatusCode(500);
        }

        responseObj.putResponseHeader("Content-Location", requestObj.getIdentifier());
        responseObj.sendResponse();
    }

    private int write(File file, String[] contentLines) {
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file, false);
            for (String line : contentLines) {
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private int overwrite(File file, String[] contentLines) {
        try {
            FileWriter writer = new FileWriter(file, false);
            for (String line : contentLines) {
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

}
