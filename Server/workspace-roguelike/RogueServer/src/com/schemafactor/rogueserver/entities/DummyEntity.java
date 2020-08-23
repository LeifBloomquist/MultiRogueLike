package com.schemafactor.rogueserver.entities;

import com.schemafactor.rogueserver.common.Position;

public class DummyEntity extends Entity
{  
    /** Creates a new instance */
    public DummyEntity(Position startposition)
    {
       super("Dummy", startposition, entityTypes.NONE, (byte)0, 0f);
    }
    
    @Override
	public void action() 
	{	
		;	
	}   

	@Override
	public void update() 
	{	
		;	
	}   
	
	@Override
    public void updateNow()
    {
       ;        
    }

    @Override
    public void addMessage(String msg)
    {
        ;        
    }
    
    @Override
    public void playSound(byte id)
    {
        ;        
    }
}