/**********************************************************************
 * File: MimeTypes.java
 * Description: This class runs once at ANTIPARAZI start up to parse
 * through the mime.types file and
 *********************************************************************/
package bin;

import java.util.HashMap;

public class MimeTypes {
    private HashMap<String, String> typesHashMap;

    public MimeTypes() {
        typesHashMap = new HashMap<>();
    }

    // FOR DEBUG ONLY
    public void printTypes() {
        System.out.println("EXTENSION\tMIME TYPE");
        for (String key : typesHashMap.keySet()) {
            System.out.printf("%s\t%s\n",key, typesHashMap.get(key));
        }
    }

    // Hashmap helper functions
    public boolean containsKey(String key) { return typesHashMap.containsKey(key); }
    public boolean containsValue(String value) { return typesHashMap.containsValue(value); }
    public boolean isEmpty() { return typesHashMap.isEmpty(); }
    public String put(String key, String value) { return typesHashMap.put(key, value); }
    public String get(String key) { return typesHashMap.get(key); }
    public int size() { return typesHashMap.size(); }
}
