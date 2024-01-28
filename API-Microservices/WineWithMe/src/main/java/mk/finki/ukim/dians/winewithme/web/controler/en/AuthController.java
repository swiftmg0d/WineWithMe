package mk.finki.ukim.dians.winewithme.web.controler.en;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameInPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.en.*;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get the login form
     *
     * @return ModelAndView which name is login.html
     */
    @GetMapping("/login/en")
    public ModelAndView login() {
        // You can customize this based on your logic
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("en/login");
        return modelAndView;
    }

    /**
     * Get the register form
     *
     * @return ModelAndView which name is register.html
     */
    @GetMapping("/register/en")
    public ModelAndView register() {
        // You can customize this based on your logic
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("en/register");
        return modelAndView;
    }

    /**
     * Handles the registration of a new user.
     *
     * @param name
     * @param surname
     * @param username
     * @param password
     * @param rpassword
     * @return ModelAndView and redirects to the login page if successful,
     * otherwise returns to the registration page with an error message.
     */
    @PostMapping("/register/en")
    public ModelAndView registerAccount(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String rpassword) {

        ModelAndView modelAndView = new ModelAndView();

        try {
            userService.registerAccount(name, surname, username, password, rpassword, "EN");
            modelAndView.setViewName("en/login");
            return modelAndView;
        } catch (PasswordNotMatchException | UsernameExistsException | InvalidPasswordException |
                 UsernameInPasswordException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("en/register");
            return modelAndView;
        }
    }

    /**
     * Handles the user login.
     *
     * @param username
     * @param password
     * @return ModelAndView and redirects to the main page if login is successful,
     * otherwise returns to the login page with an error message.
     */
    @PostMapping("/login/en")
    public ModelAndView loginAccount(
            @RequestParam String username,
            @RequestParam String password,HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        try {
            User currentUser = userService.loginAccount(username, password, "EN");
            // Assuming session handling is taken care of elsewhere
            session.setAttribute("User", currentUser);
            String redirectUrl = "/mainpage/en";

            return new ModelAndView(new RedirectView(redirectUrl, true));

        } catch (Username0rPasswordDoesntMatchException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("en/login");

            return modelAndView;
        }
    }

    /**
     * Checks the authentication status of the user.
     *
     * @param session
     * @return ModelAndView for redirect based on the authentication status.
     */
    @PostMapping("/auth-status/en")
    public ModelAndView authStatus(HttpSession session) {
        User currentUser = (User) session.getAttribute("User");

        if (currentUser != null) {
            // User is authenticated, redirect to main page
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/mainpage/en");
            return modelAndView;
        }

        // User is not authenticated, redirect to login page
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login/en");
        return modelAndView;
    }

    /**
     * Logs out the user and invalidates the current session.
     *
     * @param session
     * @return ModelAndView and redirect to the homepage.
     */
    @GetMapping("/logout/en")
    public ModelAndView logoutAccount(HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/homepage/en");
        return modelAndView;
    }

    /**
     * Handles the process of changing the user's password.
     *
     * @param currentPassword
     * @param newPassword
     * @param confirmPassword
     * @param model
     * @param session
     * @return ModelAndView and redirect to the profile page with appropriate messages.
     */
    @PostMapping("/changePass/en")
    public ModelAndView changePass(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("User");
        currentUser = userRepository.findById(currentUser.getId()).get();

        if (!(passwordEncoder.matches(currentPassword, currentUser.getPassword()))) {
            String exception = "Your current password is incorrect";
            String currentPasswordIncorrect = "true";
            String changePass = "true";
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/profile/en?currentPasswordIncorrect=" +
                    currentPasswordIncorrect + "&changePass=" + changePass + "&messageException=" + exception);

            return modelAndView;
        }

        if (!(newPassword.equals(confirmPassword))) {
            String exception = "Your passwords don't match";
            String passwordsDontMatch = "true";
            String changePass = "true";
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/profile/en?passwordsDontMatch=" +
                    passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception);

            return modelAndView;
        }

        try {
            userService.updatePassword(currentUser.getUsername(), newPassword, "EN");
        } catch (InvalidPasswordException e) {
            String exception = e.getMessage();
            String passwordsDontMatch = "true";
            String changePass = "true";
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/profile/en?passwordsDontMatch=" +
                    passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception);

            return modelAndView;
        }

        String successfullyChanged = "true";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/profile/en?successfullyChanged=" + successfullyChanged);
        return modelAndView;
    }

    /**
     * Displays the change password form.
     *
     * @param model
     * @param session
     * @return ModelAndView with a redirect URL to the profile page with appropriate messages.
     */
    @GetMapping("/changePass/en")
    public ModelAndView changePass(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userRepository.findById(currentUser.getId()).get());
        String changePass = "true";
        modelAndView.setViewName("redirect:/profile/en?changePass=" + changePass);
        return modelAndView;
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
     * @return ModelAndView containing the user's profile and messages.
     */
    @GetMapping("/profile/en")
    public ModelAndView profile(
            Model model,
            HttpSession session,
            @RequestParam(required = false) String changePass,
            @RequestParam(required = false) String successfullyChanged,
            @RequestParam(required = false) String passwordsDontMatch,
            @RequestParam(required = false) String messageException,
            @RequestParam(required = false) String currentPasswordIncorrect) {

        ModelAndView modelAndView = new ModelAndView();

        User currentUser = (User) session.getAttribute("User");
        modelAndView.addObject("user", userRepository.findById(currentUser.getId()).get());
        modelAndView.addObject("changePass", changePass);
        modelAndView.addObject("successfullyChanged", successfullyChanged);
        modelAndView.addObject("message", messageException);
        modelAndView.addObject("passwordsDontMatch", passwordsDontMatch);
        modelAndView.addObject("currentPasswordIncorrect", currentPasswordIncorrect);
        modelAndView.setViewName("en/profile");

        return modelAndView;
    }
}
