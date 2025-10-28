package com.devouk.devouk_back.member.controller;

import com.devouk.devouk_back.member.dto.MemberRequest;
import com.devouk.devouk_back.member.dto.MemberResponse;
import com.devouk.devouk_back.member.service.MemberAppService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private final MemberAppService memberAppService;

    public MemberController(MemberAppService memberAppService) {
        this.memberAppService = memberAppService;
    }

    @PostMapping
    public MemberResponse createMember(@RequestBody @Valid MemberRequest request) {
        return memberAppService.createMember(request);
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        return memberAppService.getMember(id);
    }

    @GetMapping
    public List<MemberResponse> getAllMembers() {
        return memberAppService.getAllMembers();
    }
}
