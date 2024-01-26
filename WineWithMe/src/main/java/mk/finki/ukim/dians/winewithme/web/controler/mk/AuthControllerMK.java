package mk.finki.ukim.dians.winewithme.web.controler.mk;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.password.en.InvalidPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameInPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.mk.InvalidPasswordExceptionMK;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthControllerMK {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Get the register form
     *
     * @return registerMk.html
     */

    @GetMapping("/register/mk")
    private String register() {
        return "mk/registerMk";
    }

    /**
     * Handles the registration of a new user.
     *
     * @param model
     * @param name
     * @param surname
     * @param username
     * @param password
     * @param rpassword
     * @return Redirects to the login page if successful,
     * otherwise returns to the registration page with an error message.
     */
    @PostMapping("/register/mk")
    private String registerAccount(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password, @RequestParam String rpassword) {
        try {
            userService.registerAccount(name, surname, username, password, rpassword, "MK");
        } catch (PasswordNotMatchException | UsernameExistsException | InvalidPasswordExceptionMK |
                 UsernameInPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return "mk/registerMk";
        }
        return "redirect:/login/mk";
    }

    /**
     * Get the login form
     *
     * @return loginMk.html
     */
    @GetMapping("/login/mk")
    private String login() {
        return "mk/loginMk";
    }

    /**
     * Handles the user login.
     *
     * @param model
     * @param session
     * @param username
     * @param password
     * @return Redirects to the main page if login is successful,
     * otherwise returns to the login page with an error message.
     */

    @PostMapping("/login/mk")
    private String loginAccount(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        try {
            User currentUser = userService.loginAccount(username, password, "MK");
            session.setAttribute("User", currentUser);
        } catch (Username0rPasswordDoesntMatchException e) {
            model.addAttribute("error", e.getMessage());
            return "mk/loginMk";
        }
        return "redirect:/mainpage/mk";
    }

    /**
     * Checks the authentication status of the user.
     *
     * @param session
     * @return A string representing a redirect to the main page if the user is authenticated, otherwise a redirect to the login page.
     */

    @PostMapping("/auth-status/mk")
    private String authStatus(HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        if (currentUser != null) {
            return "redirect:/mainpage/mk";
        }

        return "redirect:/login/mk";

    }

    /**
     * Logs out the user and invalidates the current session.
     *
     * @param session
     * @return Redirects to the homepage.
     */
    @GetMapping("/logout/mk")
    private String logoutAccount(HttpSession session) {
        session.invalidate();
        return "redirect:/homepage/mk";
    }

    /**
     * Handles the process of changing the user's password.
     *
     * @param currentPassword
     * @param newPassword
     * @param confirmPassword
     * @param model
     * @param session
     * @return Redirects to the profile page with appropriate messages.
     */

    @PostMapping("/changePass/mk")
    private String changePass(@RequestParam String currentPassword,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        currentUser = userRepository.findById(currentUser.getId()).get();
        if (!(passwordEncoder.matches(currentPassword, currentUser.getPassword()))) {
            String exception = "Вашата моментална лозика е неточна";
            String currentPasswordIncorrect = "true";
            String changePass = "true";

            return "redirect:/profile/mk?currentPasswordIncorrect=" + currentPasswordIncorrect + "&changePass=" + changePass + "&messageException=" + exception;
        }
        if (!(newPassword.equals(confirmPassword))) {
            String exception = "Вашите лозинки не се совпаѓаат";
            String passwordsDontMatch = "true";
            String changePass = "true";

            return "redirect:/profile/mk?passwordsDontMatch=" + passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception;
        }
        try {
            userService.updatePassword(currentUser.getUsername(), newPassword, "MK");

        } catch (InvalidPasswordException e) {
            String exception = e.getMessage();
            String passwordsDontMatch = "true";
            String changePass = "true";
            return "redirect:/profile/mk?passwordsDontMatch=" + passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception;

        }
        String successfullyChanged = "true";

        return "redirect:/profile/mk?successfullyChanged=" + successfullyChanged;
    }

    /**
     * Displays the change password form.
     *
     * @param model
     * @param session
     * @return Redirects to the profile page with appropriate messages.
     */
    @GetMapping("/changePass/mk")
    private String changePass(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());
        String changePass = "true";
        return "redirect:/profile/mk?changePass=" + changePass;
    }

    /**
     * Displays the user profile page with optional messages and flags.
     *
     * @param model
     * @param session
     * @param changePass
     * @param successfullyChanged
     * @param passwordsDontMatch
     * @param messageException         Error message to be displayed.
     * @param currentPasswordIncorrect
     * @return the view of the users profile
     */
    @GetMapping("/profile/mk")
    private String profile(Model model, HttpSession session,
                           @RequestParam(required = false) String changePass,
                           @RequestParam(required = false) String successfullyChanged,
                           @RequestParam(required = false) String passwordsDontMatch,
                           @RequestParam(required = false) String messageException,
                           @RequestParam(required = false) String currentPasswordIncorrect) {
        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());
        model.addAttribute("changePass", changePass);
        model.addAttribute("successfullyChanged", successfullyChanged);
        model.addAttribute("message", messageException);
        model.addAttribute("passwordsDontMatch", passwordsDontMatch);
        model.addAttribute("currentPasswordIncorrect", currentPasswordIncorrect);
        return "mk/profileMk";
    }


}
