package com.devouk.devouk_back.infra.member;

import static org.assertj.core.api.Assertions.*;

import com.devouk.devouk_back.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryMemberRepositoryTest {

  private InMemoryMemberRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryMemberRepository();
  }

  @Test
  void save_assignsNewId_whenIdIsNull() {
    // given
    Member m = new Member("test1@example.com", "테스터1");

    // when
    Member saved = repository.save(m);

    // then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getEmail()).isEqualTo("test1@example.com");
    assertThat(saved.getName()).isEqualTo("테스터1");

    // 저장된 값도 findById로 다시 나와야 함
    Optional<Member> found = repository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("test1@example.com");
  }

  @Test
  void save_updatesExistingMember_whenIdExists() {
    Member first = repository.save(new Member("test2@example.com", "초기이름"));
    Long existingId = first.getId();

    Member updatedCandidate = new Member(existingId, "test2@example.com", "바뀐이름");

    Member updated = repository.save(updatedCandidate);

    assertThat(updated.getId()).isEqualTo(existingId);
    assertThat(updated.getName()).isEqualTo("바뀐이름");

    Optional<Member> foundAgain = repository.findById(existingId);
    assertThat(foundAgain).isPresent();
    assertThat(foundAgain.get().getName()).isEqualTo("바뀐이름");
  }

  @Test
  void findById_returnsEmptyOptional_whenNotExists() {
    Optional<Member> result = repository.findById(999L);
    assertThat(result).isEmpty();
  }

  @Test
  void findAll_returnsAllSavedMembers_asSnapshotList() {
    Member a = repository.save(new Member("a@example.com", "A"));
    Member b = repository.save(new Member("b@example.com", "B"));

    List<Member> all = repository.findAll();

    assertThat(all).hasSize(2);
    assertThat(all)
        .extracting(Member::getEmail)
        .containsExactlyInAnyOrder("a@example.com", "b@example.com");

    all.clear();
    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void existsByEmail_returnsTrueIfPresentOtherwiseFalse() {
    repository.save(new Member("dup@example.com", "DupUser"));

    boolean exists1 = repository.existsByEmail("dup@example.com");
    boolean exists2 = repository.existsByEmail("nope@example.com");

    assertThat(exists1).isTrue();
    assertThat(exists2).isFalse();
  }

  @Test
  void idSequenceIncrementsForEachNewSave() {
    Member m1 = repository.save(new Member("s1@example.com", "S1"));
    Member m2 = repository.save(new Member("s2@example.com", "S2"));
    Member m3 = repository.save(new Member("s3@example.com", "S3"));

    assertThat(m1.getId()).isNotNull();
    assertThat(m2.getId()).isNotNull();
    assertThat(m3.getId()).isNotNull();

    assertThat(m1.getId()).isNotEqualTo(m2.getId());
    assertThat(m2.getId()).isNotEqualTo(m3.getId());
    assertThat(m1.getId()).isNotEqualTo(m3.getId());
  }
}
