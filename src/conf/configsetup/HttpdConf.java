package conf.configsetup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpdConf extends ConfigurationReader {

    private HashMap<String, String> httpdList;
    private HashMap<String, String> scriptAliasList;
    private HashMap<String, String> aliasList;

    public HttpdConf(String file_name) {
        super(file_name);
        this.httpdList = new HashMap<>();
        this.scriptAliasList = new HashMap<>();
        this.aliasList = new HashMap<>();
        execute();
    }

    @Override
    public void execute() {
        //general storage variables for parsing, tokenizer for...tokenizing
        String currLine, key, value;
        StringTokenizer tokenizer;
        try {
            //set up the readers
            this.fileReader = new FileReader(this.configFile);
            this.bufferedReader = new BufferedReader(this.fileReader);
            //while the file still has lines
            while ((currLine = this.bufferedReader.readLine()) != null) {
                //set the tokenizer to the current line, get the next token
                tokenizer = new StringTokenizer(currLine);
                String currToken = tokenizer.nextToken();
                //hashtags are line delimiters
                if(currToken.equals("#")){
                    continue;
                }
                //the actual parsing
                //first check is if its a script alias, they are listed one by one
                if (currToken.equals("ScriptAlias")) {
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    this.scriptAliasList.put(key, value);
                    this.scriptAliasList.put(value, key);
                    //second check is if its just an alias, they are also listed one by one
                }else if (currToken.equals("Alias")){
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    this.aliasList.put(key, value);
                    this.aliasList.put(value, key);
                    //third check is for Directory Index, special handling involved for multipled listed files
                } else if (currToken.equals("DirectoryIndex")){
                    key = currToken;
                    while(tokenizer.hasMoreTokens()){
                        value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                        this.httpdList.put(key, value);
                    }
                    //if its none of those, then it goes into the httpd list
                } else {
                    key = currToken;
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    this.httpdList.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    //standard getter methods

    public String getHttpdConf(String key){
        if (this.httpdList.containsKey(key)){
            return this.httpdList.get(key);
        }
        return null;
    }

    public String get_alias(String key){
        if(this.aliasList.containsKey(key)){
            return this.aliasList.get(key);
        }
        return null;
    }

    public String get_script_alias(String key){
        if(this.scriptAliasList.containsKey(key)){
            return this.scriptAliasList.get(key);
        }
        return null;
    }
}
