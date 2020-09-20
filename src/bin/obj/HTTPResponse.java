package bin.obj;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class HTTPResponse {
    private HashMap<String, String> responseHeaders;
    private String version, reasonPhrase, body;
    private int statusCode;
    private OutputStream clientOutput;

    public HTTPResponse(OutputStream clientOutput) {
        responseHeaders = new HashMap<>();
        version = "";
        body = "";
        reasonPhrase = "";
        statusCode = -1;
        this.clientOutput = clientOutput;
    }

    public void sendResponse() {
        StringBuilder response = new StringBuilder();
        // First line of request is: VERSION STATUS-CODE REASON-PHRASE
        response.append(version);
        response.append(" ");
        response.append(statusCode);
        response.append(" ");
        response.append(getReasonPhrase(statusCode));
        response.append("\n");

        // Next, we start putting in each independent header
        for (String key : responseHeaders.keySet()) {
            response.append(key);
            response.append(":");
            response.append(responseHeaders.get(key));
            response.append("\n");
        }

        // Lasly, we append the body
        response.append(body);

        // Convert our String data into a byte stream
        byte[] data = response.toString().getBytes();

        try {
            clientOutput.write(data);
        } catch (IOException e) {
            System.out.println("Some kind of error with writing to client");
            e.printStackTrace();
        }
    }

    private String getReasonPhrase(int code) {
        return StatusCodes.getTitleFromCode(code);
    }

    // Getters & setters
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String putResponseHeader(String key, String val) {
        return responseHeaders.put(key,val);
    }
    public String getResponseHeader(String key) { return responseHeaders.get(key); }


}
