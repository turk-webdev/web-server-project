package bin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpdConf extends ConfigurationReader {

    private HashMap<String, String> httpdList;
    private HashMap<String, String> scriptAliasList;
    private HashMap<String, String> aliasList;

    public HttpdConf(InputStream file_name) {
        super(file_name);
        httpdList = new HashMap<>();
        scriptAliasList = new HashMap<>();
        aliasList = new HashMap<>();
        execute();
    }

    @Override
    public void execute() {
        //general storage variables for parsing, tokenizer for...tokenizing
        String currLine, key, value;
        StringTokenizer tokenizer;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(configFile,"UTF-8"));

            // read the file line by line
            while ((currLine = bufferedReader.readLine()) != null) {
                // Ignore blank lines
                if (currLine.equals("")) {
                    continue;
                }

                tokenizer = new StringTokenizer(currLine);
                String currToken = tokenizer.nextToken();

                // ignore comments
                if(currToken.equals("#") || currToken.charAt(0) == '#'){
                    continue;
                }

                // parsing to handle several exceptions within the httpd.conf file
                if (currToken.equals("ScriptAlias")) {
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    scriptAliasList.put(key, value);
                    scriptAliasList.put(value, key);
                    //second check is if its just an alias, they are also listed one by one
                }else if (currToken.equals("Alias")){
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    aliasList.put(key, value);
                    aliasList.put(value, key);
                    //third check is for Directory Index, special handling involved for multipled listed files
                } else if (currToken.equals("DirectoryIndex")){
                    key = currToken;
                    while(tokenizer.hasMoreTokens()){
                        value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                        httpdList.put(key, value);
                    }
                    //if its none of those, then it goes into the httpd list
                } else {
                    key = currToken;
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    httpdList.put(key, value);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    //standard getter methods

    public String getHttpdConf(String key){
        if (httpdList.containsKey(key)){
            return httpdList.get(key);
        }
        return null;
    }

    public String get_alias(String key){
        if(aliasList.containsKey(key)){
            return aliasList.get(key);
        }
        return null;
    }

    public String get_script_alias(String key){
        if(scriptAliasList.containsKey(key)){
            return scriptAliasList.get(key);
        }
        return null;
    }
}
