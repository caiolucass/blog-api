package com.solides.blogapi.service.impl;

import com.solides.blogapi.exception.BlogApiException;
import com.solides.blogapi.exception.ResourceNotFoundException;
import com.solides.blogapi.model.Comment;
import com.solides.blogapi.model.Post;
import com.solides.blogapi.model.User;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.request.CommentRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.repository.CommentRepository;
import com.solides.blogapi.repository.PostRepository;
import com.solides.blogapi.repository.UserRepository;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.CommentService;
import com.solides.blogapi.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import static com.solides.blogapi.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Override
    public PagedResponse<Comment> getAllComments(Long postId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return new PagedResponse<>(comments.getContent(), comments.getNumber(), comments.getSize(),
                comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
    }

    @Override
    public Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, postId));
        User user = userRepository.getUser(currentUser);
        Comment comment = new Comment(commentRequest.getBody());
        comment.setUser(user);
        comment.setPost(post);
        comment.setName(currentUser.getUsername());
        comment.setEmail(currentUser.getEmail());
        return commentRepository.save(comment);
    }

    @Override
    public Comment getComment(Long postId, Long id) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT, ID, id));
        if (comment.getPost().getId().equals(post.getId())) {
            return comment;
        }

        throw new BlogApiException(HttpStatus.BAD_REQUEST, COMENTARIO_NAO_PERTENCE_AO_POST);
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentRequest commentRequest,
                                 UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT, ID, id));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, COMENTARIO_NAO_PERTENCE_AO_POST);
        }

        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            comment.setBody(commentRequest.getBody());
            return commentRepository.save(comment);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, SEM_PERMISSAO + "atualizar esse comentário.");
    }

    @Override
    public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT, ID, id));

        if (!comment.getPost().getId().equals(post.getId())) {
            return new ApiResponse(Boolean.FALSE, COMENTARIO_NAO_PERTENCE_AO_POST);
        }

        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            commentRepository.deleteById(comment.getId());
            return new ApiResponse(Boolean.TRUE, "Comentario deletado com sucesso.");
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, SEM_PERMISSAO + "deletar esse comentario");
    }
}
