package com.devouk.devouk_back.algo_note.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

@Getter
public class AlgoNoteCreateRequest {
  @NotBlank(message = "title 은 비울 수 없습니다.")
  @Size(max = 200, message = "title 은 200자 이하여야 합니다.")
  private String title;

  @NotBlank(message = "slug 은 비울 수 없습니다.")
  @Size(max = 200, message = "slug 은 200자 이하여야 합니다.")
  private String slug;

  @NotNull(message = "contentJson 은 비울 수 없습니다.")
  private JsonNode contentJson;

  private String contentHtml;
  private String contentText;

  @NotBlank(message = "status 는 비울 수 없습니다.")
  private String status;

  private Boolean isPublic;
  private Boolean isPin;

  private List<@NotBlank(message = "tagSlugs 항목은 비울 수 없습니다.") String> tagSlugs;

  // setters for jackson
  public void setTitle(String title) {
    this.title = title;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public void setContentJson(JsonNode contentJson) {
    this.contentJson = contentJson;
  }

  public void setContentHtml(String contentHtml) {
    this.contentHtml = contentHtml;
  }

  public void setContentText(String contentText) {
    this.contentText = contentText;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public void setIsPin(Boolean isPin) {
    this.isPin = isPin;
  }

  public void setTagSlugs(List<String> tagSlugs) {
    this.tagSlugs = tagSlugs;
  }
}
