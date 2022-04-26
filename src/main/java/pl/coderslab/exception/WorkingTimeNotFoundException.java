package pl.coderslab.exception;

public class WorkingTimeNotFoundException extends RuntimeException{
    public WorkingTimeNotFoundException(long id){
        super("Could not find working time: " + id);
    }
}
