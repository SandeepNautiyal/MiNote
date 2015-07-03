package com.gp.app.minote.exceptions;

public class MiNoteRuntimeException extends RuntimeException
{
	static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    public MiNoteRuntimeException() 
    {
        super();
    }

    
    public MiNoteRuntimeException(String message)
    {
        super(message);
    }

    
    public MiNoteRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public MiNoteRuntimeException(Throwable cause) 
    {
        super(cause);
    }
}
