import configsetup.*;

public class testDriver{
    public static void main(String[] args){
        String filename, testAlias;
        filename = "httpdConfTest.txt";
        testAlias = "Listen";
        System.out.println("Hello World!");
        HttpdConf httpd_reader = new HttpdConf(filename);
        System.out.println(httpd_reader.get_httpd_conf(testAlias));
        }
}