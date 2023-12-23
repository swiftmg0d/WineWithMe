package mk.finki.ukim.dians.winewithme.web.controler.mk;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Contact;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.password.en.InvalidPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.model.exception.password.uni.UsernameInPasswordException;
import mk.finki.ukim.dians.winewithme.model.exception.password.mk.InvalidPasswordExceptionMK;
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
public class AuthControllerMK {
    private final UserService userService;
    private final ContactService contactService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/about/mk")
    private String aboutPageMk() {
        return "mk/aboutMk";
    }

    @GetMapping("/contact/mk")
    public String showContactFormMk(Model model) {
        model.addAttribute("contact", new Contact());

        return "mk/contactMk";
    }

    @GetMapping("/register/mk")
    private String register() {
        return "mk/registerMk";
    }

    @PostMapping("/register/mk")
    private String registerAccount(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password, @RequestParam String rpassword) {
        try {
            userService.registerAccount(name, surname, username, password, rpassword,"MK");
        } catch (PasswordNotMatchException | UsernameExistsException | InvalidPasswordExceptionMK |
                 UsernameInPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return "mk/registerMk";
        }
        return "redirect:/login/mk";
    }

    @PostMapping("/submitContactForm/mk")
    public String submitContactFormMk(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "mk/contactMk";
    }

    @GetMapping("/login/mk")
    private String login() {
        return "mk/loginMk";
    }

    @PostMapping("/login/mk")
    private String loginAccount(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        try {
            User currentUser = userService.loginAccount(username, password,"MK");
            session.setAttribute("User", currentUser);
        } catch (Username0rPasswordDoesntMatchException e) {
            model.addAttribute("error", e.getMessage());
            return "mk/loginMk";
        }
        return "redirect:/mainpage/mk";
    }

    @PostMapping("/auth-status/mk")
    private String authStatus(HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        if (currentUser != null) {
            return "redirect:/mainpage/mk";
        }

        return "redirect:/login/mk";

    }

    @GetMapping("/logout/mk")
    private String logoutAccount(HttpSession session) {
        session.invalidate();
        return "redirect:/homepage/mk";
    }
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
            userService.updatePassword(currentUser.getUsername(), newPassword,"MK");

        }catch (InvalidPasswordException e){
            String exception = e.getMessage();
            String passwordsDontMatch = "true";
            String changePass = "true";
            return "redirect:/profile/mk?passwordsDontMatch=" + passwordsDontMatch + "&changePass=" + changePass + "&messageException=" + exception;

        }
        String successfullyChanged = "true";

        return "redirect:/profile/mk?successfullyChanged=" + successfullyChanged;
    }

}
