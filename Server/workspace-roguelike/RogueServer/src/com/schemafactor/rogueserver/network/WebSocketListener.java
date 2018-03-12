package com.schemafactor.rogueserver.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.clients.ClientWebSocket;

public class WebSocketListener extends WebSocketServer
{
    // Client List
    List<ClientWebSocket> wsClients = Collections.synchronizedList(new ArrayList<ClientWebSocket>());
    
    public WebSocketListener( int port ) 
    {
        super( new InetSocketAddress( port ) );    // throws UnknownHostException  ?
    }
    
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote )
    {
        ClientWebSocket which = findClient(conn);
        wsClients.remove(which);
        JavaTools.printlnTime("WebSocket connection closed: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() );
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        JavaTools.printlnTime("WebSocket EXCEPTION from: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() );
        JavaTools.printlnTime("WebSocket EXCEPTION: " + ex.getMessage() );
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        parseMessage(conn, message.getBytes());
    }
    
    @Override
    public void onMessage(WebSocket conn, ByteBuffer message ) 
    {
        parseMessage(conn, message.array());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake ) 
    {
        JavaTools.printlnTime("New WebSocket connection: " + handshake.getResourceDescriptor() + " from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() );        
        ClientWebSocket cws = new ClientWebSocket(conn);       
        wsClients.add(cws);
    }

    @Override
    public void onStart()
    {
        JavaTools.printlnTime("WebSocket Server started!");
    }
    
    private ClientWebSocket findClient(WebSocket conn)
    {
        for (ClientWebSocket cws : wsClients)
        {
            if (cws.getWebSocket() == conn)
            {
                return cws;
            }
        }
        
        return null;  // Not found
    }
    
    private void parseMessage(WebSocket conn, byte[] message)
    {
        ClientWebSocket which = findClient(conn);
        which.receiveUpdate(message);
    }
}
