/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import auth.Htpassword;
import bin.*;
import bin.obj.HttpdConf;
import bin.obj.MimeTypes;
import bin.obj.parser.HtpasswordParser;
import bin.obj.parser.HttpdConfParser;
import bin.obj.parser.MimeTypesParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebServer {
    private HttpdConf httpdConf = new HttpdConf();
    private MimeTypes mimeTypes = new MimeTypes();
    private Htpassword htpassword = new Htpassword();

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();
    }

    private void start(){
        ServerSocket server;
        Socket client;
        int port;

        // Get input streams for our conf files
        InputStream mimeTypesIS = getClass().getClassLoader().getResourceAsStream("conf/mime.types");
        InputStream httpdConfIS = getClass().getClassLoader().getResourceAsStream("conf/httpd.conf");
        InputStream htpasswordIS = getClass().getClassLoader().getResourceAsStream("conf/.htpassword");

        // Parse our config files into their relevant objects
        MimeTypesParser mimeParser = new MimeTypesParser(mimeTypes);
        mimeParser.parse(mimeTypesIS);
        HttpdConfParser httpdConfParser = new HttpdConfParser(httpdConf);
        httpdConfParser.parse(httpdConfIS);
        HtpasswordParser htpasswordParser = new HtpasswordParser(htpassword);
        htpasswordParser.parse(htpasswordIS);

        port = Integer.parseInt(httpdConf.getHttpd("Listen"));

        Executor service = Executors.newFixedThreadPool(32);

        try{
            server = new ServerSocket(port);
            while(true){
                client = server.accept();
                service.execute(new HTTPRequestThread(client, httpdConf, mimeTypes, htpassword));

            }
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

}
