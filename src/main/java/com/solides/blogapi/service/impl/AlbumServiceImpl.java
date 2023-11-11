package com.solides.blogapi.service.impl;

import com.solides.blogapi.exception.BlogApiException;
import com.solides.blogapi.exception.ResourceNotFoundException;
import com.solides.blogapi.model.Album;
import com.solides.blogapi.model.User;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.request.AlbumRequest;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.AlbumResponse;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.repository.AlbumRepository;
import com.solides.blogapi.repository.UserRepository;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.AlbumService;
import com.solides.blogapi.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.solides.blogapi.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private static final String CREATED_AT = "createdAt";
    private final AlbumRepository albumRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public PagedResponse<AlbumResponse> getAllAlbums(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Album> albums = albumRepository.findAll(pageable);

        if (albums.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                    albums.getTotalPages(), albums.isLast());
        }

        List<AlbumResponse> albumResponses = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));

        return new PagedResponse<>(albumResponses, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(),
                albums.isLast());
    }

    @Override
    public ResponseEntity<Album> addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        Album album = new Album();

        modelMapper.map(albumRequest, album);

        album.setUser(user);
        Album newAlbum = albumRepository.save(album);
        return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Album> getAlbum(Long id) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, id));
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, id));
        User user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            album.setTitle(newAlbum.getTitle());
            Album updatedAlbum = albumRepository.save(album);

            AlbumResponse albumResponse = new AlbumResponse();

            modelMapper.map(updatedAlbum, albumResponse);

            return new ResponseEntity<>(albumResponse, HttpStatus.OK);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, SEM_PERMISSAO_PARA_ESSA_OPERACAO);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, id));
        User user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            albumRepository.deleteById(id);
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Album deletado com sucesso."), HttpStatus.OK);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, SEM_PERMISSAO_PARA_ESSA_OPERACAO);
    }

    @Override
    public PagedResponse<Album> getUserAlbums(String username, int page, int size) {
        User user = userRepository.getUserByName(username);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Album> albums = albumRepository.findByCreatedBy(user.getId(), pageable);

        List<Album> content = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();

        return new PagedResponse<>(content, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(), albums.isLast());
    }
}
