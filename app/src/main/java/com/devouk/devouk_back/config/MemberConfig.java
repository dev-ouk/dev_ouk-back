package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.member.MemberRepositoryPort;
import com.devouk.devouk_back.domain.member.MemberService;
import com.devouk.devouk_back.domain.member.MemberServiceImpl;
import com.devouk.devouk_back.infra.member.InMemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberConfig {
  @Bean
  public MemberRepositoryPort memberRepository() {
    return new InMemoryMemberRepository();
  }

  @Bean
  public MemberService memberService(MemberRepositoryPort memberRepository) {
    return new MemberServiceImpl(memberRepository);
  }
}
