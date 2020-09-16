/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import bin.RequestHandler;
import bin.HttpdConf;
import bin.MimeTypes;
import bin.obj.HTTPRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebServer {
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;

    public static void main(String[] args) {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    private void start(){
        ServerSocket server;
        Socket client;
        int port;


        InputStream mimeTypesIS = getClass().getClassLoader().getResourceAsStream("conf/mime.types");
        InputStream httpdConfIS = getClass().getClassLoader().getResourceAsStream("conf/httpd.conf");

        this.mimeTypes = new MimeTypes(mimeTypesIS);
        this.httpdConf = new HttpdConf(httpdConfIS);

        port = Integer.parseInt(httpdConf.getHttpdConf("Listen"));

        Executor service = Executors.newFixedThreadPool(32);

        try{
            server = new ServerSocket(port);
            while(true){
                // TODO: Thread out request workers here
                client = server.accept();
            }
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

}
