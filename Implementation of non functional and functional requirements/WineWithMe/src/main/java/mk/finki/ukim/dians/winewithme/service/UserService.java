package mk.finki.ukim.dians.winewithme.service;

import mk.finki.ukim.dians.winewithme.model.User;

public interface UserService {
    User registerAccount(String name, String surname, String username, String password, String rpassword,String lang);

    User loginAccount(String username, String password,String lang);
    User updatePassword(String username, String newPassword,String lang);

    void favoriteWinery(Long user, Long id);

    void undoFavorityWinery(Long user, Long id);

    void undoShowMyWinery(Long user, Long id);
}
