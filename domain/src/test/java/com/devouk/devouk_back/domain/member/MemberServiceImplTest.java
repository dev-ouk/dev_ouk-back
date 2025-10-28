package com.devouk.devouk_back.domain.member;

import com.devouk.devouk_back.domain.common.exception.DuplicateMemberException;
import com.devouk.devouk_back.domain.common.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class MemberServiceImplTest {
    MemberRepositoryPort repository;
    MemberServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(MemberRepositoryPort.class);
        service = new MemberServiceImpl(repository);
    }

    @Test
    void createMember_success_savesAndReturnsMember() {
        given(repository.existsByEmail("a@b.com")).willReturn(false);
        Member saved = new Member(1L, "a@b.com", "상욱");
        given(repository.save(any(Member.class))).willReturn(saved);

        Member result = service.createMember("a@b.com", "상욱");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("a@b.com");
        assertThat(result.getName()).isEqualTo("상욱");
        then(repository).should().existsByEmail("a@b.com");
        then(repository).should().save(any(Member.class));
    }

    @Test
    void createMember_duplicateEmail_throwsDuplicateMemberException() {
        given(repository.existsByEmail("dup@test.com")).willReturn(true);

        assertThatThrownBy(() -> service.createMember("dup@test.com", "중복"))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessageContaining("dup@test.com");
    }

    @Test
    void getMember_found_returnsMember() {
        Member m = new Member(10L, "x@y.com", "철수");
        given(repository.findById(10L)).willReturn(Optional.of(m));

        Member result = service.getMember(10L);

        assertThat(result.getEmail()).isEqualTo("x@y.com");
        assertThat(result.getName()).isEqualTo("철수");
    }

    @Test
    void getMember_notFound_throwsMemberNotFoundException() {
        given(repository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMember(999L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void getAllMembers_returnsListFromRepository() {
        Member m1 = new Member(1L, "a@a.com", "A");
        Member m2 = new Member(2L, "b@b.com", "B");
        given(repository.findAll()).willReturn(List.of(m1, m2));

        List<Member> result = service.getAllMembers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("a@a.com");
    }

    @Test
    void member_equalsAndHashCode_areEmailBased() {
        Member m1 = new Member(1L, "same@mail.com", "철수");
        Member m2 = new Member(2L, "same@mail.com", "영희");

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }
}
