package com.solides.blogapi.service.impl;

import com.solides.blogapi.exception.*;
import com.solides.blogapi.model.User;
import com.solides.blogapi.model.role.Role;
import com.solides.blogapi.model.role.RoleName;
import com.solides.blogapi.payload.UserIdentityAvailability;
import com.solides.blogapi.payload.UserProfile;
import com.solides.blogapi.payload.UserSummary;
import com.solides.blogapi.payload.request.InfoRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.repository.PostRepository;
import com.solides.blogapi.repository.RoleRepository;
import com.solides.blogapi.repository.UserRepository;
import com.solides.blogapi.security.UserPrincipal;
import com.solides.blogapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername());
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.getUserByName(username);

        Long postCount = postRepository.countByCreatedBy(user.getId());

        return new UserProfile(user.getId(), user.getUsername(), postCount);
    }

    @Override
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Nome de usuário ja cadastrado.");
            throw new BadRequestException(apiResponse);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email já cadastrado.");
            throw new BadRequestException(apiResponse);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("Role do usuario nao setada.")));
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        User user = userRepository.getUserByName(username);
        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(user);

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao tem permissao para atualizar o perfil do: " + username);
        throw new UnauthorizedException(apiResponse);

    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", username));
        if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao tem permissao para deletar o perfil do: " + username);
            throw new AccessDeniedException(apiResponse);
        }

        userRepository.deleteById(user.getId());
        return new ApiResponse(Boolean.TRUE, "Peril deletado com sucesso: " + username);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        User user = userRepository.getUserByName(username);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("Role do usuario nao setada.")));
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("Role do usuario nao setada.")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ApiResponse(Boolean.TRUE, "Você deu a permissão de ADMIN para o usuario: " + username);
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        User user = userRepository.getUserByName(username);
        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("Role do usuario nao setada.")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ApiResponse(Boolean.TRUE, "Você removeu a permissão de ADMIN para o usuario: " + username);
    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", currentUser.getUsername()));

        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            User updatedUser = userRepository.save(user);

            Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return new UserProfile(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getCreatedAt(),
                    updatedUser.getEmail(), postCount);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Você nao tem permissão para atualizar o perfil dos usuarios.", HttpStatus.FORBIDDEN);
        throw new AccessDeniedException(apiResponse);
    }
}
