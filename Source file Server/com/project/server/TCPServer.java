package com.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.project.httpserver.HTTPServer;

public class TCPServer {


	public static void main(String argv[]) throws IOException 
	{ 
		HTTPServer httpServer=new HTTPServer();
		httpServer.start();
		
		ServerSocket welcomeSocket = new ServerSocket(8899);
		System.out.println("Server Started running on 8899 port no... ");

		while(true) {

			try{
				System.out.println("Waiting for client to connect..");
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Client connected..");
				
				new Thread(new RequestHandler(connectionSocket)).start();

			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		
		}
	} 
}
