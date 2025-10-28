package com.devouk.devouk_back.domain.member;

import com.devouk.devouk_back.domain.common.exception.DuplicateMemberException;
import com.devouk.devouk_back.domain.common.exception.MemberNotFoundException;
import java.util.List;

public class MemberServiceImpl implements MemberService {

  private final MemberRepositoryPort memberRepository;

  public MemberServiceImpl(MemberRepositoryPort memberRepository) {
    this.memberRepository = memberRepository;
  }

  public Member createMember(String email, String name) {
    if (memberRepository.existsByEmail(email)) {
      throw new DuplicateMemberException(email);
    }
    Member member = new Member(email, name);
    return memberRepository.save(member);
  }

  public Member getMember(Long id) {
    return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
  }

  public List<Member> getAllMembers() {
    return memberRepository.findAll();
  }
}
