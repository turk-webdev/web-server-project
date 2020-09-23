package bin.obj;

import java.util.HashMap;
import java.util.Set;

public class HttpdConf{

    private HashMap<String, String> httpdList; //HashMap for httpd keys and values
    private HashMap<String, String> scriptAliasList; //HashMap for script alias keys and values
    private HashMap<String, String> aliasList; //HashMap for alias keys and values

    public HttpdConf() {
        httpdList = new HashMap<>();
        scriptAliasList = new HashMap<>();
        aliasList = new HashMap<>();
    }

    public boolean httpdContainsKey(String key) { return httpdList.containsKey(key); }
    public boolean aliasContainsKey(String key) { return aliasList.containsKey(key); }
    public boolean scriptAliasContainsKey(String key) { return scriptAliasList.containsKey(key); }
    public Set<String> getAliasKeySet() { return aliasList.keySet(); }
    public Set<String> getScriptAliasKeySet() { return scriptAliasList.keySet(); }

    //standard getter methods
    public String getHttpd(String key){ return httpdList.get(key); }
    public String getAlias(String key){ return aliasList.get(key); }
    public String getScriptAlias(String key){ return scriptAliasList.get(key); }

    public String putHttpd(String key, String value){
        //puts it in reverse
        httpdList.put(value, key);
        //returns the value after putting it again not-reversed
        return httpdList.put(key, value);
    }
    public String putAlias(String key, String value){
        //puts it in reverse
        aliasList.put(value, key);
        //returns the value after putting it again not-reversed
        return aliasList.put(key, value);
    }
    public String putScriptAlias(String key, String value){
        //puts it in reverse
        scriptAliasList.put(value, key);
        //returns the value after putting it again not-reversed
        return scriptAliasList.put(key, value);
    }
}
