package conf.conf;

import java.util.*;
import java.io.*;

/**
 * REQUEST FORMAT:
 * HTTP_METHOD(verb) IDENTIFIER HTTP_VERSION [\n]
 * BODY [\n]
 */
public class HTTPRequest {
    private String verb, identifier, version; // header vars
    private HashMap<String, String> body = new HashMap<String, String>();

    public HTTPRequest(String request) throws IOException {
        Reader requestString = new StringReader(request);
        BufferedReader br = new BufferedReader(requestString);
        int linesRead = 0;

        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        // TODO: Error handling
        verb = st.nextToken();
        identifier = st.nextToken();
        version = st.nextToken();

        // DEBUG ONLY -- print header vars
        System.out.printf("verb=%s\nid=%s\nver=%s\n",verb,identifier,version);

        // Read through each line in the request
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line,":");

            // TODO: Error handling
            body.put(st.nextToken(),st.nextToken());

            linesRead++;
        }

        // DEBUG ONLY -- print out hashmap
        Iterator it = body.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.printf("K = '%s', V= '%s'\n",pair.getKey(), pair.getValue());
        }


    }

    // Getters
    public String getVerb() { return verb; }
    public String getIdentifier() { return identifier; }
    public String getVersion() { return version; }

}