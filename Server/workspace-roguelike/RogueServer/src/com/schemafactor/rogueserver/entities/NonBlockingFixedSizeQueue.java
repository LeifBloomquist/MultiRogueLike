package com.schemafactor.rogueserver.entities;

import java.util.concurrent.ArrayBlockingQueue;

// From http://crunchify.com/how-to-create-your-own-non-blocking-queue-in-java-same-as-evictingqueue/

/**
 * @author Crunchify.com Feel free to use this in your Enterprise Java Project
 */
 
public class NonBlockingFixedSizeQueue<E> extends ArrayBlockingQueue<E> {
 
    /**
     * generated serial number
     */
    private static final long serialVersionUID = -7772085623838075507L;
 
    // Size of the queue
    private int size;
 
    // Constructor
    public NonBlockingFixedSizeQueue(int crunchifySize) {
 
        // Creates an ArrayBlockingQueue with the given (fixed) capacity and default access policy
        super(crunchifySize);
        this.size = crunchifySize;
    }
 
    // If queue is full, it will remove oldest/first element from queue like FIFO
    // Do we need this add() method synchronize? What do you think?
    @Override
    synchronized public boolean add(E e) {
 
        // Check if queue full already?
        if (super.size() == this.size) {
            // remove element from queue if queue is full
            this.remove();
        }
        return super.add(e);
    }
    
    // Get a specific element, or null
    @SuppressWarnings("unchecked")
    public E elementAt(int index) {
        
      try {  
          E[] temp = (E[]) this.toArray();      
          return temp[index];
      }
      catch (Exception ex) {
          return null;
      }
    }
}