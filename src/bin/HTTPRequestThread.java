/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This thread is responsible for following the basic flow
 * of the webserver by delegating jobs for each step to the necessary
 * objects.
 *********************************************************************/

package bin;

import auth.Authentication;
import bin.obj.*;
import bin.obj.parser.HTTPRequestParser;
import bin.obj.parser.URIParser;
import logs.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPRequestThread extends Thread {
    private Socket client;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;

    public HTTPRequestThread(Socket client, HttpdConf httpdConf, MimeTypes mimeTypes) {
        this.client = client;
        this.httpdConf = httpdConf;
        this.mimeTypes = mimeTypes;
    }

    @Override
    public void run() {
        // Init our I/O streams for the client
        OutputStream clientOutput;
        InputStream clientInput;
        try {
            clientInput = client.getInputStream();
            clientOutput = client.getOutputStream();
        } catch (IOException e) {
            System.out.println("Error establishing server I/O");
            e.printStackTrace();
            return;
        }

        // Init local objects
        HTTPRequest requestObj = new HTTPRequest();
        HTTPRequestParser requestParser = new HTTPRequestParser();
        HTTPResponse responseObj = new HTTPResponse(clientOutput);
        URIResource uriObj = new URIResource();
        URIParser uriParser = new URIParser();
        Logger logger = new Logger(httpdConf);

        // Mandatory response fields - Server & Date
        responseObj.putResponseHeader("Server","BeaErdin/1.0");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        responseObj.putResponseHeader("Date",formatter.format(new Date()));

        // First we want to make sure the request is valid
        int parseCode = requestParser.parseRequest(requestObj, new BufferedReader(new InputStreamReader(clientInput)));
        if (parseCode == 400) {
            responseObj.setStatusCode(parseCode);
            responseObj.sendResponse();
            try {
                clientInput.close();
                clientOutput.flush();
                clientOutput.close();
                client.close();
            } catch (IOException e) {
                System.out.println("Error closing connection");
                e.printStackTrace();
            }
            return;
        }
        responseObj.setVersion(requestObj.getVersion());

        // Next, we want to parse the URI to check if it's aliased
        // and resolve the absolute path to the requested resource
        uriParser.parseURI(requestObj.getIdentifier(), uriObj, this);
        requestObj.setUriObj(uriObj); // We need to put the uriObj in the requestObj so it can be accessed by the HTTPVerb obj

        // Next, we perform our authorization checks
        // If the target directory is protected, check the headers
        Authentication auth = new Authentication();
        if (auth.isProtectedDir(uriObj.getPathToDest(), getHttpd("AccessFile"))) {
            int authCode = auth.run(uriObj.getPathToDest() + "/" + getHttpd("AccessFile"), responseObj, requestObj);

            // If things were not okay, then we built the response within auth, just send the response
            if (authCode != 200) {
                responseObj.sendResponse();
                try {
                    client.close();
                } catch (IOException e) {
                    System.out.println("Error closing connection");
                    e.printStackTrace();
                }
                return;
            }
        }

        // Check if file exists
        if (!uriObj.checkFileExists() && !requestObj.getVerb().equals("PUT")) {
            responseObj.setStatusCode(404);
            String html = "<html><body><h1>404 File Not Found</h1><p>The requested resource could not be found</b></body></html>";
            byte[] data = html.getBytes();
            responseObj.putResponseHeader("Content-Type","text/html");
            responseObj.putResponseHeader("Content-Length",Integer.toString(data.length));
            responseObj.sendResponse();
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error closing connection");
                e.printStackTrace();
            }
            return;
        }

        if (uriObj.isScriptAliased()) {
            CGIHandler cgiHandler = new CGIHandler();
            byte[] body = cgiHandler.handle(uriObj, requestObj);
            if (!(body.equals("error".getBytes()))){
                responseObj.setStatusCode(200);
                responseObj.setBody(body);
            } else {
                responseObj.setStatusCode(500);
            }
            responseObj.sendResponse();
            return;
        }

        // With every check complete, we can now process the verb
        HTTPVerb verbObj = HTTPVerb.getVerb(requestObj.getVerb().toUpperCase());
        verbObj.execute(responseObj, requestObj, this);

        // Finally, log everything
        logger.log(client, requestObj, responseObj, auth.getUsername());

        // With everything done, we close the client connection
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("Something went horribly wrong");
            e.printStackTrace();
        }
    }

    // httpd.conf functions
    public boolean httpdContainsKey(String key) { return httpdConf.httpdContainsKey(key); }
    public boolean aliasContainsKey(String key) { return httpdConf.aliasContainsKey(key); }
    public boolean scriptAliasContainsKey(String key) { return httpdConf.scriptAliasContainsKey(key); }
    public String getHttpd(String key){ return httpdConf.getHttpd(key); }
    public String getAlias(String key){ return httpdConf.getAlias(key); }
    public String getScriptAlias(String key){ return httpdConf.getScriptAlias(key); }

    // mime.types functions
    public String getMimeType(String key) { return mimeTypes.get(key); }
}