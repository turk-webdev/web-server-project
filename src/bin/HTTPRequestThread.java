/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This threaded object is run each time ANTIPARAZI receives a
 * new request. It delegates the parsing of the request and the response and
 * is simply responsible for facilitating communication to and from the client.
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
    private Htpassword htpassword;

    public HTTPRequestThread(Socket client, HttpdConf httpdConf, MimeTypes mimeTypes, Htpassword htpassword) {
        this.client = client;
        this.httpdConf = httpdConf;
        this.mimeTypes = mimeTypes;
        this.htpassword = htpassword;
    }

    @Override
    public void run() {
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

        // Mandatory response fields - Server & Date
        responseObj.putResponseHeader("Server","ANTIPARAZI/1.0");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        responseObj.putResponseHeader("Date",formatter.format(new Date()));

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

        URIResource uriObj = new URIResource();
        URIParser uriParser = new URIParser();
        uriParser.parseURI(requestObj.getIdentifier(), uriObj, this);
        requestObj.setUriObj(uriObj);

        // Check if directory is protected by testing
        AuthDriver authDriver = new AuthDriver();
        if (authDriver.isProtectedDir(uriObj.getPathToDest(), getHttpd("AccessFile"))) {
            int authCode = authDriver.run(requestObj, uriObj.getPathToDest()+"/"+getHttpd("AccessFile"), this);
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

    // htpassword functions
    public String getHtpassword(String key) { return htpassword.get(key); }
    public String putHtpassword(String key, String value) { return htpassword.put(key, value); }
    public boolean htpasswordContainsKey(String key) { return htpassword.containsKey(key); }

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