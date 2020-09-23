package bin.obj.verb;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.HTTPVerb;

import java.io.File;

public class Delete extends HTTPVerb {
    @Override
    public void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker) {
        File file = new File(requestObj.getPathWithDest());
        if (!file.exists()) {
            responseObj.setStatusCode(404);
        } else if(file.delete()) {
            responseObj.setStatusCode(204);
        } else {
            responseObj.setStatusCode(500);
            String html = "<html>\n<body>\n<h1>500 Internal Server Error</h1>\n<p>Something went wrong while processing the request</p>\n</body>\n</html>";
            byte data[] = html.getBytes();
            responseObj.putResponseHeader("Content-Type", "text/html");
            responseObj.putResponseHeader("Content-Length", Integer.toString(data.length));
        }

        responseObj.sendResponse();
    }
}
