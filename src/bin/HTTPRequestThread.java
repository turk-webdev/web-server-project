/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This threaded object is run each time ANTIPARAZI receives a
 * new request. It delegates the parsing of the request and the response and
 * is simply responsible for facilitating communication to and from the client.
 *********************************************************************/

package bin;

import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.MimeTypes;
import bin.obj.URIResource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPRequestThread implements Runnable {
    private InputStream clientInput;
    private OutputStream clientOutput;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;

    public HTTPRequestThread(InputStream clientInput, OutputStream clientOutput, HttpdConf httpdConf, MimeTypes mimeTypes) {
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
        this.httpdConf = httpdConf;
        this.mimeTypes = mimeTypes;
    }

    /**
     * 1) Delegate parse & store the HTTP Request from clientInput
     * 2) Go through webserver workflow
     * 3) Delegate building of response
     * 4) Send response to clientOutput
     */
    @Override
    public void run() {
        // Init local objects
        HTTPRequest requestObj = new HTTPRequest();
        HTTPRequestParser requestParser = new HTTPRequestParser();
        HTTPResponse responseObj = new HTTPResponse();

        // Mandatory response fields
        responseObj.putResponseHeader("Server","ANTIPARAZI/1.0");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        responseObj.putResponseHeader("Date",formatter.format(new Date()));

        int parseCode = requestParser.parseRequest(requestObj, new BufferedReader(new InputStreamReader(clientInput)));
        String verb = requestObj.getVerb(), uri = requestObj.getIdentifier();
        if (parseCode == 400) {
            // TODO: Send bad request response
            System.out.println("400: BAD REQUEST");
            return;
        }

        URIResource uriObj = new URIResource(uri, httpdConf);

        // This object will go through all of the path parsing in the workflow
        uriObj.run();
        String absolutePath = uriObj.getModifiedUriString();

        // Does htaccess exist?
        if (uriObj.uriContains(absolutePath, ".htaccess", true)) {
            // TODO: Check Auth header
            // If yes, is there an Auth header?
            // If no, 401
        } else {
            // If htaccess does not exist, check if file exists
            File file = new File(absolutePath);
            if (file.exists()) {

            } else {
                // TODO: Send 404
                System.out.println("404: FILE NOT FOUND");
                return;
            }
        }

        if (uriObj.isScriptAliased()) {
            // TODO: Execute script
        } else {
            switch (verb) {
                case "POST":
                    post();
                case "PUT":
                    put();
                case "HEAD":
                    head();
                case "GET":
                    get();
                case "DELETE":
                    delete();
                    break;
                default:
                    // TODO: Throw 400 error
            }
        }

    }

    // HTTP Verbs
    void put() {}
    void post() {}
    void head() {}
    void get() {}
    void delete() {}
}