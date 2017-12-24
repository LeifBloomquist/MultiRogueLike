package com.schemafactor.rogueserver.network;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.clients.HumanPlayerTCP;
import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.universe.Dungeon;

public class TCPListener extends Thread
{
    private int port = 0;
    private ServerSocket serverSocket;
    private List<ClientThread> clientThreads = new ArrayList<ClientThread>();
    
    private enum ExitReason { NONE,  DIED, DISCONNECTED, QUIT }
    
    // Force remote Telnet client to not use linemode (i.e. character mode), and to echo.  Adapted from http://www.mudbytes.net/forum/comment/56126/
    private final byte[] telnet_params = 
        {
          (byte) 255,      // NVT_IAC
          (byte) 251,      // WILL
          (byte) 3,        // Suppress Go Ahead
          (byte) 255,      // NVT_IAC
          (byte) 251,      // WILL
          (byte) 1         // Echo
        };
    
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
        private OutputStream output_stream;

        public ClientThread(Socket socket) throws IOException 
        {
            JavaTools.printlnTime( "TCP connection from " + socket.getRemoteSocketAddress().toString() + " established." );
            
            this.clientSocket = socket;
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output_stream = clientSocket.getOutputStream();
            output = new PrintWriter(new OutputStreamWriter(output_stream, StandardCharsets.UTF_8), true);
            start();
        }

        public void sendCharacters(byte[] bytes) 
        {
            try
            {
                output_stream.write(bytes);
                output.flush();
            } 
            catch (IOException e)
            {                
                JavaTools.printlnTime( "EXCEPTION writing to TCP stream:  " + e.getMessage() );
            }           
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
            sendCharacters(telnet_params);
            
            try            
            {  
                while (true) // Infinite cycle of life and death
                {                    
                    // 1. Login
                    HumanPlayerTCP who = doLogin();                
                    if (who == null)
                    {
                        return;
                    }                
                    dungeon.addEntity(who);
                    
                    // 2. Game Loop - Blocks until user disconnects or dies
                    ExitReason exit = gameLoop(who);   
                    who.removeMe();                
                    
                    if (exit == ExitReason.DISCONNECTED)
                    {
                        return;
                    }                    
    
                    // 3. Play again?
                    if (playAgain())
                    {
                        continue;
                    }
                    else
                    {                     
                        return;
                    }   
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

        private HumanPlayerTCP doLogin() throws IOException
        {
            // Login Loop
            while (true) 
            {
                sendCharacters(Constants.ANSI_CLEAR);
                sendString("Connected to the Rogue Test Server");
                sendString("Server version: " + Double.toString(Constants.VERSION) );
                
                // Wait for Login              
                sendString("");
                sendCharacters("Login: ");                    
                String login = input.readLine();
                
                if (login == null)
                {
                    JavaTools.printlnTime( "TCP connection from " + clientSocket.getRemoteSocketAddress().toString() + " terminated at login." );
                    close();
                    return null;
                }

                login = JavaTools.Sanitize(login);       
                
                if (login.equals(""))
                {
                    continue;
                }
                
                sendString(login);  // Echo back
                
                // Create a player
                JavaTools.printlnTime( "Creating player " + login + " from " + clientSocket.getRemoteSocketAddress().toString() + " [TCP]" );
                return new HumanPlayerTCP(login, output);
            }
        }

        // Game Loop.  Updates occur in background thread controlled by UpdaterThread.
        private ExitReason gameLoop(HumanPlayerTCP who) throws IOException
        {   
            who.addMessage("Welcome to the Rogue Test Server");
            who.addMessage("Server version: " + Double.toString(Constants.VERSION) );
            who.addMessage("Test Message #3 ");
            
            while (true)
            {             
                int ic = input.read();  // Blocks
                
                if (ic < 0)
                {
                    JavaTools.printlnTime( "TCP connection from " + clientSocket.getRemoteSocketAddress().toString() + " terminated waiting for input." );
                    close();
                    return ExitReason.DISCONNECTED;
                }                    
                            
                JavaTools.printlnTime("DEBUG: Received: " + (char)ic + " | " + ic + " from " + who.getDescription());                  
                who.handleKeystroke(ic);
                
                // Exit for removed entities
                if (who.getRemoved())
                {
                   return ExitReason.DIED;
                }
            }
        }
        
        // "Play Again?" loop.  Return true on Yes, false on No.
        public boolean playAgain() throws IOException
        {
            sendCharacters(Constants.ANSI_CLEAR);
            
            while (true)
            {               
                sendString("You have died!  Play again?  (y/n)");
                
                int ic = input.read();
                
                if (ic < 0)
                {
                    JavaTools.printlnTime( "TCP connection from " + clientSocket.getRemoteSocketAddress().toString() + " terminated at Play Again prompt." );             
                    close();
                    return false;   // We'll take that as a No
                }
                
                switch (ic)
                {
                    case 'y':
                    case 'Y':
                        return true;
                        
                    case 'n':
                    case 'N':  
                        close();
                        return false;
                        
                    // Default keep asking
                }                       
            }                    
        }
    }

   
}