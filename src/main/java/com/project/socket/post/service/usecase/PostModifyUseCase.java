package com.project.socket.post.service.usecase;

import com.project.socket.post.model.Post;

public interface PostModifyUseCase {


  Post modifyPostNew(PostModifyCommand postModifyCommand);

}
