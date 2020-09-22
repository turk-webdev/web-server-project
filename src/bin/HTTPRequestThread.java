/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This thread is responsible for following the basic flow
 * of the webserver by delegating jobs for each step to the necessary
 * objects.
 *********************************************************************/

package bin;

import auth.AuthDriver;
import auth.Htpassword;
import bin.obj.*;
import bin.obj.parser.HTTPRequestParser;
import bin.obj.parser.URIParser;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPRequestThread implements Runnable {
    private Socket client;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;

    public HTTPRequestThread(Socket client, HttpdConf httpdConf, MimeTypes mimeTypes, Htpassword htpassword) {
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

        // Mandatory response fields - Server & Date
        responseObj.putResponseHeader("Server","ErdinBea/1.0");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        responseObj.putResponseHeader("Date",formatter.format(new Date()));

        // First we want to make sure the request is valid
        int parseCode = requestParser.parseRequest(requestObj, new BufferedReader(new InputStreamReader(clientInput)));
        if (parseCode == 400) {
            System.out.printf("[%s] Line 50: 400 code\n",Thread.currentThread().getName());
            responseObj.setStatusCode(parseCode);
            responseObj.sendResponse();
            try {
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
        AuthDriver authDriver = new AuthDriver();
        if (authDriver.isProtectedDir(uriObj.getPathToDest(), getHttpd("AccessFile"))) {
            int authCode = authDriver.run(requestObj, uriObj.getPathToDest()+"/"+getHttpd("AccessFile"), this);
            switch (authCode) {
                case 401:
//                    authDriver
                case 403:
                    break;
            }
            if (authCode != 200) {
                responseObj.setStatusCode(authCode);
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
        if (!uriObj.checkFileExists()) {
            responseObj.setStatusCode(404);
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
            // TODO: script processing here
            return;
        }

        // With every check complete, we can now process the verb
        HTTPVerb verbObj = HTTPVerb.getVerb(requestObj.getVerb().toUpperCase());
        verbObj.execute(responseObj, requestObj, this);

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