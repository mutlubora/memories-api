package com.memories.api.error;

import com.memories.api.auth.exception.AuthenticationException;
import com.memories.api.auth.exception.AuthorizationException;
import com.memories.api.user.exception.InvalidTokenException;
import com.memories.api.user.exception.NotUniqueUsernameException;
import com.memories.api.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotUniqueUsernameException.class)
    public ResponseEntity<?> handleNotUniqueUsernameException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handleAuthorizationException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(HttpServletRequest request, DisabledException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        errorResponse.setMessage("Validation error.");
        errorResponse.setStatus(status.value());
        errorResponse.setValidationErrors(new HashMap<>());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            errorResponse.addError(e.getField(), e.getDefaultMessage());
        });
        return new ResponseEntity<>(errorResponse, headers, status);
    }

}
