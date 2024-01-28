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
import mk.finki.ukim.dians.winewithme.repository.WineryRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import mk.finki.ukim.dians.winewithme.service.WineryService;
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
    private final WineryService wineryService;

    /**
     * Function for registration of new accounts
     *
     * @param name
     * @param surname
     * @param username
     * @param password
     * @param rpassword
     * @param type      The language type ("MK" for Macedonian, "EN" for English)
     * @return The registered user
     */
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

    /**
     * Function for user login
     *
     * @param username
     * @param password
     * @param lang     The language type ("MK" for Macedonian, "EN" for English)
     * @return The logged-in user
     */
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

    /**
     * Function for updating user password
     *
     * @param username
     * @param newPassword
     * @param lang        The language type ("MK" for Macedonian, "EN" for English)
     * @return The user with updated password
     */

    public User updatePassword(String username, String newPassword, String lang) {
        if (lang.equals("MK")) PasswordValidatorMK.isValid(newPassword);
        else PasswordValidator.isValid(newPassword);

        User user = userRepository.findUserByUsername(username).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;

    }

    @Override
    public void favoriteWinery(Long user, Long id) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().add(i);
                userRepository.save(k);
            });
        });
    }

    @Override
    public void undoFavorityWinery(Long user, Long id) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
    }

    @Override
    public void undoShowMyWinery(Long user, Long id) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
    }
}
