package com.devouk.devouk_back.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

public class CorrelationIdFilterTest {

  private CorrelationIdFilter filter;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    filter = new CorrelationIdFilter();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
  }

  @AfterEach
  void clearMdc() {
    MDC.clear();
  }

  @Test
  void doFilterInternal_shouldGenerateNewCorrelationId_whenHeaderMissing()
      throws ServletException, IOException {

    when(request.getHeader("X-Correlation-ID")).thenReturn(null);

    doAnswer(
            invocation -> {
              String idInside = MDC.get("correlationId");
              assertThat(idInside).isNotBlank().hasSizeGreaterThan(10);
              return null;
            })
        .when(filterChain)
        .doFilter(request, response);
    filter.doFilterInternal(request, response, filterChain);

    verify(response).setHeader(eq("X-Correlation-ID"), anyString());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void doFilterInternal_shouldUseExistingCorrelationId_whenHeaderPresent()
      throws ServletException, IOException {

    when(request.getHeader("X-Correlation-ID")).thenReturn("test-correlation-id");

    doAnswer(
            invocation -> {
              String idInside = MDC.get("correlationId");
              assertThat(idInside).isEqualTo("test-correlation-id");
              return null;
            })
        .when(filterChain)
        .doFilter(request, response);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).setHeader("X-Correlation-ID", "test-correlation-id");
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void doFilterInternal_shouldRemoveMdcAfterExecution() throws ServletException, IOException {

    when(request.getHeader("X-Correlation-ID")).thenReturn("remove-test-id");

    filter.doFilterInternal(request, response, filterChain);

    assertThat(MDC.get("correlationId")).isNull();
  }
}
