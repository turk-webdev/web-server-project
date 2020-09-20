package bin.obj;

import java.util.HashMap;

public class Htpassword {

    private HashMap<String, String> htpasswordList;

    public Htpassword(){ htpasswordList = new HashMap<>();}

    public boolean containsKey(String key){ return htpasswordList.containsKey(key); }

    public String get(String key){ return htpasswordList.get(key); }

    public String put(String key, String value){ return htpasswordList.put(key, value); }
}
