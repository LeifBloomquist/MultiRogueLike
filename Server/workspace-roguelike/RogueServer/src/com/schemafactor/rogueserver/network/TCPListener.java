package com.schemafactor.rogueserver.network;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.HumanPlayer;
import com.schemafactor.rogueserver.entities.HumanPlayerTCP;
import com.schemafactor.rogueserver.universe.Dungeon;

public class TCPListener extends Thread
{
    private int port = 0;
    private ServerSocket serverSocket;
    private List<ClientThread> clientThreads = new ArrayList<ClientThread>();
    
    public void start(int port) 
    {
        this.port = port;
        start();
    }    
  
    @Override
    public void run()
    {
        Thread.currentThread().setName("Rogue TCP Listener Thread");
        
        while (true)   // Always loop and try to recover in case of exceptions
        {  
            try 
            {
                serverSocket = new ServerSocket(port);
                JavaTools.printlnTime("TCP server is listening on port " + port);
                
                /* Loop Forever, waiting for connections. */  
                while(true) 
                {
                    Socket socket = serverSocket.accept();
                    connect(socket);
                }
                
            } 
            catch (IOException e) 
            {
                JavaTools.printlnTime(e.getMessage());
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
                    JavaTools.printlnTime(e.getMessage());
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
        }
    } // run

    public void connect(Socket socket) 
    {
        try 
        {
            clientThreads.add(new ClientThread(socket));
        }
        catch (IOException e) 
        {
            JavaTools.printlnTime(e.getMessage());
        }
    }

    public void disconnect(ClientThread client) 
    {
        Iterator<ClientThread> itr = clientThreads.iterator();
        
        while(itr.hasNext()) 
        {
            if (itr.next().equals(client))
                itr.remove();
            break;
        }
    }

    // Inner Classes
    private class ClientThread extends Thread 
    {
        private Socket clientSocket;
        private BufferedReader input;
        private PrintWriter output;

        public ClientThread(Socket socket) throws IOException 
        {
            JavaTools.printlnTime( "TCP connection from " + socket.getRemoteSocketAddress().toString() + " established." );
            
            this.clientSocket = socket;
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(),true);
            start();
        }

        public void sendCharacters(String chars) 
        {
            output.print(chars);
            output.flush();
        }
        
        public void sendString(String message) 
        {
            output.println(message);
            output.flush();
        }

        public void close() 
        {           
            try {
                if(input != null)
                    input.close();
                if(output != null)
                    output.close();
                if(clientSocket != null)
                    clientSocket.close();
                disconnect(this);
            } catch (IOException e) {
                JavaTools.printlnTime(e.getMessage());
            }
        }

        
        public void run() 
        {
            Thread.currentThread().setName("TCP Client Thread");
            
            Dungeon dungeon = Dungeon.getInstance();  
            
            sendCharacters("\u001B[2J\u001B[H");
            sendString("Connected to the Rogue Test Server\n");
            
            // TODO Move all this to dedicated functions
            try 
            {   
                // Login Loop
                while(true) 
                {
                    // Wait for Login              
                    sendCharacters("Login: ");                    
                    String login = input.readLine();                    
                    login = JavaTools.Sanitize(login);                    
                    
                    if (login == null)
                    {
                        JavaTools.printlnTime( "TCP connection from " + clientSocket.getRemoteSocketAddress().toString() + " terminated." );
                        close();
                        return;
                    }
                    
                    if (login == "")
                    {
                        continue;
                    }
                    
                    sendString(login);
                    
                    // Create a player
                    // No match, create new user and add to list
                    JavaTools.printlnTime( "Creating player " + login + " from " + clientSocket.getRemoteSocketAddress().toString() + " [TCP]" );
                    HumanPlayer who = new HumanPlayerTCP(login, output);        
                    dungeon.addEntity(who);
                    break;   // To next loop
                }
                
                // Game Loop.  Updates occur in background.
                while (true)
                {
                    char c = (char)input.read();
                    sendString("Received: " + c);
                }              
            } 
            catch (IOException e) 
            {
                JavaTools.printlnTime(e.getMessage());
            }
            finally 
            {
                close();
            }
        }
    }
}