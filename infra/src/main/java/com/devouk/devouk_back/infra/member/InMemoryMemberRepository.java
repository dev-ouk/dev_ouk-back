package com.devouk.devouk_back.infra.member;

import com.devouk.devouk_back.domain.member.Member;
import com.devouk.devouk_back.domain.member.MemberRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberRepository implements MemberRepositoryPort {

  private final Map<Long, Member> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong(0L);

  @Override
  public Member save(Member member) {
    if (member.getId() == null) {
      Long newId = sequence.incrementAndGet();
      Member newMember = new Member(newId, member.getEmail(), member.getName());
      store.put(newId, newMember);
      return newMember;
    } else {
      store.put(member.getId(), member);
      return member;
    }
  }

  @Override
  public Optional<Member> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Member> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public boolean existsByEmail(String email) {
    return store.values().stream().anyMatch(member -> member.getEmail().equals(email));
  }
}
