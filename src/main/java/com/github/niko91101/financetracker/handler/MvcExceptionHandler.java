package com.github.niko91101.financetracker.handler;

import com.github.niko91101.financetracker.exception.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(1)
public class MvcExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "Отсутствует обязательный параметр: " + ex.getParameterName(),
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                Map.of()
        );
        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.toList()
                        )
                ));

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "Ошибка валидации",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                fieldErrors
        );

        return new ResponseEntity<>(error, headers, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Некорректное значение параметра: " + e.getName(),
                request.getMethod(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "JSON некорректно сформирован",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                Map.of()
        );

        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "Метод не поддерживается",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                Map.of()
        );
        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "Неподдерживаемый Content-Type",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                Map.of()
        );

        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        HttpServletRequest httpRequest = servletWebRequest.getRequest();

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                "Ошибка валидации параметров запроса",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                Map.of()
        );
        return new ResponseEntity<>(error, headers, status);
    }
}
