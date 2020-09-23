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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Put extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        try {
            FileWriter writer = new FileWriter(requestObj.getPathWithDest(),false);
            byte data[] = requestObj.getBody().getBytes();

            if (Files.exists(Paths.get(requestObj.getPathWithDest()))) {
                responseObj.setStatusCode(204);
            } else {
                responseObj.setStatusCode(201);
            }

            responseObj.putResponseHeader("Content-Type", worker.getMimeType(requestObj.getFileExt()));
            responseObj.putResponseHeader("Content-Length", Integer.toString(data.length));

            writer.write(requestObj.getBody());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            responseObj.setStatusCode(500);
            String html = "<html>\n<body>\n<h1>500 Internal Server Error</h1>\n<p>Something went wrong while processing the request</p>\n</body>\n</html>";
            byte data[] = html.getBytes();
            responseObj.putResponseHeader("Content-Type", "text/html");
            responseObj.putResponseHeader("Content-Length", Integer.toString(data.length));
        }

        responseObj.sendResponse();
    }

}
