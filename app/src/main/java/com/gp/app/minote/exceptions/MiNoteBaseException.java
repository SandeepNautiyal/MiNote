package com.gp.app.minote.exceptions;

public class MiNoteBaseException extends Exception
{
	static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    public MiNoteBaseException() 
    {
        super();
    }

    
    public MiNoteBaseException(String message)
    {
        super(message);
    }

    
    public MiNoteBaseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public MiNoteBaseException(Throwable cause) 
    {
        super(cause);
    }
}
