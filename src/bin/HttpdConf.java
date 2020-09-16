package bin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpdConf{

    private HashMap<String, String> httpdList; //HashMap for httpd keys and values
    private HashMap<String, String> scriptAliasList; //HashMap for script alias keys and values
    private HashMap<String, String> aliasList; //HashMap for alias keys and values

    public HttpdConf() {
        httpdList = new HashMap<>();
        scriptAliasList = new HashMap<>();
        aliasList = new HashMap<>();
    }

    public void printDebug() {
        System.out.println("~~~~~~~~~~~~~");
        System.out.println("HttpdList");
        System.out.println("~~~~~~~~~~~~~");
        for (String key : httpdList.keySet()) {
            System.out.printf("%s\t%s\n",key, httpdList.get(key));
        }
        System.out.println("~~~~~~~~~~~~~");
        System.out.println("ScriptAliasList");
        System.out.println("~~~~~~~~~~~~~");
        for (String key : scriptAliasList.keySet()) {
            System.out.printf("%s\t%s\n",key, scriptAliasList.get(key));
        }
        System.out.println("~~~~~~~~~~~~~");
        System.out.println("AliasList");
        System.out.println("~~~~~~~~~~~~~");
        for (String key : aliasList.keySet()) {
            System.out.printf("%s\t%s\n",key, aliasList.get(key));
        }
    }

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
