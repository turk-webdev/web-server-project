import conf.configsetup.*;

public class testDriver{
    public static void main(String[] args){
        String fileName, testAlias;
        fileName = "httpdConfTest.txt";
        testAlias = "Listen";
        HttpdConf httpdReader = new HttpdConf(fileName);
        System.out.println(testAlias + ": " + httpdReader.getHttpdConf(testAlias));
        }
}