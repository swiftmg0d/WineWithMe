package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.password.mk.PasswordValidatorMK;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameInPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.en.*;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerAccount(String name, String surname, String username, String password, String rpassword, String type) {

        if (type.equals("MK")) {

            if (!password.equals(rpassword)) throw new PasswordNotMatchException("Лозинките не се совпаѓаат!");
            PasswordValidatorMK.isValid(password);
            if (userRepository.findUserByUsername(username.strip()).isPresent())
                throw new UsernameExistsException(String.format("%s постои, пробајте со друго корисничко име!", username));
            if (password.toLowerCase().contains(username.toLowerCase()))
                throw new UsernameInPasswordException("Вашата лозика не треба да го содржи корисничкото име!");

        } else if (type.equals("EN")) {

            if (!password.equals(rpassword)) throw new PasswordNotMatchException("The passwords didn't match!");
            PasswordValidator.isValid(password);
            if (userRepository.findUserByUsername(username.strip()).isPresent())
                throw new UsernameExistsException(String.format("%s exists, try diffrent username", username));
            if (password.toLowerCase().contains(username.toLowerCase()))
                throw new UsernameInPasswordException("Your password shouldn't contain your username!");
        }


        return userRepository.save(new User(name, surname, username, passwordEncoder.encode(password)));
    }

    @Override
    public User loginAccount(String username, String password, String lang) {

        Optional<User> currentUser = userRepository.findUserByUsername(username);

        if (lang.equals("MK")) {
            if (currentUser.isEmpty() || !passwordEncoder.matches(password, currentUser.get().getPassword())) {
                throw new Username0rPasswordDoesntMatchException("Вашото корисничко име или лозинка не се совпаѓаат!");
            }
        } else if (lang.equals("EN")) {
            if (currentUser.isEmpty() || !passwordEncoder.matches(password, currentUser.get().getPassword())) {
                throw new Username0rPasswordDoesntMatchException("Your username or password don't match!");
            }
        }

        return currentUser.get();
    }


    public User updatePassword(String username, String newPassword, String lang) {
        if (lang.equals("MK")) PasswordValidatorMK.isValid(newPassword);
        else PasswordValidator.isValid(newPassword);

        User user = userRepository.findUserByUsername(username).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;

    }
}
