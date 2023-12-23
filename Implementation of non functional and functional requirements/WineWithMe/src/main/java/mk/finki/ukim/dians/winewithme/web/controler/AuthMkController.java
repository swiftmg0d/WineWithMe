package mk.finki.ukim.dians.winewithme.web.controler;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Contact;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.service.ContactService;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthMkController {
    private final UserService userService;
    private  final ContactService contactService;

    @GetMapping("/about/mk")
    private String aboutPageMk(){
        return "aboutMk";
    }

    @GetMapping("/contact/mk")
    public String showContactFormMk(Model model) {
        // Add an empty Contact object to the model for Thymeleaf to bind to
        model.addAttribute("contact", new Contact());

        return "contactMk"; // Assuming your Thymeleaf template is named "contact.html"
    }

    @GetMapping("/register/mk")
    private String register() {
        return "registerMk";
    }

    @PostMapping("/register/mk")
    private String registerAccount(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password, @RequestParam String rpassword) {
        try {
            userService.registerAccount(name, surname, username, password, rpassword);
        } catch (PasswordNotMatchException | UsernameExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "registerMk";
        }
        return "redirect:/login/mk";
    }

    @PostMapping("/submitContactForm/mk")
    public String submitContactFormMk(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "contactMk";
    }
    @GetMapping("/login/mk")
    private String login() {
        return "loginMk";
    }
    @PostMapping("/login/mk")
    private String loginAccount(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        try {
            User currentUser = userService.loginAccount(username, password);
            session.setAttribute("User", currentUser);
        } catch (Username0rPasswordDoesntMatchException e) {
            model.addAttribute("error", e.getMessage());
            return "loginMk";
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


}
