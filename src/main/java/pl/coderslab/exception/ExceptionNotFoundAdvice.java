package pl.coderslab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionNotFoundAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error entityNotFoundHandler(EntityNotFoundException e, HttpServletRequest request){
        Error error = new Error(404,e.getMessage(),request.getServletPath());
        return error;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error accessDeniedHandler(AccessDeniedException e, HttpServletRequest request){
        Error error = new Error(403, e.getMessage(),request.getServletPath());
        return error;
    }
}
