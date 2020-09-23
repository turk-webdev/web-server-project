package bin.obj;

import java.util.HashMap;
import java.util.Set;

public class HTTPRequest {
    private String verb, identifier, version;
    private HashMap<String, String> headers;
    private String body;
    private URIResource uriObj;

    public HTTPRequest() {
        headers = new HashMap<>();
        verb = "";
        identifier = "";
        version = "";
        body = "";
    }

    // Getters
    public String getVerb() { return verb; }
    public String getIdentifier() { return identifier; }
    public String getVersion() { return version; }

    // Setters
    public void setVerb(String verb) { this.verb = verb; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public void setVersion(String version) { this.version = version; }
    public void setUriObj(URIResource uriObj) { this.uriObj = uriObj; }

    // Hashmap helper functions
    public boolean containsKey(String key) { return headers.containsKey(key); }
    public boolean containsValue(String value) { return headers.containsValue(value); }
    public boolean isEmpty() { return headers.isEmpty(); }
    public String put(String key, String value) { return headers.put(key, value); }
    public String get(String key) { return headers.get(key); }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public int size() { return headers.size(); }

    // URIResource helper functions
    public String getFullUri() { return uriObj.getFullUri(); }
    public String getPathToDest() { return uriObj.getPathToDest(); }
    public String getPathWithDest() { return uriObj.getPathWithDest(); }
    public String getDestination() { return uriObj.getDestination(); }
    public boolean isAliased() { return uriObj.isAliased(); }
    public boolean isScriptAliased() { return uriObj.isScriptAliased(); }
    public String getFileExt() { return uriObj.getFileExt(); }
    public Set<String> argsKeySet() { return uriObj.argsKeySet(); }
    public String getArgs(String key) { return uriObj.getArgs(key); }

    // CGI Handler helper function
    public Set<String> envKeySet() { return headers.keySet(); }

}
