package com.solides.blogapi.service.impl;

import com.solides.blogapi.exception.BadRequestException;
import com.solides.blogapi.exception.ResourceNotFoundException;
import com.solides.blogapi.exception.UnauthorizedException;
import com.solides.blogapi.model.Post;
import com.solides.blogapi.model.User;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.request.PostRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PostResponse;
import com.solides.blogapi.repository.PostRepository;
import com.solides.blogapi.repository.UserRepository;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.PostService;
import com.solides.blogapi.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.solides.blogapi.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size) {
        validatePageNumberAndSize(page, size);
        User user = userRepository.getUserByName(username);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    @Override
    public Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            post.setTitle(newPostRequest.getTitle());
            post.setBody(newPostRequest.getBody());
            return postRepository.save(post);
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, SEM_PERMISSAO_PARA_ESSA_OPERACAO);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            postRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Post deletado com sucesso.");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, SEM_PERMISSAO_PARA_ESSA_OPERACAO);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, 1L));

        Post post = new Post();
        post.setBody(postRequest.getBody());
        post.setTitle(postRequest.getTitle());
        post.setUser(user);

        Post newPost = postRepository.save(post);
        PostResponse postResponse = new PostResponse();

        postResponse.setTitle(newPost.getTitle());
        postResponse.setBody(newPost.getBody());

        return postResponse;
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("O número de paginas nao pode ser menor que 0.");
        }

        if (size < 0) {
            throw new BadRequestException("O tamanho nao pode ser menor que 0.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("O tamanho da pagina nao pode ser maior que: " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
