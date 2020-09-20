package bin.obj;

import bin.HTTPRequestThread;
import bin.obj.verb.*;

import java.util.HashMap;

public abstract class HTTPVerb {
    static HashMap<String, HTTPVerb> verbMap = new HashMap<>();
    static {
        verbMap.put("PUT", new Put());
        verbMap.put("POST", new Post());
        verbMap.put("HEAD", new Head());
        verbMap.put("GET", new Get());
        verbMap.put("DELETE", new Delete());
    }

    public static HTTPVerb getVerb(String verb) {
        return verbMap.get(verb);
    }

    public static boolean checkVerb(String verb) {
        return verbMap.containsKey(verb);
    }

    public abstract void execute(HTTPResponse responseObj, HTTPRequest requestObj, HTTPRequestThread worker);
}
