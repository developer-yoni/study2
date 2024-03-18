package com.example.api.global.advice;

import com.example.api.global.response.ApiException;
import com.example.api.global.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalAdvice extends ResponseEntityExceptionHandler {

    /**
     * [우리가 정의한 API Exception 상황 발생시에] -> 내부 세부 Status에 따른 실패 응답
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(ApiException e) {

        log.error("EXCEPTION = {}, MESSAGE = {}, INTERNAL_MESSAGE = {}", e, e.getApiCode().getMessage(), e.getInternalMessage(), e);
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getApiCode()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
                                                                         HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
                                                                     HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers,
                                                                      HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status,
                                                               WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
                                                                          HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers,
                                                                     HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers,
                                                                          HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBeanValidation(ex.getBindingResult()));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status,
                                                                   WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status,
                                                                        WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForInternalServerError(ex));
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ApiResponse.failForInternalServerError(ex));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.failForBasicException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ApiResponse.failForInternalServerError(ex));
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode,
                                                             WebRequest request) {

        log.error("Exception = {}, message = {}", ex, ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ApiResponse.failForInternalServerError(ex));
    }
}