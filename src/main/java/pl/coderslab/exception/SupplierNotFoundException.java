package pl.coderslab.exception;

public class SupplierNotFoundException extends RuntimeException{
    public SupplierNotFoundException(long id){
        super("Could not found supplier " + id);
    }
}
