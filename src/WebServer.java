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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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


        InputStream mimeTypesIS = getClass().getClassLoader().getResourceAsStream("conf/mime.types");
        InputStream httpdConfIS = getClass().getClassLoader().getResourceAsStream("conf/httpd.conf");

        MimeTypesParser mimeParser = new MimeTypesParser(mimeTypes);
        mimeParser.parse(mimeTypesIS);
        HttpdConfParser httpdConfParser = new HttpdConfParser(httpdConf);
        httpdConfParser.parse(httpdConfIS);

        port = Integer.parseInt(httpdConf.getHttpd("Listen"));

        Executor service = Executors.newFixedThreadPool(32);

        try{
            server = new ServerSocket(port);
            while(true){
                // TODO: Thread out request workers here
                client = server.accept();
                service.execute(new HTTPRequestThread(client.getInputStream(), client.getOutputStream(), httpdConf, mimeTypes));

            }
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

}
