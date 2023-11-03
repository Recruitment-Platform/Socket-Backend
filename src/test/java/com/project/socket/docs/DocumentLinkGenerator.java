package com.project.socket.docs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DocumentLinkGenerator {

  static String generateLinkCode(DocUrl docUrl) {
    return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text,
        "코드");
  }

  @RequiredArgsConstructor
  enum DocUrl {
    COMMENT_STATUS("comment-status", "댓글 상태");
    private final String pageId;
    @Getter
    private final String text;
  }
}
