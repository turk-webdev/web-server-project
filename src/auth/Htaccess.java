package auth;

import java.util.HashMap;

public class Htaccess {

    private HashMap <String, String> htaccessList;

    public Htaccess(){ htaccessList = new HashMap<>(); }

    public boolean containsKey(String key){ return htaccessList.containsKey(key); }

    public String get(String key){ return htaccessList.get(key);}

    public String put(String key, String value){ return htaccessList.put(key, value);}
}
