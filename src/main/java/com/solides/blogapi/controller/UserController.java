package com.solides.blogapi.controller;

import com.solides.blogapi.model.Album;
import com.solides.blogapi.model.Post;
import com.solides.blogapi.model.User;
import com.solides.blogapi.payload.UserIdentityAvailability;
import com.solides.blogapi.payload.UserProfile;
import com.solides.blogapi.payload.UserSummary;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.security.CurrentUser;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.AlbumService;
import com.solides.blogapi.service.PostService;
import com.solides.blogapi.service.UserService;
import com.solides.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final PostService postService;

    private final AlbumService albumService;

    public UserController(UserService userService, PostService postService, AlbumService albumService) {
        this.userService = userService;
        this.postService = postService;
        this.albumService = albumService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(currentUser);

        return new ResponseEntity< >(userSummary, HttpStatus.OK);
    }

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);

        return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email) {
        UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
        return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getUSerProfile(@PathVariable(value = "username") String username) {
        UserProfile userProfile = userService.getUserProfile(username);
        return new ResponseEntity< >(userProfile, HttpStatus.OK);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<PagedResponse<Post>> getPostsCreatedBy(@PathVariable(value = "username") String username,
                                                                 @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)
                                                                 Integer page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE)
                                                                 Integer size) {
        PagedResponse<Post> response = postService.getPostsByCreatedBy(username, page, size);
        return new ResponseEntity<  >(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/albums")
    public ResponseEntity<PagedResponse<Album>> getUserAlbums(@PathVariable(name = "username") String username,
                                                              @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)
                                                              Integer page,
                                                              @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE)
                                                              Integer size) {

        PagedResponse<Album> response = albumService.getUserAlbums(username, page, size);
        return new ResponseEntity<  >(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity< >(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser,
                                           @PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser) {
        User updatedUSer = userService.updateUser(newUser, username, currentUser);
        return new ResponseEntity< >(updatedUSer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
                                                  @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = userService.deleteUser(username, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.removeAdmin(username);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/setOrUpdateInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser) {
        UserProfile userProfile = userService.setOrUpdateInfo(currentUser);
        return new ResponseEntity< >(userProfile, HttpStatus.OK);
    }
}
