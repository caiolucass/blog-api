package com.solides.blogapi.controller;

import com.solides.blogapi.model.Post;
import com.solides.blogapi.payload.request.PostRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.PostResponse;
import com.solides.blogapi.security.CurrentUser;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.PostService;
import com.solides.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Post>> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        PagedResponse<Post> response = postService.getAllPosts(page, size);

        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> addPost(@Valid @RequestBody PostRequest postRequest,
                                                @CurrentUser UserPrincipal currentUser) {
        PostResponse postResponse = postService.addPost(postRequest, currentUser);

        return new ResponseEntity< >(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable(name = "id") Long id) {
        Post post = postService.getPost(id);

        return new ResponseEntity< >(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "id") Long id,
                                           @Valid @RequestBody PostRequest newPostRequest, @CurrentUser UserPrincipal currentUser) {
        Post post = postService.updatePost(id, newPostRequest, currentUser);

        return new ResponseEntity< >(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = postService.deletePost(id, currentUser);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }
}