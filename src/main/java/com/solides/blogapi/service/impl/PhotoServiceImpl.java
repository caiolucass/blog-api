package com.solides.blogapi.service.impl;

import com.solides.blogapi.exception.ResourceNotFoundException;
import com.solides.blogapi.exception.UnauthorizedException;
import com.solides.blogapi.model.Album;
import com.solides.blogapi.model.Photo;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.request.PhotoRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.PhotoResponse;
import com.solides.blogapi.repository.AlbumRepository;
import com.solides.blogapi.repository.PhotoRepository;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.PhotoService;
import com.solides.blogapi.utils.AppConstants;
import com.solides.blogapi.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.solides.blogapi.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final AlbumRepository albumRepository;

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        if (photos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), photos.getNumber(), photos.getSize(),
                    photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
        }
        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());

    }

    @Override
    public PhotoResponse getPhoto(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        return new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                photo.getThumbnailUrl(), photo.getAlbum().getId());
    }

    @Override
    public PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);
            Photo updatedPhoto = photoRepository.save(photo);
            return new PhotoResponse(updatedPhoto.getId(), updatedPhoto.getTitle(),
                    updatedPhoto.getUrl(), updatedPhoto.getThumbnailUrl(), updatedPhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao te permissao para atualizar essa foto.");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));
        if (album.getUser().getId().equals(currentUser.getId())) {
            Photo photo = new Photo(photoRequest.getTitle(), photoRequest.getUrl(), photoRequest.getThumbnailUrl(), album);
            Photo newPhoto = photoRepository.save(photo);
            return new PhotoResponse(newPhoto.getId(), newPhoto.getTitle(), newPhoto.getUrl(),
                    newPhoto.getThumbnailUrl(), newPhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao te permissao para adicionar essa foto neste album");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            photoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Foto deletada com sucesso.");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao te permissao para deletar essa foto");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());
    }
}