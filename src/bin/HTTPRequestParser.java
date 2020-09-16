package bin;
/**********************************************************************
 * File: HTTPRequestParser.java
 * Description: This is a parser for the input given by the thread, and will
 * deposit it into the provided HTTPRequest object
 *********************************************************************/

import java.io.BufferedReader;
import java.util.StringTokenizer;

/**
 * REQUEST FORMAT:
 * HTTP_METHOD(verb) IDENTIFIER HTTP_VERSION [\n]
 * BODY [\n]
 */

public class HTTPRequestParser {
    /**
     * Reads through the client's input stream to get the HTTP request,
     * then parses it into a request object
     * @param requestObj the object to hold data this method parses
     * @param inputReader buffered reader for the request
     * @return 400 if there is an error, 200 if all went OK
     */
    public int parseRequest(HTTPRequest requestObj, BufferedReader inputReader) {
        try {
            String currLine = inputReader.readLine();
            StringTokenizer st = new StringTokenizer(currLine);

            // First line is always the header, easiest way to find an error in request
            String currTok = st.nextToken();
            switch (currTok) {
                case "POST":
                case "PUT":
                case "HEAD":
                case "GET":
                case "DELETE":
                    break;
                default:
                    throw new Error("Verb is invalid");
            }

            requestObj.setVerb(currTok);
            requestObj.setIdentifier(st.nextToken());
            requestObj.setVersion(st.nextToken());

            // With the header parsed, the whole body follows a k-v pair pattern
            // Populate the HTTPRequest object's HashMap representing the body
            while ((currLine = inputReader.readLine()) != null) {
                st = new StringTokenizer(currLine);
                requestObj.put(st.nextToken(),st.nextToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 400;
        }

        return 200;
    }
}
