package com.solides.blogapi.controller;

import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.request.PhotoRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PhotoResponse;
import com.solides.blogapi.security.CurrentUser;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.PhotoService;
import com.solides.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping
    public PagedResponse<PhotoResponse> getAllPhotos(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return photoService.getAllPhotos(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PhotoResponse> addPhoto(@Valid @RequestBody PhotoRequest photoRequest,
                                                  @CurrentUser UserPrincipal currentUser) {
        PhotoResponse photoResponse = photoService.addPhoto(photoRequest, currentUser);

        return new ResponseEntity< >(photoResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable(name = "id") Long id) {
        PhotoResponse photoResponse = photoService.getPhoto(id);

        return new ResponseEntity< >(photoResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PhotoResponse> updatePhoto(@PathVariable(name = "id") Long id,
                                                     @Valid @RequestBody PhotoRequest photoRequest, @CurrentUser UserPrincipal currentUser) {

        PhotoResponse photoResponse = photoService.updatePhoto(id, photoRequest, currentUser);

        return new ResponseEntity< >(photoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = photoService.deletePhoto(id, currentUser);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }
}
