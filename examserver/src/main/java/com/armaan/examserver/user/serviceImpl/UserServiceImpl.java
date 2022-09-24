package com.armaan.examserver.user.serviceImpl;

import com.armaan.examserver.user.entity.User;
import com.armaan.examserver.user.entity.UserRole;
import com.armaan.examserver.user.repository.RoleRepository;
import com.armaan.examserver.user.repository.UserRepository;
import com.armaan.examserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Creating User
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        User local = this.userRepository.findByUsername(user.getUsername());
        User userLocal;

        if (local != null) {
            System.out.println("User is already here");
            throw new Exception("User already present");
        } else {
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole()); // Saving each new role
            }
            user.getUserRoles().addAll(userRoles); // Assigning role in users before saving the users
            String encodedPassword = this.bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userLocal = this.userRepository.save(user);
        }
        return userLocal;
    }

    // Getting User by Username
    @Override
    public User findUserByUserName(String username) {

        return this.userRepository.findByUsername(username);
    }

    // Deleting User by Id
    @Override
    public void deleteUserById(Long userId) {

        this.userRepository.deleteById(userId);
    }

}