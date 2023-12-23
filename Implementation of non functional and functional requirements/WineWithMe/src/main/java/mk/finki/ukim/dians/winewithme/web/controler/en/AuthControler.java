package mk.finki.ukim.dians.winewithme.web.controler.en;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Contact;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameInPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.en.*;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.ContactService;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthControler {
    private final UserService userService;
    private final ContactService contactService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/login/en")
    private String login() {
        return "en/login";
    }

    @GetMapping("/register/en")
    private String register() {
        return "en/register";
    }

    @PostMapping("/register/en")
    private String registerAccount(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password, @RequestParam String rpassword) {
        try {
            userService.registerAccount(name, surname, username, password, rpassword,"EN");
        } catch (PasswordNotMatchException | UsernameExistsException | InvalidPasswordException |
                 UsernameInPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return "en/register";
        }
        return "redirect:/login/en";
    }

    @PostMapping("/login/en")
    private String loginAccount(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        try {
            User currentUser = userService.loginAccount(username, password,"EN");
            session.setAttribute("User", currentUser);
        } catch (Username0rPasswordDoesntMatchException e) {
            model.addAttribute("error", e.getMessage());
            return "en/login";
        }
        return "redirect:/mainpage/en";
    }

    @PostMapping("/auth-status/en")
    private String authStatus(HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        if (currentUser != null) {
            return "redirect:/mainpage/en";
        }

        return "redirect:/login/en";


    }

    @GetMapping("/logout/en")
    private String logoutAccount(HttpSession session) {
        session.invalidate();
        return "redirect:/homepage/en";
    }

    @GetMapping("/about/en")
    private String aboutPage() {
        return "en/about";
    }


    @GetMapping("/contact/en")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());

        return "en/contact";
    }

    @PostMapping("/submitContactForm/en")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "en/contact";
    }

    @PostMapping("/changePass/en")
    private String changePass(@RequestParam String currentPassword,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        currentUser = userRepository.findById(currentUser.getId()).get();
        if (!(passwordEncoder.matches(currentPassword, currentUser.getPassword()))) {
            String exception = "Your current password is incorrect";
            String currentPasswordIncorrect = "true";
            String changePass = "true";

            return "redirect:/profile/en?currentPasswordIncorrect=" + currentPasswordIncorrect + "&changePass=" + changePass + "&messageException=" + exception;
        }
        if (!(newPassword.equals(confirmPassword))) {
            String exception = "Your passwords doesn't match";
            String passwordsDontMatch = "true";
            String changePass = "true";

            return "redirect:/profile/en?passwordsDontMatch=" + passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception;
        }
        try {
            userService.updatePassword(currentUser.getUsername(), newPassword,"EN");

        }catch (InvalidPasswordException e){
            String exception = e.getMessage();
            String passwordsDontMatch = "true";
            String changePass = "true";
            return "redirect:/profile/en?passwordsDontMatch=" + passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception;

        }
        String successfullyChanged = "true";

        return "redirect:/profile/en?successfullyChanged=" + successfullyChanged;
    }
}
