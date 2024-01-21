package com.onito.tambola.CustomException;

public class TicketIdNotFoundException extends RuntimeException{
    public TicketIdNotFoundException(String message)
    {
        super(message);
    }
}
