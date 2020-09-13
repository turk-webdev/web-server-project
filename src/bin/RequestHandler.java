/**********************************************************************
 * File: RequestHandler.java
 * Description: This class will listen for an incoming request and then
 * assign a RequestWorker
 *********************************************************************/

package bin;

import bin.obj.HTTPRequest;

import java.io.BufferedReader;
import java.util.Stack;

public class RequestHandler {
    private Stack<HTTPRequest> requestStack;

    public RequestHandler() {
        requestStack =  new Stack<>();
    }

    public int processRequest(BufferedReader clientInput) {
        // Given an input stream from the client, build it into a full string
        String request = "",currLine;
        try {
            while ((currLine = clientInput.readLine()) != null) {
                request += currLine;
            }

            requestStack.push(new HTTPRequest(request));
        } catch (Exception e) {
            // TODO: Error handling
            e.printStackTrace();
            return 500;
        }

        return 200;
    }


}
