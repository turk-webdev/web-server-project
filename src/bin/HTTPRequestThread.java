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
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPRequestThread implements Runnable {
    private InputStream clientInput;
    private OutputStream clientOutput;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private Htpassword htpassword;

    public HTTPRequestThread(InputStream clientInput, OutputStream clientOutput, HttpdConf httpdConf, MimeTypes mimeTypes, Htpassword htpassword) {
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
        this.httpdConf = httpdConf;
        this.mimeTypes = mimeTypes;
        this.htpassword = htpassword;
    }

    @Override
    public void run() {
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
            responseObj.setStatusCode(parseCode);
            responseObj.sendResponse();
            return;
        }

        URIResource uriObj = new URIResource();
        URIParser uriParser = new URIParser();
        uriParser.parseURI(requestObj.getIdentifier(), uriObj, this);

        // Check if directory is protected by .htaccess
        AuthDriver authDriver = new AuthDriver();
        if (authDriver.isProtectedDir(uriObj.getPathToDest(), getHttpd("AccessFile"))) {
            int authCode = authDriver.run();
            if (authCode != 200) {
                responseObj.setStatusCode(authCode);
                responseObj.sendResponse();
                return;
            }
        }

        // Check if file exists
        if (!uriObj.checkFileExists()) {
          responseObj.setStatusCode(404);
            responseObj.sendResponse();
            return;
        }

        if (uriObj.isScriptAliased()) {
            // TODO: script processing here
            return;
        }

        // TODO: Verb processing here


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
}