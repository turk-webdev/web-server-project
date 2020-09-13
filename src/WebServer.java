/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import parsers.HttpdConf;
import parsers.MimeTypes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private ServerSocket server;
    private Socket client;
    private int port;

    public static void main(String[] args) throws IOException {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    public void start(){
        this.loadConfigs();
        this.port = Integer.parseInt(this.httpdConf.getHttpdConf("Listen"));

        this.test();
        try{
            server = new ServerSocket(port);
            while(true){
                //todo: this is where threading is going to go, threads/Workers.
                client = server.accept();
                System.out.println("Connection Established!\n");
            }
        } catch (IOException e){
            System.out.println("IOException");
        }
        while(true){}
    }

    public void loadConfigs() {
        // TODO: Use relative pathing for this
        String mimeTypesURI="/src/conf/mime.types",httpdConfURI="/src/conf/httpd.conf";
        try {
            mimeTypesURI = Class.forName(this.getClass().getName()).getClassLoader().getResource("mime.types").toString();
            httpdConfURI = Class.forName(this.getClass().getName()).getClassLoader().getResource("httpd.conf").toString();
            System.out.printf("mime.types=%s, httpd.conf=%s\n",mimeTypesURI,httpdConfURI);
        } catch (Exception e) {
            System.out.println("Could not resolve config file paths");
            e.printStackTrace();
        }

        this.mimeTypes = new MimeTypes(mimeTypesURI);
        this.httpdConf = new HttpdConf(httpdConfURI);

    }

    public void test(){
        System.out.println("MIME TYPES HASHMAP\n========================");
        this.mimeTypes.print();
        String testAlias = "Listen";
        System.out.println("\n" + testAlias + ": " + this.port);

    }

}
