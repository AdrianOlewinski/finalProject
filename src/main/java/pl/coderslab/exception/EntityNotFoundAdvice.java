package pl.coderslab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class EntityNotFoundAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error entityNotFoundHandler(EntityNotFoundException e, HttpServletRequest request){
        Error error = new Error(404,e.getMessage(),request.getServletPath());
        return error;
    }
}
