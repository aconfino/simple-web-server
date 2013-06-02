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
	
	private static final String newline = System.getProperty("line.separator");
	private static Integer port = 8000;

    public static void main(String[] args) throws Exception {
    	System.out.println("Starting server...");
    	String portArgument = System.getProperty("port");
    	if (portArgument != null){
    		port = new Integer(portArgument);
    	}
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is ready.  Lisening of port " + port);
        System.out.println("");
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        	StringBuilder builder = new StringBuilder();
        	builder.append(newline);
        	builder.append("Protocol: " + t.getProtocol() + newline);
        	builder.append("Request URI: " + t.getRequestURI().toASCIIString() + newline);
        	builder.append(getHeaderInfo(t));
        	builder.append(newline);
        	InputStream requestBody = t.getRequestBody();
        	builder.append("Request body: " + IOUtils.toString(requestBody) + newline);
        	String response = builder.toString();
        	System.out.println(response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    public static String getHeaderInfo(HttpExchange t){
    	Headers headers = t.getRequestHeaders();
    	StringBuilder builder = new StringBuilder();
    	builder.append(printInfo(headers, "HOST"));
    	builder.append(printInfo(headers, "Accept-encoding"));
    	builder.append(printInfo(headers, "Connection"));
    	builder.append(printInfo(headers, "Accept-language"));
    	builder.append(printInfo(headers, "User-agent"));
    	builder.append(printInfo(headers, "Accept"));
    	return builder.toString();
    }
    
    public static String printInfo(Headers headers, String key){
    	if (headers.containsKey(key)){
    		return headers.get(key).toString() + newline;
    	} else
    		return "";
    }

}
