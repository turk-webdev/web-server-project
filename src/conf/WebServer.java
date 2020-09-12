/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/
package conf;

import conf.configsetup.HttpdConf;
import conf.configsetup.MimeTypes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private HttpdConf httpd_conf;
    private MimeTypes mime_types;
    private ServerSocket server;
    private Socket client;
    private int port;

    public static void main(String[] args) throws IOException {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    public void start() throws IOException{
        this.load_configs();
        this.port = Integer.parseInt(this.httpd_conf.get_httpd_conf("Listen"));
        this.test();
        try{
            server = new ServerSocket(port);
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    public void load_configs() {
        // TODO: Use relative pathing for this
        this.mime_types = new MimeTypes("conf/mime.types");
        this.httpd_conf = new HttpdConf("conf/httpd.conf");

    }

    public void test(){
        System.out.println("MIME TYPES HASHMAP\n========================");
        this.mime_types.print();
        String test_alias = "Listen";
        System.out.println("\n" + test_alias + ": " + this.port);

    }

}
