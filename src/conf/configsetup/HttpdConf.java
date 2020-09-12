//package conf.configsetup;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.StringTokenizer;
//
//public class HttpdConf extends ConfigurationReader {
//
//    private HashMap<String, String> httpd_list;
//    private HashMap<String, String> script_alias_list;
//    private HashMap<String, String> alias_list;
//
//    public HttpdConf(String file_name) {
//        super(file_name);
//        this.httpd_list = new HashMap<String, String>();
//        this.script_alias_list = new HashMap<String, String>();
//        this.alias_list = new HashMap<String, String>();
//        this.execute();
//    }
//
//    @Override
//    public void execute() {
//
//        //general storage variables for parsing, tokenizer for...tokenizing
//        String current_line, key, value;
//        StringTokenizer tokenizer;
//
//        try {
//            //set up the readers
//            this.file_reader = new FileReader(this.config_file);
//            this.buffer_reader = new BufferedReader(this.file_reader);
//            //while the file still has lines
//            while ((current_line = this.buffer_reader.readLine()) != null) {
//                //set the tokenizer to the current line, get the next token
//                tokenizer = new StringTokenizer(current_line);
//                String current_token = tokenizer.nextToken();
//                //hashtags are line delimiters
//                if(current_token.equals("#")){
//                    continue;
//                }
//                //the actual parsing
//                //first check is if its a script alias, they are listed one by one
//                if (current_token.equals("ScriptAlias")) {
//                    key = tokenizer.nextToken();
//                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
//                    this.script_alias_list.put(key, value);
//                //second check is if its just an alias, they are also listed one by one
//                }else if (current_token.equals("Alias")){
//                    key = tokenizer.nextToken();
//                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
//                    this.alias_list.put(key, value);
//                //third check is for Directory Index, special handling involved for multipled listed files
//                } else if (currToken.equals("DirectoryIndex")){
//                    key = current_token;
//                    while(tokenizer.hasMoreTokens()){
//                        value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
//                        this.httpd_list.put(key, value);
//                    }
//                //if its none of those, then it goes into the httpd list
//                } else {
//                    key = current_token;
//                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
//                    this.httpdList.put(key, value);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("IOException");
//        }
//    }
//
//    //standard getter methods
//
//    public String get_httpd_conf(String key){
//        if (this.httpdList.containsKey(key)){
//            return this.httpdList.get(key);
//        }
//        return null;
//    }
//
//    public String get_alias(String key){
//        if(this.alias_list.containsKey(key)){
//            return this.alias_list.get(key);
//        }
//        return null;
//    }
//
//    public String get_script_alias(String key){
//        if(this.script_alias_list.containsKey(key)){
//            return this.script_alias_list.get(key);
//        }
//        return null;
//    }
//}
