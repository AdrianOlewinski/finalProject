package pl.coderslab.exception;


public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(long id){
        super("Could not found entity id=" + id);
    }
    public EntityNotFoundException(String login){
        super("Could not found entity login=" + login);
    }
}
