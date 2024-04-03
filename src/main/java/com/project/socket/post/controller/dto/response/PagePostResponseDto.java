package com.project.socket.post.controller.dto.response;

import com.project.socket.post.service.usecase.PostDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record PagePostResponseDto(
    List<PostResponseDto> content,
    PageMetaData pageMetaData
) {

  public record PageMetaData(
      long totalElements,
      int totalPages,
      int pageNumber,
      int pageSize,
      int currentPageElements,
      boolean isFirstPage,
      boolean isLastPage,
      boolean hasPreviousPage,
      boolean hasNextPage,
      String sortCriteria,
      String sortDirection
  ) {

  }

  public static PagePostResponseDto toResponse(Page<PostDto> postDtoPage) {
    List<PostResponseDto> content = postDtoPage.getContent().stream()
        .map(PostResponseDto::toResponse).toList();

    String property = postDtoPage.getSort().iterator().next().getProperty();

    return new PagePostResponseDto(
        content,
        new PageMetaData(
            postDtoPage.getTotalElements(),
            postDtoPage.getTotalPages(),
            postDtoPage.getNumber(),
            postDtoPage.getSize(),
            postDtoPage.getNumberOfElements(),
            postDtoPage.isFirst(),
            postDtoPage.isLast(),
            postDtoPage.hasPrevious(),
            postDtoPage.hasNext(),
            postDtoPage.getSort().iterator().next().getProperty(),
            postDtoPage.getSort().getOrderFor(property).getDirection().name()
        )
    );
  }
}
