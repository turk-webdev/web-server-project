package bin.obj;

import java.util.HashMap;

public class HTTPRequest {
    private String verb, identifier, version;
    private HashMap<String, String> headers;
    private String body;

    public HTTPRequest() {
        headers = new HashMap<>();
    }

    public void printRequest() {
        System.out.printf("%s %s %s\n",verb,identifier,version);
        for (String key: headers.keySet()) {
            System.out.printf("%s : %s\n",key, headers.get(key));
        }
    }

    // Getters
    public String getVerb() { return verb; }
    public String getIdentifier() { return identifier; }
    public String getVersion() { return version; }

    // Setters
    public void setVerb(String verb) { this.verb = verb; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public void setVersion(String version) { this.version = version; }

    // Hashmap helper functions
    public boolean containsKey(String key) { return headers.containsKey(key); }
    public boolean containsValue(String value) { return headers.containsValue(value); }
    public boolean isEmpty() { return headers.isEmpty(); }
    public String put(String key, String value) { return headers.put(key, value); }
    public String get(String key) { return headers.get(key); }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public int size() { return headers.size(); }


}
