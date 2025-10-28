package com.devouk.devouk_back.domain.member;

import java.util.List;

public interface MemberService {

  Member createMember(String email, String name);

  Member getMember(Long id);

  List<Member> getAllMembers();
}
