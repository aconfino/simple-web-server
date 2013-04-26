package com.confino.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleServer {

    public static void main(String[] args) throws Exception {
    	System.out.println("Starting server...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is ready.");
        System.out.println("");
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        	System.out.println("");
        	System.out.println("Protocol: " + t.getProtocol());
        	System.out.println("Request URI: " + t.getRequestURI().toASCIIString());
        	getHeaderInfo(t);
        	System.out.println("");
        	InputStream requestBody = t.getRequestBody();
        	System.out.println("Request body: " + IOUtils.toString(requestBody));
        	String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    public static void getHeaderInfo(HttpExchange t){
    	Headers headers = t.getRequestHeaders();
    	printInfo(headers, "HOST");
    	printInfo(headers, "Accept-encoding");
    	printInfo(headers, "Connection");
    	printInfo(headers, "Accept-language");
    	printInfo(headers, "User-agent");
    	printInfo(headers, "Accept");
    }
    
    public static void printInfo(Headers headers, String key){
    	if (headers.containsKey(key)){
    		System.out.println(headers.get(key).toString());
    	}
    }

}
