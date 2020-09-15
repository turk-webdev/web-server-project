/**********************************************************************
 * File: HTTPRequest.java
 * Description: This threaded object
 *********************************************************************/

package bin.obj;

import java.util.*;
import java.io.*;

/**
 * REQUEST FORMAT:
 * HTTP_METHOD(verb) IDENTIFIER HTTP_VERSION [\n]
 * BODY [\n]
 */
public class HTTPRequest implements Runnable {
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


    // DEBUG ONLY
    public void printRequest() {
        System.out.printf("%s\t%s\t%s\n",verb,identifier,version);
        for (String key : body.keySet()) {
            System.out.printf("%s: %s\n",key,body.get(key));
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // TODO: Write code
    }
}