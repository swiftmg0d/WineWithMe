package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.*;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerAccount(String name, String surname, String username, String password, String rpassword) {

        if (!password.equals(rpassword)) throw new PasswordNotMatchException();
        PasswordValidator.isValid(password);
        if (userRepository.findUserByUsername(username.strip()).isPresent())
            throw new UsernameExistsException(username);
        if (password.toLowerCase().contains(username.toLowerCase())) throw new UsernameInPasswordException();
        return userRepository.save(new User(name, surname, username, passwordEncoder.encode(password)));
    }

    @Override
    public User loginAccount(String username, String password) {
        Optional<User> currentUser = userRepository.findUserByUsername(username);
        if (currentUser.isEmpty() || !passwordEncoder.matches(password, currentUser.get().getPassword())) {
            throw new Username0rPasswordDoesntMatchException("Your username or password don't match!");
        }
        return currentUser.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public User updatePassword(String username, String newPassword){

        User user = userRepository.findUserByUsername(username).get();
        user.setPassword(newPassword);
        userRepository.save(user);
        return user;

    }
}
