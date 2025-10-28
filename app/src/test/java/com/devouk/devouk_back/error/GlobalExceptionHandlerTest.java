package com.devouk.devouk_back.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.devouk.devouk_back.domain.common.exception.BusinessException;
import com.devouk.devouk_back.domain.common.exception.DuplicateMemberException;
import com.devouk.devouk_back.domain.common.exception.MemberNotFoundException;
import com.devouk.devouk_back.member.dto.MemberRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class GlobalExceptionHandlerTest {

  GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @AfterEach
  void clearMdc() {
    MDC.clear();
  }

  private HttpServletRequest mockRequest(String uri) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(request.getRequestURI()).willReturn(uri);
    return request;
  }

  static class DummyController {
    public void dummyMethod(@Valid MemberRequest request) {}
  }

  private MethodArgumentNotValidException buildMethodArgumentNotValidException()
      throws NoSuchMethodException {
    Method m = DummyController.class.getMethod("dummyMethod", MemberRequest.class);
    MethodParameter methodParameter = new MethodParameter(m, 0);

    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(new MemberRequest(), "memberRequest");
    bindingResult.rejectValue("email", "Email", "올바른 이메일 형식이어야 합니다.");
    bindingResult.rejectValue("name", "NotBlank", "이름은 필수입니다.");

    return new MethodArgumentNotValidException(methodParameter, bindingResult);
  }

  static class Dummy {
    private Long id;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  private BindException buildBindException() {
    BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Dummy(), "dummy");
    br.rejectValue("id", "typeMismatch", "정수가 아닙니다.");
    return new BindException(br);
  }

  @interface DummyConstraint {}

  @SuppressWarnings({"unchecked", "rawtypes"})
  private ConstraintViolationException buildConstraintViolationException() {
    ConstraintViolation<?> v1 = mock(ConstraintViolation.class);

    given(v1.getPropertyPath()).willReturn(null);
    given(v1.getConstraintDescriptor()).willReturn(null);
    given(v1.getMessage()).willReturn("파라미터가 잘못됐습니다.");

    ConstraintViolation<?> v2 = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    given(path.toString()).willReturn("id");

    @SuppressWarnings("unchecked")
    ConstraintDescriptor descriptor = mock(ConstraintDescriptor.class);

    Annotation dummyAnno =
        new Annotation() {
          @Override
          public Class<? extends Annotation> annotationType() {
            return DummyConstraint.class;
          }
        };
    given(descriptor.getAnnotation()).willReturn(dummyAnno);

    given(v2.getPropertyPath()).willReturn(path);
    given(v2.getConstraintDescriptor()).willReturn(descriptor);
    given(v2.getMessage()).willReturn("id는 양수여야 합니다.");

    Set<ConstraintViolation<?>> set = new LinkedHashSet<>(List.of(v1, v2));
    return new ConstraintViolationException("invalid params", set);
  }

  @Test
  void handleMethodArgumentNotValid_returns400_andFieldErrors() throws Exception {
    MethodArgumentNotValidException ex = buildMethodArgumentNotValidException();
    HttpServletRequest req = mockRequest("/members");

    ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValid(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).isEqualTo("Validation Failed");
    assertThat(body.getPath()).isEqualTo("/members");
    assertThat(body.getErrors()).hasSize(2);
  }

  @Test
  void handleConstraintViolation_returns400_andFieldErrors() {
    ConstraintViolationException ex = buildConstraintViolationException();
    HttpServletRequest req = mockRequest("/members");

    ResponseEntity<ErrorResponse> response = handler.handleConstraintViolation(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).isEqualTo("Validation Failed");
    assertThat(body.getErrors()).hasSize(2);

    assertThat(body.getErrors()).extracting("field").containsExactlyInAnyOrder(null, "id");
    assertThat(body.getErrors())
        .filteredOn("field", "id")
        .extracting("code")
        .containsExactly("DummyConstraint");
  }

  @Test
  void handleBindException_returns400() {
    BindException ex = buildBindException();
    HttpServletRequest req = mockRequest("/members");

    ResponseEntity<ErrorResponse> response = handler.handleBindException(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).isEqualTo("Invalid request binding");
    assertThat(body.getErrors()).hasSize(1);
    assertThat(body.getErrors().get(0).getField()).isEqualTo("id");
  }

  @Test
  void handleNotReadable_returns400() {
    HttpMessageNotReadableException ex =
        new HttpMessageNotReadableException("bad body", (Throwable) null, null);

    HttpServletRequest req = mockRequest("/members");

    ResponseEntity<ErrorResponse> response = handler.handleNotReadable(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).isEqualTo("Malformed request body");
    assertThat(body.getErrors()).isEmpty();
  }

  @Test
  void handleBusiness_duplicate_returns409_andCorrelationIdUsed() {
    MDC.put("correlationId", "test-cid-123");

    BusinessException ex = new DuplicateMemberException("dup@test.com");
    HttpServletRequest req = mockRequest("/members");

    ResponseEntity<ErrorResponse> response = handler.handleBusiness(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(409);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getCorrelationId()).isEqualTo("test-cid-123");
    assertThat(body.getMessage()).contains("dup@test.com");
  }

  @Test
  void handleBusiness_notFound_returns404() {
    BusinessException ex = new MemberNotFoundException(999L);
    HttpServletRequest req = mockRequest("/members/999");

    ResponseEntity<ErrorResponse> response = handler.handleBusiness(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(404);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getPath()).isEqualTo("/members/999");
    assertThat(body.getMessage()).contains("999");
  }

  static class SomeOtherBusinessException extends BusinessException {
    public SomeOtherBusinessException(String message) {
      super(message);
    }
  }

  @Test
  void handleBusiness_otherBusinessException_returns400() {
    BusinessException ex = new SomeOtherBusinessException("weird");
    HttpServletRequest req = mockRequest("/weird");

    ResponseEntity<ErrorResponse> response = handler.handleBusiness(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(400);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getMessage()).isEqualTo("weird");
  }

  @Test
  void handleOtherExceptions_returns500_andCorrelationIdEmptyIfMdcMissing() {
    Exception ex = new RuntimeException("boom");
    HttpServletRequest req = mockRequest("/crash");

    ResponseEntity<ErrorResponse> response = handler.handleOtherExceptions(ex, req);

    assertThat(response.getStatusCode().value()).isEqualTo(500);
    ErrorResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getMessage()).isEqualTo("Internal server error");
    assertThat(body.getCorrelationId()).isEqualTo("");
    assertThat(body.getErrors()).isEmpty();
  }
}
