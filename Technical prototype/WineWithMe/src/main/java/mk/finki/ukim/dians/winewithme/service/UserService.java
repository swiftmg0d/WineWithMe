package mk.finki.ukim.dians.winewithme.service;

import mk.finki.ukim.dians.winewithme.model.User;

public interface UserService {
    User registerAccount(String name, String surname, String username, String password, String rpassword);

    User loginAccount(String username, String password);
}
