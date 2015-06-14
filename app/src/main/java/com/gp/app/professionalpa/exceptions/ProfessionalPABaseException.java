package com.gp.app.professionalpa.exceptions;

public class ProfessionalPABaseException extends Exception
{
	static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    public ProfessionalPABaseException() 
    {
        super();
    }

    
    public ProfessionalPABaseException(String message)
    {
        super(message);
    }

    
    public ProfessionalPABaseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    
    public ProfessionalPABaseException(Throwable cause) 
    {
        super(cause);
    }
}
