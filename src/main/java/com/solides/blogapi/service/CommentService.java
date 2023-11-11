package com.solides.blogapi.service;

import com.solides.blogapi.model.Comment;
import com.solides.blogapi.payload.request.CommentRequest;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.security.UserPrincipal;

public interface CommentService {

    PagedResponse<Comment> getAllComments(Long postId, int page, int size);

    Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);

    Comment getComment(Long postId, Long id);

    Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser);

    ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);

}
