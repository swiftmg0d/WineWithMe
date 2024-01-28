package mk.finki.ukim.dians.winewithme.web.controler.en.REST;


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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/en")
@AllArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get the login form
     *
     * @return ResponseEntity<Map<String, String>> with json response
     */
    @GetMapping("/login/en")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> loginInfo = new HashMap<>();
        loginInfo.put("message", "Login page rendered successfully") ;
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }

    /**
     * Get the register form
     *
     * @return ResponseEntity<Map<String, String>> with json response
     */
    @GetMapping("/register/en")
    public ResponseEntity<Map<String, String>> register() {
        // You can customize this based on your logic
        Map<String, String> regInfo = new HashMap<>();
        regInfo.put("message", "Register page rendered successfully") ;
        return new ResponseEntity<>(regInfo,HttpStatus.OK);
    }

    /**
     * Handles the registration of a new user.
     *
     * @param name
     * @param surname
     * @param username
     * @param password
     * @param rpassword
     * @return ResponseEntity<Map<String, String>> with json response
     *
     */
    @PostMapping("/register/en")
    public ResponseEntity<?> registerAccount(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String rpassword
    ) {

        try {


//            Map<String, String> regInfo = new HashMap<>();
//            regInfo.put("message", "User registered successfully") ;
            return new ResponseEntity<>(userService.registerAccount(name, surname, username, password, rpassword, "EN"),HttpStatus.CREATED);
        } catch (PasswordNotMatchException | UsernameExistsException | InvalidPasswordException |
                 UsernameInPasswordException e) {
            String response =  e.getMessage();
            Map<String, String> regInfo = new HashMap<>();
            regInfo.put("error", response) ;
            return new ResponseEntity<>(regInfo,HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles the user login.
     *
     * @param username
     * @param password
     * @return ResponseEntity<Map<String, String>> with json response
     *
     */
    @PostMapping("/login/en")
    public ResponseEntity<?> loginAccount(
            @RequestParam String username,
            @RequestParam String password,HttpSession session) {



        try {
            User currentUser = userService.loginAccount(username, password, "EN");
            session.setAttribute("User", currentUser);

//            Map<String, String> loginInfo = new HashMap<>();
//            loginInfo.put("message", "User has successfully logged in") ;
            return new ResponseEntity<>(currentUser,HttpStatus.OK);


        } catch (Username0rPasswordDoesntMatchException e) {
            Map<String, String> loginInfo = new HashMap<>();
            loginInfo.put("error", e.getMessage()) ;
            return new ResponseEntity<>(loginInfo,HttpStatus.OK);
        }
    }

    /**
     * Checks the authentication status of the user.
     *
     * @param session
     * @return ResponseEntity<Map<String, String>> with json response
     */
    @PostMapping("/auth-status/en")
    public ResponseEntity<?> authStatus(HttpSession session

//                                        @RequestParam String name,
//                                        @RequestParam String surname,
//                                        @RequestParam String username,
//                                        @RequestParam String password
    ) {
        User currentUser = (User) session.getAttribute("User");
        //  User user = userRepository.findUserByUsername(username).get();

        if (currentUser != null) {
            // User is authenticated
            return new ResponseEntity<>(currentUser,HttpStatus.OK);
        }

        // User is not authenticated
        Map<String, String> authInfo = new HashMap<>();
        authInfo.put("error", "User is not authenticated ") ;
        return new ResponseEntity<>(authInfo,HttpStatus.OK);
    }

    /**
     * Logs out the user and invalidates the current session.
     *
     *
     * @return ResponseEntity<Map<String, String>> and json response
     */
    @GetMapping("/logout/en")
    public ResponseEntity<Map<String, String>> logoutAccount() {
        Map<String, String> logoutInfo = new HashMap<>();
        logoutInfo.put("message", "User has successfully logged out ") ;
        return new ResponseEntity<>(logoutInfo,HttpStatus.OK);
    }

    /**
     * Handles the process of changing the user's password.
     *
     * @param currentPassword
     * @param newPassword
     * @param confirmPassword
     * @param
     * @param session
     * @return ResponseEntity<> with json response.
     */
    @PostMapping("/changePass/en")
    public ResponseEntity<Map<String, String>> changePass(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("User");
        currentUser = userRepository.findById(currentUser.getId()).get();

        if (!(passwordEncoder.matches(currentPassword, currentUser.getPassword()))) {
            String exception = "Your current password is incorrect";
//            String currentPasswordIncorrect = "true";
//            String changePass = "true";
            Map<String, String> changePassInfo = new HashMap<>();
            changePassInfo.put("error", exception) ;
            return new ResponseEntity<>(changePassInfo,HttpStatus.OK);

        }

        if (!(newPassword.equals(confirmPassword))) {
            String exception = "Your passwords don't match";
//            String passwordsDontMatch = "true";
//            String changePass = "true";
            Map<String, String> changePassInfo = new HashMap<>();
            changePassInfo.put("error", exception) ;
            return new ResponseEntity<>(changePassInfo,HttpStatus.OK);


        }

        try {
            userService.updatePassword(currentUser.getUsername(), newPassword, "EN");
        } catch (InvalidPasswordException e) {
            String exception = e.getMessage();
//            String passwordsDontMatch = "true";
//            String changePass = "true";
            Map<String, String> changePassInfo = new HashMap<>();
            changePassInfo.put("error", exception) ;
            return new ResponseEntity<>(changePassInfo,HttpStatus.OK);

        }

        Map<String, String> changePassInfo = new HashMap<>();
        changePassInfo.put("message", "Password has been successfully changed") ;
        return new ResponseEntity<>(changePassInfo,HttpStatus.OK);
    }

    /**
     * Displays the change password form.
     *
     * @param
     * @param session
     * @return ResponseEntity<Map<String, User>> with json response.
     */
    @GetMapping("/changePass/en")
    public ResponseEntity<Map<String, User>> changePass( HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        Map<String, User> changePassInfo = new HashMap<>();
        changePassInfo.put("UserToChange",currentUser);
        return new ResponseEntity<>(changePassInfo,HttpStatus.OK);
    }
    /**
     * Displays the user profile page with optional messages and flags.
     *
     *
     * @param session
     *
     * @return ResponseEntity<Map<String, User>> with json response containing the user
     */
    @GetMapping("/profile/en")
    public ResponseEntity<Map<String, User>> profile(
            HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        User currentUser = (User) session.getAttribute("User");

        Map<String, User> profileInfo = new HashMap<>();
        profileInfo.put("User Profile Informations",currentUser);
        return new ResponseEntity<>(profileInfo,HttpStatus.OK);

    }
}
