package com.schemafactor.u64fileserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.schemafactor.rogueserver.common.JavaTools;

public class Main
{
    static byte[] fileContents = null;
    
    public static void main(String[] args)
    {
        int port = 3064;
        ServerSocket serverSocket = null;
        
        JavaTools.printlnTime("-----------------------------------------------");
        JavaTools.printlnTime("Ultimate 64 File Server Starting");

        // Parse command line parameters
        try
        {
            port = Integer.parseInt(args[0]);
            fileContents = Files.readAllBytes(Paths.get(args[1]));
            JavaTools.printlnTime("Read " + fileContents.length + " bytes from " + args[1]);
        }
        catch (Exception e) 
        {
            JavaTools.printlnTime("Exception: " + e.getMessage() );
            JavaTools.printlnTime("Invalid parameters.  Usage  java -jar u64fileserver <port> <filename>");
            return;
        }         

        //  Main loop.  Always loop and try to recover in case of exceptions
        while (true)   
        {  
            try 
            {
                serverSocket = new ServerSocket(port);
                JavaTools.printlnTime("Listening on port " + port);
                
                /* Loop Forever, waiting for connections. */  
                while(true) 
                {
                    Socket socket = serverSocket.accept();
                    send(socket);
                }
                
            } 
            catch (IOException e) 
            {
                JavaTools.printlnTime("EXCEPTION on accept(): " + e.getMessage());
            } 
            finally 
            {
                try 
                {
                    if (serverSocket != null)
                        serverSocket.close();
                } 
                catch (IOException e) 
                {
                    JavaTools.printlnTime("EXCEPTION on close(): " + e.getMessage());
                }
            }
                
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                ;
            }
            finally 
            {
                ;
            }
        }
    }

    public static void send(Socket socket) throws IOException 
    {
        JavaTools.printlnTime( "-----" );
        JavaTools.printlnTime( "Connection from " + socket.getRemoteSocketAddress().toString() );
        
        if (fileContents != null) 
        {            
            JavaTools.printlnTime( "Writing " + fileContents.length + " bytes" );
            socket.getOutputStream().write(fileContents);
            JavaTools.printlnTime( "Wrote " + fileContents.length + " bytes, closing connection" );            
        }
        else
        {
            JavaTools.printlnTime( "Error: File contents null, closing connection" );
        }
        
        socket.close();
        return;
    }
}