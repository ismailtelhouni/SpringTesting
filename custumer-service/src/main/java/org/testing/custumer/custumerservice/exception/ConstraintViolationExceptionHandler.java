package org.testing.custumer.custumerservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ConstraintViolationException exception){
//        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
//        Map<String, List<String>> listMap=new HashMap<>();
//        constraintViolations.forEach(cv->{
//            listMap.computeIfAbsent(cv.getPropertyPath().toString(), k -> new ArrayList<>());
//            listMap.get(cv.getPropertyPath().toString()).add(cv.getMessage());
//        });
//        return ResponseEntity.badRequest().body(listMap);
//    }

    @ExceptionHandler(value = { CustomerNotFoundException.class })
    public ResponseEntity<Object> entityNotFoundException( CustomerNotFoundException ex ) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .code(404)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { EmailAlreadyExistException.class })
    public ResponseEntity<Object> emailAlreadyExistException( EmailAlreadyExistException ex ) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(ex.getMessage())
                .timestamp(new Date())
                .code(400)
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> HandlerMethodArgumentNotValid( MethodArgumentNotValidException ex ){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach( error->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors,new HttpHeaders(),HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
