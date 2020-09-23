/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import bin.*;
import bin.obj.HttpdConf;
import bin.obj.MimeTypes;
import bin.obj.parser.HttpdConfParser;
import bin.obj.parser.MimeTypesParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
    private HttpdConf httpdConf = new HttpdConf();
    private MimeTypes mimeTypes = new MimeTypes();

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

        // Parse our config files into their relevant objects
        MimeTypesParser mimeParser = new MimeTypesParser(mimeTypes);
        mimeParser.parse(mimeTypesIS);
        HttpdConfParser httpdConfParser = new HttpdConfParser(httpdConf);
        httpdConfParser.parse(httpdConfIS);

        port = Integer.parseInt(httpdConf.getHttpd("Listen"));

        try{
            server = new ServerSocket(port);
            while(true){
                client = server.accept();
//                client.getInputStream().close();
//                client.getOutputStream().flush();
//                client.getOutputStream().close();
//                client.close();
//                break;
                // Create a new thread each time we accept a request
                new Thread(new HTTPRequestThread(client, httpdConf, mimeTypes)).start();

            }
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Done");
    }

}
