package bin.obj;

import java.util.HashMap;

public class HTTPResponse {
    private HashMap<String, String> responseHeaders;
    private String version, reasonPhrase;
    private int statusCode;

    public HTTPResponse() {
        responseHeaders = new HashMap<>();
        version = "";
        reasonPhrase = "";
        statusCode = -1;
    }

    // Getters & setters
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getReasonPhrase() { return reasonPhrase; }
    public void setReasonPhrase(String reasonPhrase) { this.reasonPhrase = reasonPhrase; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String putResponseHeader(String key, String val) {
        return responseHeaders.put(key,val);
    }


}
