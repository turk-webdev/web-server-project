import java.util.*;
import java.io.*;

/**
 * REQUEST FORMAT:
 * HTTP_METHOD(verb) IDENTIFIER HTTP_VERSION [\n]
 * BODY [\n]
 */
public class HTTPRequest {
    // header info
    private String verb, identifier, version;
    private HashMap<String, String> body = new HashMap<String, String>();

    public HTTPRequest(String request) throws IOException {
        Reader requestString = new StringReader(request);
        BufferedReader br = new BufferedReader(requestString);
        int linesRead = 0;
        String line;

        // Read through each line in the request
        while (null != (line = br.readLine())) {
            String[] lineArr = line.split("\\s+");
            // If this is the first line, then it is the header and we want to grab the header info
            if (linesRead == 0) {
                // TODO: Error handling
                verb = lineArr[0];
                identifier = lineArr[1];
                version = lineArr[2];
            }

            // TODO: Error handling & fix regex
            lineArr[0].replaceAll("\\:",""); // This should remove all colons in the param name
            body.put(lineArr[0],lineArr[1]);

            linesRead++;
        } // end while


    } // end constructor

    // Getters
    public String getVerb() { return verb; }
    public String getIdentifier() { return identifier; }
    public String getVersion() { return version; }

} // end class
