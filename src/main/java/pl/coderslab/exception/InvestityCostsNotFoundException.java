package pl.coderslab.exception;

public class InvestityCostsNotFoundException extends RuntimeException{
    public InvestityCostsNotFoundException(long id){
        super("Could not found investity costs " + id);
    }
}
