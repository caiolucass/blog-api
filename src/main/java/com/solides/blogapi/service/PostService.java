package com.solides.blogapi.service;

import com.solides.blogapi.model.Post;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.request.PostRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PostResponse;
import com.solides.blogapi.security.UserPrincipal;

public interface PostService {

    PagedResponse<Post> getAllPosts(int page, int size);

    PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size);

    Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser);

    ApiResponse deletePost(Long id, UserPrincipal currentUser);

    PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPost(Long id);

}
