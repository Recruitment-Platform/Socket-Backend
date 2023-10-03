package com.project.socket.common.error;

import static com.project.socket.common.error.ErrorCode.INVALID_INPUT_VALUE;

import com.project.socket.common.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error("Exception : {}, Message : {}", ex.getClass(), ex.getMessage());
    ProblemDetail problemDetail = ProblemDetailFactory.of(
        INVALID_INPUT_VALUE, ex.getBindingResult());
    return ResponseEntity.badRequest().headers(headers).body(problemDetail);
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ProblemDetail> handleBusinessException(BusinessException e,
      HttpServletRequest request) {
    log.error("Exception : {}, Message : {}", e.getClass(), e.getMessage());
    ErrorCode errorCode = e.getErrorCode();
    return new ResponseEntity<>(
        ProblemDetailFactory.of(errorCode, request.getRequestURI()),
        HttpStatusCode.valueOf(errorCode.getStatus()));
  }
}
