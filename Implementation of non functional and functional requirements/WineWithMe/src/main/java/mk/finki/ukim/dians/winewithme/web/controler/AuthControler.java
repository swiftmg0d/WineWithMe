package mk.finki.ukim.dians.winewithme.web.controler;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.exception.PasswordNotMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.Username0rPasswordDoesntMatchException;
import mk.finki.ukim.dians.winewithme.model.exception.UsernameExistsException;
import mk.finki.ukim.dians.winewithme.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@AllArgsConstructor
public class AuthControler {
    private final UserService userService;

    @GetMapping("/login")
    private String login() {
        return "login";
    }

    @GetMapping("/register")
    private String register() {
        return "register";
    }

    @PostMapping("/register")
    private String registerAccount(Model model, @RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password, @RequestParam String rpassword) {
        try {
            userService.registerAccount(name, surname, username, password, rpassword);
        } catch (PasswordNotMatchException | UsernameExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    @PostMapping("/login")
    private String loginAccount(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        try {
            User currentUser = userService.loginAccount(username, password);
            session.setAttribute("User", currentUser);
        } catch (Username0rPasswordDoesntMatchException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
        return "redirect:/mainpage";
    }

    @PostMapping("/auth-status")
    private String authStatus(HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        if (currentUser != null) {
            return "redirect:/mainpage";
        }

        return "redirect:/login";


    }

    @GetMapping("/logout")
    private String logoutAccount(HttpSession session) {
        session.invalidate();
        return "redirect:/homepage";
    }
    @PostMapping("/changePass")
    private String changePass(@RequestParam String currentPassword,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword, Model model,HttpSession session){
        User currentUser= (User) session.getAttribute("User");
        model.addAttribute("user",currentUser);
        if(!(currentPassword.equals(currentUser.getPassword()))){
            String exception = new PasswordNotMatchException().getMessage();
            model.addAttribute("changePass",true);
            model.addAttribute("currentPasswordIncorrect",true);
            model.addAttribute("message",exception);
            return "profile";
        }
        if(!(newPassword.equals(confirmPassword))){
            String exception = new PasswordNotMatchException().getMessage();
            model.addAttribute("changePass",true);
            model.addAttribute("passwordsDontMatch",true);
            model.addAttribute("message",exception);
            return "profile";
        }
        model.addAttribute("passwordsDontMatch",false);
        model.addAttribute("currentPasswordIncorrect",false);
        model.addAttribute("changePass",false);
        userService.updatePassword(currentUser.getUsername(),newPassword);
        model.addAttribute("successfullyChanged",true);
        String messageForChangedPass = "Password has been successfully changed";
        model.addAttribute("messageForChangedPass",messageForChangedPass);
        return "profile";
    }
}
