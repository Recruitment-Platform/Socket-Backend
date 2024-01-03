package com.project.socket.docs;


import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnumDocs {

  Map<String, String> commentStatus;
  Map<String, String> postMeeting;
  Map<String, String> postType;
  Map<String, String> postStatus;

}
