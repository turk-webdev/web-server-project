package bin;

import java.util.HashMap;

public class HTTPRequest {
    private String verb, identifier, version;
    private HashMap<String, String> body;

    public HTTPRequest() {
        body = new HashMap<>();
    }

    public void printRequest() {
        System.out.printf("%s %s %s\n",verb,identifier,version);
        for (String key: body.keySet()) {
            System.out.printf("%s : %s\n",key,body.get(key));
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
    public boolean containsKey(String key) { return body.containsKey(key); }
    public boolean containsValue(String value) { return body.containsValue(value); }
    public boolean isEmpty() { return body.isEmpty(); }
    public String put(String key, String value) { return body.put(key, value); }
    public String get(String key) { return body.get(key); }
    public int size() { return body.size(); }


}
