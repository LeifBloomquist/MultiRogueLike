package com.schemafactor.rogueserver.common.interfaces;

import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.items.Item;

public interface Container
{
    public byte getSeenCharCode();             // When directly on top of this container, what is seen?
    public Item getItem();                     // Get a pointer to the contained item
    public Item takeItem(Entity who);          // Get a pointer to the contained item and set internal Item to null if not empty
    public boolean setContainedItem(Item item);// Set contained item to new Item (destroys any Item contained) (returns true on success)
    public boolean placeItem(Item item);       // If possible, place an Item in this container (returns true on success) 
}
