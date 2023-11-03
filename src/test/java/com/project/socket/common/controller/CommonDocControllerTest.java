package com.project.socket.common.controller;

import static com.project.socket.common.error.ProblemDetailFieldDescription.CODE;
import static com.project.socket.common.error.ProblemDetailFieldDescription.DETAIL;
import static com.project.socket.common.error.ProblemDetailFieldDescription.DETAIL_LIST_FIELD;
import static com.project.socket.common.error.ProblemDetailFieldDescription.DETAIL_LIST_REASON;
import static com.project.socket.common.error.ProblemDetailFieldDescription.DETAIL_LIST_VALUE;
import static com.project.socket.common.error.ProblemDetailFieldDescription.INSTANCE;
import static com.project.socket.common.error.ProblemDetailFieldDescription.STATUS;
import static com.project.socket.common.error.ProblemDetailFieldDescription.TITLE;
import static com.project.socket.common.error.ProblemDetailFieldDescription.TYPE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.controller.CommonDocController.SampleRequest;
import com.project.socket.common.error.GlobalExceptionHandler;
import com.project.socket.docs.CustomResponseFieldsSnippet;
import com.project.socket.docs.EnumDocs;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class CommonDocControllerTest {

  ObjectMapper objectMapper = new ObjectMapper();
  MockMvc mockMvc;
  CommonDocController commonDocController = new CommonDocController();

  @BeforeEach
  void setup(RestDocumentationContextProvider restDocumentation) {
    mockMvc = MockMvcBuilders.standaloneSetup(commonDocController)
                             .apply(documentationConfiguration(restDocumentation))
                             .setControllerAdvice(GlobalExceptionHandler.class)
                             .addFilter(new CharacterEncodingFilter("UTF-8", true))
                             .build();
  }

  @Test
  void test() throws Exception {
    SampleRequest sampleRequest = new SampleRequest("", "email");
    mockMvc.perform(
               post("/sample/error")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsBytes(sampleRequest)))
           .andExpect(status().isBadRequest())
           .andDo(
               document("sample-error-response",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   responseFields(
                       fieldWithPath(TYPE.getField()).description(TYPE.getDescription()),
                       fieldWithPath(TITLE.getField()).description(TITLE.getDescription()),
                       fieldWithPath(STATUS.getField()).description(STATUS.getDescription()),
                       fieldWithPath(DETAIL.getField()).description(DETAIL.getDescription()),
                       fieldWithPath(DETAIL_LIST_FIELD.getField())
                           .description(DETAIL_LIST_FIELD.getDescription()).optional(),
                       fieldWithPath(DETAIL_LIST_VALUE.getField())
                           .description(DETAIL_LIST_VALUE.getDescription()).optional(),
                       fieldWithPath(DETAIL_LIST_REASON.getField())
                           .description(DETAIL_LIST_REASON.getDescription()).optional(),
                       fieldWithPath(INSTANCE.getField()).description(INSTANCE.getDescription()),
                       fieldWithPath(CODE.getField()).description(CODE.getDescription()).optional()
                   )
               )
           );
  }

  @Test
  void enums() throws Exception {
    ResultActions result = this.mockMvc.perform(get("/sample/enums")
        .contentType(MediaType.APPLICATION_JSON)
    );

    MvcResult mvcResult = result.andReturn();

    EnumDocs enumDocs = getData(mvcResult);

    result.andExpect(status().isOk())
          .andDo(
              document(
                  "sample-enums-response",
                  Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                  Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                  customResponseFields("custom-response",
                      beneathPath("data.commentStatus").withSubsectionId("commentStatus"),
                      attributes(key("title").value("commentStatus")),
                      enumConvertFieldDescriptor((enumDocs.getCommentStatus()))
                  )
              ));
  }

  // 커스텀 템플릿 사용을 위한 함수
  public static CustomResponseFieldsSnippet customResponseFields
  (String type, PayloadSubsectionExtractor<?> subsectionExtractor,
      Map<String, Object> attributes, FieldDescriptor... descriptors) {
    return new CustomResponseFieldsSnippet(type, Arrays.asList(descriptors),
        attributes
        , true, subsectionExtractor);
  }

  // Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
  private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
    return enumValues.entrySet().stream()
                     .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                     .toArray(FieldDescriptor[]::new);
  }

  // mvc result 데이터 파싱
  private EnumDocs getData(MvcResult result) throws IOException {
    ApiResponseDto<EnumDocs> apiResponseDto = objectMapper
        .readValue(result.getResponse().getContentAsByteArray(),
            new TypeReference<ApiResponseDto<EnumDocs>>() {
            }
        );
    return apiResponseDto.getData();
  }
}