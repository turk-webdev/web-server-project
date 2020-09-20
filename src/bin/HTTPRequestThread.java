/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This threaded object is run each time ANTIPARAZI receives a
 * new request. It delegates the parsing of the request and the response and
 * is simply responsible for facilitating communication to and from the client.
 *********************************************************************/

package bin;

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

        URIResource uriObj = new URIResource();
        URIParser uriParser = new URIParser();
        uriParser.parseURI(requestObj.getIdentifier(), uriObj, this);

        // Check if directory is protected by .htaccess
        if (isProtectedDir(uriObj.getPathToDest())) {
            // TODO: Check auth header
        }



    }

    boolean isProtectedDir(String path) {
        String contents[] = new File(path).list();

        for (String currFile : contents) {
            if (currFile.equals(getHttpd("AccessFile"))) return true;
        }

        return false;
    }

    // httpd.conf functions
    public boolean httpdContainsKey(String key) { return httpdConf.httpdContainsKey(key); }
    public boolean aliasContainsKey(String key) { return httpdConf.aliasContainsKey(key); }
    public boolean scriptAliasContainsKey(String key) { return httpdConf.scriptAliasContainsKey(key); }

    public String getHttpd(String key){ return httpdConf.getHttpd(key); }
    public String getAlias(String key){ return httpdConf.getAlias(key); }
    public String getScriptAlias(String key){ return httpdConf.getScriptAlias(key); }
}