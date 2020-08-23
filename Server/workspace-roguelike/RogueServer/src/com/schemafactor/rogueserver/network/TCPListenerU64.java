package com.schemafactor.rogueserver.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.PETSCII;
import com.schemafactor.rogueserver.entities.clients.Client;
import com.schemafactor.rogueserver.entities.clients.ClientU64;
import com.schemafactor.rogueserver.universe.Dungeon;

// TODO, this class shares a ton of code with TCPListenerU64, refactor!

public class TCPListenerU64 extends Thread
{
    private int port = 0;
    private ServerSocket serverSocket;
    private List<ClientThread> clientThreads = new ArrayList<ClientThread>();
    
    private enum ExitReason { NONE,  DIED, DISCONNECTED, QUIT }
    
    public void start(int port) 
    {
        this.port = port;
        start();
    }    
  
    @Override
    public void run()
    {
        Thread.currentThread().setName("Rogue TCP Listener Thread [Ultimate 64]");
        
        while (true)   // Always loop and try to recover in case of exceptions
        {  
            try 
            {
                serverSocket = new ServerSocket(port);
                JavaTools.printlnTime("TCP server [Ultimate 64] is listening on port " + port);
                
                /* Loop Forever, waiting for connections. */  
                while(true) 
                {
                    Socket socket = serverSocket.accept();
                    connect(socket);
                }
                
            } 
            catch (IOException e) 
            {
                JavaTools.printlnTime("EXCEPTION: " + e.getMessage());
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
                    JavaTools.printlnTime("EXCEPTION: " + e.getMessage());
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
        private DataInputStream  input;
        private DataOutputStream output;

        public ClientThread(Socket socket) throws IOException 
        {
            JavaTools.printlnTime( "TCP [Ultimate 64] connection from " + socket.getRemoteSocketAddress().toString() + " established." );
            
            this.clientSocket = socket;
            input  = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());
            start();
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
        
        private byte[] receiveChars(int num)
        {
            byte[] buf = new byte[num];     
            
            try
            {
                input.readFully(buf);
                return buf;
            } 
            catch (EOFException e)
            {
                return null;  // End of stream
            } 
            catch (IOException e)
            {
                JavaTools.printlnTime("EXCEPTION receiving data from U64: " + JavaTools.getStackTrace(e));
                return null;
            }
        }        
        
        public void run() 
        {
            Thread.currentThread().setName("TCP Client Thread [Ultimate 64]");            
            Dungeon dungeon = Dungeon.getInstance();
                      
            try            
            {
                while (true) // Infinite cycle of life and death
                {
                    // 1. Wait for an announce packet
                    byte[] announce = receiveChars(17);
                    
                    if (announce == null) return;  // Disconnected or other error
                    
                    // Workaround for U64 - null bytes terminate the packet.
                    byte[] announce_fixed = new byte[announce.length];
                    
                    for (int i=0; i<announce.length; i++)
                    {
                        if (announce[i] == -1)
                        {
                            announce_fixed[i] = 0;
                        }
                        else
                        {
                            announce_fixed[i] = announce[i];
                        }
                    }
                    
                    ClientU64 u64 = new ClientU64(announce_fixed, output);
                    dungeon.addEntity(u64);
                    
                    // 2. Game Loop - Blocks until user disconnects or dies
                    ExitReason exit = gameLoop(u64);   
                    u64.removeMe();                
                    
                    if (exit == ExitReason.DISCONNECTED)
                    {
                        return;
                    }                    
    
                    // 3. Play again?
                    if (true) //playAgain())
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
                JavaTools.printlnTime("EXCEPTION in TCP U64 Listener" + e.getMessage());
            }
            finally 
            {
                close();
            }
        }
        
        // Game Loop.  Updates occur in background thread controlled by UpdaterThread.
        private ExitReason gameLoop(Client who) throws IOException
        {            
            while (true)
            {             
                int ic = input.read();  // Blocks
                
                if (ic < 0)
                {
                    JavaTools.printlnTime( "TCP connection from " + clientSocket.getRemoteSocketAddress().toString() + " terminated waiting for input." );
                    close();
                    return ExitReason.DISCONNECTED;
                }
                
                byte petscii_char = (byte)ic;
                byte ascii_char = PETSCII.toASCII(petscii_char);     
                who.handleKeystroke(ascii_char);
                
                // Exit for removed entities
                if (who.getRemoved())
                {
                   return ExitReason.DIED;
                }
            }
        }        
    }   
}