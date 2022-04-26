package pl.coderslab.exception;

public class InvestityNotFoundException extends RuntimeException{
    public InvestityNotFoundException(long id){
        super("Could not found investity id:" + id);
    }
}

