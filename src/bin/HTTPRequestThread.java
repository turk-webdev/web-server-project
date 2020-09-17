/**********************************************************************
 * File: HTTPRequestThread.java
 * Description: This threaded object is run each time ANTIPARAZI receives a
 * new request. It delegates the parsing of the request and the response and
 * is simply responsible for facilitating communication to and from the client.
 *********************************************************************/

package bin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

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
        StatusCodes codes = new StatusCodes();
        HTTPRequest requestObj = new HTTPRequest();
        HTTPRequestParser requestParser = new HTTPRequestParser();
        HTTPResponse responseObj = new HTTPResponse();

        // Mandatory response fields
        responseObj.putResponseHeader("Server","ANTIPARAZI/1.0");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        responseObj.putResponseHeader("Date",formatter.format(new Date()));

        int parseCode = requestParser.parseRequest(requestObj, new BufferedReader(new InputStreamReader(clientInput)));
        String verb = requestObj.getVerb(), uri = requestObj.getIdentifier(), modifiedURI = "";
        if (parseCode == 400) {
            // TODO: Send bad request response
            System.out.println("400: BAD REQUEST");
            return;
        }

        // Check if URI is aliased
        boolean isScriptAliased = false;
        if (httpdConf.aliasContainsKey(uri)) {
            modifiedURI = httpdConf.getAlias(uri);
        } else if (httpdConf.scriptAliasContainsKey(uri)) {
            isScriptAliased = true;
            modifiedURI = httpdConf.getScriptAlias(uri);
        } else {
            // If URI is not aliased, resolve the path DOC_ROOT+URI
            modifiedURI = httpdConf.getHttpd("DocumentRoot") + uri;
        }

        // Is it a file?
        // If no, append DirIndex then it becomes absolute path
        // If yes, it is an absolute path
        boolean hasIndex = false;
        if (!checkIfFile(parseURI(modifiedURI))) {
            // TODO: Append dirindex
            String contents[] = new File(modifiedURI).list();
            for (String curr : contents) {
                if (curr.contains("index")) {
                    modifiedURI += curr;
                    hasIndex = true;
                    break;
                }
            }
        } else {
            hasIndex = true;
        }

        // Does htaccess exist?
        String path = getPath(parseURI(modifiedURI));
        String contents[] = new File(path).list();
        boolean hasHtaccess = false;
        for (String curr : contents) {
            if (curr.contains("htaccess")) {
                hasHtaccess = true;
                break;
            }
        }

        // If yes, is there an Auth header?
        // If no, 401
        // If yes, is uname+pass valid?
        // If no, 403
        if (hasHtaccess) {
            // TODO: Auth check
        }

        // If yes, does the file exist?
        // If no, 404
        if (hasIndex) {

        } else {
            // TODO: 404 Response
        }

        // If yes, is script aliased?
        if (isScriptAliased) {
            // If yes, execute the script
            // 200 on success, 500 on fail

        } else {
            // If no, process verb
            switch (verb) {
                case "POST":
                case "PUT":
                case "HEAD":
                case "GET":
                case "DELETE":
                    break;
                default:
                    // TODO: Throw 400 error
            }
        }





    }

    String getPath(ArrayList<String> uri) {
        String re = "";

        for (int i=0; i<uri.size()-1; i++) {
            re += uri.get(i);
        }

        return re;
    }

    boolean checkIfFile(ArrayList<String> uri) {
        return uri.get(uri.size()-1).contains(".");
    }

    ArrayList<String> parseURI(String uri) {
        ArrayList<String> tmp = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(uri,"/");

        while (st.hasMoreTokens()) {
            tmp.add(st.nextToken());
        }

        return tmp;
    }
}