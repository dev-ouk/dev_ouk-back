package com.devouk.devouk_back.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryPort {

  Member save(Member member);

  Optional<Member> findById(Long id);

  List<Member> findAll();

  boolean existsByEmail(String email);
}
