/**********************************************************************
 * File: RequestHandler.java
 * Description: This class will listen for an incoming request and then
 * assign a RequestWorker
 *********************************************************************/

package bin;

import bin.obj.HTTPRequest;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Stack;

public class RequestHandler {
    private Stack<HTTPRequest> requestStack;
    private BufferedReader clientInput;
    private PrintWriter clientOutput;

    public RequestHandler(BufferedReader clientInput, PrintWriter clientOutput) {
        requestStack =  new Stack<>();
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
    }

    public void processRequest() {
        // Given an input stream from the client, build it into a full string
        // The string will be used as the argument to construct an HTTPRequest obj
        String request = "",currLine;
        try {
            while ((currLine = clientInput.readLine()) != null) {
                request += currLine;
            }

            requestStack.push(new HTTPRequest(request));
        } catch (Exception e) {
            // TODO: Error handling
            e.printStackTrace();
        }
    }


}
