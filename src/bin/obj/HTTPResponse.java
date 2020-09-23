package bin.obj;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class HTTPResponse {
    private HashMap<String, String> responseHeaders;
    private String version, reasonPhrase;
    private byte[] body;
    private int statusCode;
    private OutputStream clientOutput;

    public HTTPResponse(OutputStream clientOutput) {
        responseHeaders = new HashMap<>();
        version = "";
        reasonPhrase = "";
        statusCode = 500;
        this.clientOutput = clientOutput;
    }

    public void sendResponse() {
        StringBuilder response = new StringBuilder();
        // First line of request is: VERSION STATUS-CODE REASON-PHRASE
        if (version.equals("")) {
            response.append("HTTP/1.1");
        } else {
            response.append(version);
        }
        response.append(" ");
        response.append(statusCode);
        response.append(" ");
        response.append(getReasonPhrase(statusCode));
        response.append("\r\n"); // CRLF

        // Next, we start putting in each independent header
        for (String key : responseHeaders.keySet()) {
            response.append(key);
            response.append(": ");
            response.append(responseHeaders.get(key));
            response.append("\n");
        }
        response.append("\r\n"); // CRLF
        // Lasly, we append the body

        // Convert our String data into a byte stream
        byte[] headers = response.toString().getBytes();

        // Combine the two byte streams
        byte[] data;
        try {
            data = new byte[body.length + headers.length];
            System.arraycopy(headers, 0, data, 0, headers.length);
            System.arraycopy(body, 0, data, headers.length, body.length);
        } catch (NullPointerException e) {
            data = new byte[headers.length];
            System.arraycopy(headers, 0, data, 0, headers.length);
        }

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
    public byte[] getBody() { return body; }
    public void setBody(byte[] body) { this.body = body; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String putResponseHeader(String key, String val) {
        return responseHeaders.put(key,val);
    }
    public String getResponseHeader(String key) { return responseHeaders.get(key); }


}
