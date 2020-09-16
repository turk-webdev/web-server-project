/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This threaded object is run each time ANTIPARAZI receives a
 * new request. It delegates the parsing of the request and the response and
 * is simply responsible for facilitating communication to and from the client.
 *********************************************************************/

package bin;

import java.io.*;

public class HTTPRequestThread implements Runnable {
    private InputStream clientInput;
    private OutputStream clientOutput;

    public HTTPRequestThread(InputStream clientInput, OutputStream clientOutput) {
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
    }

    /**
     * 1) Delegate parse & store the HTTP Request from clientInput
     * 2) Go through webserver workflow
     * 3) Delegate building of response
     * 4) Send response to clientOutput
     */
    @Override
    public void run() {
        System.out.println("Running");
        HTTPRequest requestObj = new HTTPRequest();
        HTTPRequestParser requestParser = new HTTPRequestParser();

        int parseCode = requestParser.parseRequest(requestObj, new BufferedReader(new InputStreamReader(clientInput)));
        requestObj.printRequest();


    }
}