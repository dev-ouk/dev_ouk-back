package com.devouk.devouk_back.error;

import com.devouk.devouk_back.domain.common.exception.BusinessException;
import com.devouk.devouk_back.domain.common.exception.DuplicateMemberException;
import com.devouk.devouk_back.domain.common.exception.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<ErrorResponse.FieldErrorDetail> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fe ->
                    new ErrorResponse.FieldErrorDetail(
                        fe.getField(), fe.getCode(), fe.getDefaultMessage()))
            .toList();

    ErrorResponse body =
        ErrorResponse.of(
            request.getRequestURI(), "Validation Failed", correlationId(), fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {
    List<ErrorResponse.FieldErrorDetail> fieldErrors =
        ex.getConstraintViolations().stream()
            .map(this::toFieldErrorDetail)
            .collect(Collectors.toList());

    ErrorResponse body =
        ErrorResponse.of(
            request.getRequestURI(), "Validation Failed", correlationId(), fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  private ErrorResponse.FieldErrorDetail toFieldErrorDetail(ConstraintViolation<?> v) {
    return new ErrorResponse.FieldErrorDetail(
        v.getPropertyPath() != null ? v.getPropertyPath().toString() : null,
        v.getConstraintDescriptor() != null
            ? v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
            : null,
        v.getMessage());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(
      BindException ex, HttpServletRequest request) {
    List<ErrorResponse.FieldErrorDetail> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fe ->
                    new ErrorResponse.FieldErrorDetail(
                        fe.getField(), fe.getCode(), fe.getDefaultMessage()))
            .toList();

    ErrorResponse body =
        ErrorResponse.of(
            request.getRequestURI(), "Invalid request binding", correlationId(), fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleNotReadable(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    ErrorResponse body =
        ErrorResponse.of(
            request.getRequestURI(), "Malformed request body", correlationId(), List.of());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(
      BusinessException ex, HttpServletRequest request) {
    HttpStatus status = mapBusinessExceptionToStatus(ex);

    ErrorResponse body =
        ErrorResponse.of(request.getRequestURI(), ex.getMessage(), correlationId(), List.of());

    return ResponseEntity.status(status).body(body);
  }

  private HttpStatus mapBusinessExceptionToStatus(BusinessException ex) {
    if (ex instanceof MemberNotFoundException) {
      return HttpStatus.NOT_FOUND;
    }
    if (ex instanceof DuplicateMemberException) {
      return HttpStatus.CONFLICT;
    }
    return HttpStatus.BAD_REQUEST;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleOtherExceptions(
      Exception ex, HttpServletRequest request) {
    ErrorResponse body =
        ErrorResponse.of(
            request.getRequestURI(), "Internal server error", correlationId(), List.of());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  private String correlationId() {
    String cid = MDC.get("correlationId");
    return cid != null ? cid : "";
  }
}
