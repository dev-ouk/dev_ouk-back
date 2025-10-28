package com.devouk.devouk_back.member.service;

import com.devouk.devouk_back.domain.member.Member;
import com.devouk.devouk_back.domain.member.MemberService;
import com.devouk.devouk_back.member.dto.MemberRequest;
import com.devouk.devouk_back.member.dto.MemberResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberAppService {

  private final MemberService memberService;

  public MemberAppService(MemberService memberService) {
    this.memberService = memberService;
  }

  public MemberResponse createMember(MemberRequest request) {
    Member member = memberService.createMember(request.getEmail(), request.getName());
    return MemberResponse.from(member);
  }

  @Transactional(readOnly = true)
  public MemberResponse getMember(Long id) {
    return MemberResponse.from(memberService.getMember(id));
  }

  @Transactional(readOnly = true)
  public List<MemberResponse> getAllMembers() {
    return memberService.getAllMembers().stream().map(MemberResponse::from).toList();
  }
}
