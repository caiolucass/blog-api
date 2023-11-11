package com.solides.blogapi.service;

import com.solides.blogapi.model.User;
import com.solides.blogapi.payload.UserIdentityAvailability;
import com.solides.blogapi.payload.UserProfile;
import com.solides.blogapi.payload.UserSummary;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.security.UserPrincipal;

public interface UserService {

        UserSummary getCurrentUser(UserPrincipal currentUser);

        UserIdentityAvailability checkUsernameAvailability(String username);

        UserIdentityAvailability checkEmailAvailability(String email);

        UserProfile getUserProfile(String username);

        User addUser(User user);

        User updateUser(User newUser, String username, UserPrincipal currentUser);

        ApiResponse deleteUser(String username, UserPrincipal currentUser);

        ApiResponse giveAdmin(String username);

        ApiResponse removeAdmin(String username);

        UserProfile setOrUpdateInfo(UserPrincipal currentUser);

}
