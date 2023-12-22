package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerAccount(String name, String surname, String username, String password, String rpassword) {

        if(!password.equals(rpassword)) throw new PasswordNotMatchException();
        if(userRepository.findUserByUsername(username).isPresent()) throw  new UsernameExistsException(username);

        return userRepository.save(new User(name,surname,username,passwordEncoder.encode(password)));
    }

    @Override
    public User loginAccount(String username, String password) {
        Optional<User> currentUser=userRepository.findUserByUsername(username);
        if(currentUser.isEmpty() || !passwordEncoder.matches(password,currentUser.get().getPassword())){
            throw new Username0rPasswordDoesntMatchException("Your username or password don't match!");
        }
        return currentUser.get();
    }


}
