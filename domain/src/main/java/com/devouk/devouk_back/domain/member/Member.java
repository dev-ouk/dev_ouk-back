package com.devouk.devouk_back.domain.member;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Member {
  private Long id;
  private String email;
  private String name;

  public Member(Long id, String email, String name) {
    this.id = id;
    this.email = email;
    this.name = name;
  }

  public Member(String email, String name) {
    this(null, email, name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Member)) {
      return false;
    }
    Member that = (Member) o;
    return Objects.equals(email, that.email);
  }
}
