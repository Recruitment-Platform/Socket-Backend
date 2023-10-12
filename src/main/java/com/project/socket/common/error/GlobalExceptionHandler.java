package com.project.socket.common.error;

import static com.project.socket.common.error.ErrorCode.INVALID_INPUT_VALUE;

import com.project.socket.common.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

  private static final String DEFAULT_LOG_MESSAGE = "Exception : {}, Message : {}";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error(DEFAULT_LOG_MESSAGE, ex.getClass(), ex.getMessage());
    ProblemDetail problemDetail = ProblemDetailFactory.of(
        INVALID_INPUT_VALUE, ex.getBindingResult());
    return ResponseEntity.badRequest().headers(headers).body(problemDetail);
  }

  /**
   * javax.validation.ConstraintViolationException 으로 제약조건을 위반할 때 발생한다. 주로 @Min, @Max 등 제약을 두는
   * 어노테이션에서 발생
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ProblemDetail> handleConstraintViolationException(
      ConstraintViolationException e, HttpServletRequest request) {
    log.error(DEFAULT_LOG_MESSAGE, e.getClass(), e.getMessage());
    return new ResponseEntity<>(
        ProblemDetailFactory.of(INVALID_INPUT_VALUE, request.getRequestURI()),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ProblemDetail> handleBusinessException(BusinessException e,
      HttpServletRequest request) {
    log.error(DEFAULT_LOG_MESSAGE, e.getClass(), e.getMessage());
    ErrorCode errorCode = e.getErrorCode();
    return new ResponseEntity<>(
        ProblemDetailFactory.of(errorCode, request.getRequestURI()),
        HttpStatusCode.valueOf(errorCode.getStatus()));
  }
}
