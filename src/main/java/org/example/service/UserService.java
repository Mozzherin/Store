package org.example.service;

import org.example.entityes.Role;
import org.example.entityes.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;
    @Value("${upload.path}")
    private String uploadPath;

    public User addPhoto(User user, MultipartFile file) {
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setPhotoProfile(resultFileName);
        }
        return user;
    }


    public void sendEmail(User user, MultipartFile file) {
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(addPhoto(user, file));
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Market. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        userRepository.delete(user);
        user.setActive(true);
        if (user.getUsername().equals("ADMIN")) {
            user.setRoles(Collections.singleton(Role.ADMIN));
        } else {
            user.setRoles(Collections.singleton(Role.USER));
        }
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public void update(User updateUser, MultipartFile file) {
        User userToBeUpdated = findById(updateUser.getId());
        userToBeUpdated.setActivationCode(updateUser.getActivationCode());
        userToBeUpdated.setEmail(updateUser.getEmail());
        userToBeUpdated.setUsername(updateUser.getUsername());
        userToBeUpdated.setPassword(updateUser.getPassword());
        userToBeUpdated.setPhotoProfile(updateUser.getPhotoProfile());
        userToBeUpdated.setRoles(updateUser.getRoles());
        userToBeUpdated.setActivationCode(updateUser.getActivationCode());
        if (!file.isEmpty()) {
            addPhoto(userToBeUpdated, file);
        }
        userRepository.save(userToBeUpdated);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
