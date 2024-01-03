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
    COMMENT_STATUS("comment-status", "댓글 상태"),
    POST_MEETING("post-meeting", "포스트 미팅"),
    POST_TYPE("post-type", "포스트 타입"),
    POST_STATUS("post-status", "게시물 상태");
    private final String pageId;
    @Getter
    private final String text;
  }
}
