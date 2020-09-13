/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import bin.RequestHandler;
import parsers.HttpdConf;
import parsers.MimeTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private ServerSocket server;
    private Socket client;
    private int port;

    public static void main(String[] args) {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    private void start(){
        loadConfigs();
        port = Integer.parseInt(httpdConf.getHttpdConf("Listen"));

//        test();
        try{
            server = new ServerSocket(port);
            while(true){
                //todo: this is where threading is going to go, threads/Workers.
                client = server.accept();
                RequestHandler rh = new RequestHandler();
                System.out.println("Connection Established!\n");
                rh.processRequest(new BufferedReader(new InputStreamReader(client.getInputStream())));
            }
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    private void loadConfigs() {
        // TODO: Use relative pathing for this
        InputStream mimeTypesIS = getClass().getClassLoader().getResourceAsStream("conf/mime.types");
        InputStream httpdConfIS = getClass().getClassLoader().getResourceAsStream("conf/httpd.conf");

        this.mimeTypes = new MimeTypes(mimeTypesIS);
        this.httpdConf = new HttpdConf(httpdConfIS);

    }

    private void test(){
        System.out.println("MIME TYPES HASHMAP\n========================");
        mimeTypes.print();
        String testAlias = "Listen";
        System.out.println("\n" + testAlias + ": " + port);

    }

}
