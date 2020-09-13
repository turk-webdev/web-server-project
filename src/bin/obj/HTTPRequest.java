/**********************************************************************
 * File: HTTPRequest.java
 * Description: This object is used by the RequestHandler to store all
 * fields of an incoming HTTP Request from the client
 *********************************************************************/

package bin.obj;

import java.util.*;
import java.io.*;

/**
 * REQUEST FORMAT:
 * HTTP_METHOD(verb) IDENTIFIER HTTP_VERSION [\n]
 * BODY [\n]
 */
public class HTTPRequest {
    private String verb, identifier, version; // header vars
    private HashMap<String, String> body = new HashMap<>();

    public HTTPRequest(String request) throws IOException {
        Reader requestString = new StringReader(request);
        BufferedReader br = new BufferedReader(requestString);

        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        // TODO: Error handling
        verb = st.nextToken();
        identifier = st.nextToken();
        version = st.nextToken();

        // For each line in the request, put the kv pairs into our map
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line,":");

            // TODO: Error handling
            body.put(st.nextToken(),st.nextToken());
        }
    }

    // Getters
    public String getVerb() { return verb; }
    public String getIdentifier() { return identifier; }
    public String getVersion() { return version; }

    // DEBUG ONLY
    public void printRequest() {
        System.out.printf("%s\t%s\t%s\n",verb,identifier,version);
        for (String key : body.keySet()) {
            System.out.printf("%s: %s\n",key,body.get(key));
        }
    }

}