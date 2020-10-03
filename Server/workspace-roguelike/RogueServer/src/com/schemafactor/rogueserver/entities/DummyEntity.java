package com.schemafactor.rogueserver.entities;

public class DummyEntity extends Entity
{  
	private static final long serialVersionUID = 1L;

	/** Creates a new instance */
    public DummyEntity()
    {
        super("Dummy", null, entityTypes.NONE, (byte)0, 0f, 0f);
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