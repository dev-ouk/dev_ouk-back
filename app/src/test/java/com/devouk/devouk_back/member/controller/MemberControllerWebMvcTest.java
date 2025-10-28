package com.devouk.devouk_back.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devouk.devouk_back.domain.common.exception.MemberNotFoundException;
import com.devouk.devouk_back.member.dto.MemberRequest;
import com.devouk.devouk_back.member.dto.MemberResponse;
import com.devouk.devouk_back.member.service.MemberAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean MemberAppService memberAppService;

  @Test
  @DisplayName("POST /members - 유효한 요청이면 200과 MemberResponse를 반환한다")
  void createMember_success() throws Exception {
    MemberResponse fakeResponse = new MemberResponse(1L, "valid@test.com", "상욱");
    BDDMockito.given(memberAppService.createMember(any(MemberRequest.class)))
        .willReturn(fakeResponse);

    String body =
        """
                {
                    "email": "valid@test.com",
                    "name": "상욱"
                }
                """;

    mockMvc
        .perform(
            post("/members")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.email").value("valid@test.com"))
        .andExpect(jsonPath("$.name").value("상욱"));
  }

  @Test
  @DisplayName("POST /members - 잘못된 DTO면 400과 errors 배열이 내려온다")
  void createMember_validationFail_returns400() throws Exception {
    String invalidBody =
        """
            {
              "email": "not-an-email",
              "name": ""
            }
            """;

    mockMvc
        .perform(
            post("/members")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation Failed"))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors.length()").value(2))
        .andExpect(jsonPath("$.errors[0].field").exists())
        .andExpect(jsonPath("$.errors[0].errorMessage").exists())
        .andExpect(jsonPath("$.correlationId").exists());
  }

  @Test
  @DisplayName("GET /members/{id} - 없는 멤버면 404와 에러 메시지를 반환한다")
  void getMember_notFound_returns404() throws Exception {
    long missingId = 999L;
    BDDMockito.given(memberAppService.getMember(missingId))
        .willThrow(new MemberNotFoundException(missingId));

    mockMvc
        .perform(get("/members/{id}", missingId).with(user("testUser")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Member not found with id: 999"))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.correlationId").exists());
  }

  @Test
  @DisplayName("GET /members - 전체 목록을 반환한다")
  void getAllMembers_returnsList() throws Exception {
    var r1 = new MemberResponse(1L, "a@a.com", "A");
    var r2 = new MemberResponse(2L, "b@b.com", "B");
    BDDMockito.given(memberAppService.getAllMembers()).willReturn(List.of(r1, r2));

    mockMvc
        .perform(get("/members").with(user("testUser")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].email").value("a@a.com"))
        .andExpect(jsonPath("$[0].name").value("A"))
        .andExpect(jsonPath("$[1].id").value(2L));
  }
}
