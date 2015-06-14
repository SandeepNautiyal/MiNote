package com.gp.app.professionalpa.exceptions;

public class ProfessionPARuntimeException extends RuntimeException
{
	static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    public ProfessionPARuntimeException() 
    {
        super();
    }

    
    public ProfessionPARuntimeException(String message)
    {
        super(message);
    }

    
    public ProfessionPARuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public ProfessionPARuntimeException(Throwable cause) 
    {
        super(cause);
    }
}
