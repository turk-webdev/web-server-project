import conf.configsetup.*;

public class testDriver{
    public static void main(String[] args){
        String file_name, test_alias;
        file_name = "httpdConfTest.txt";
        test_alias = "Listen";
        HttpdConf httpd_reader = new HttpdConf(file_name);
        System.out.println(test_alias + ": " + httpd_reader.get_httpd_conf(test_alias));
        }
}