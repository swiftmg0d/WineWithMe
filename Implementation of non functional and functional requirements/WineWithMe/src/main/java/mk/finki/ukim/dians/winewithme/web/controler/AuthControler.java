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
public class AuthControler {
    private final UserService userService;
    private  final ContactService contactService;


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
    @GetMapping("/about")
    private String aboutPage(){
        return "about";
    }
    //    @GetMapping("/contact")
//    private String contactPage(){
//        return "contact";
//    }
    @GetMapping("/contact")
    public String showContactForm(Model model) {
        // Add an empty Contact object to the model for Thymeleaf to bind to
        model.addAttribute("contact", new Contact());

        return "contact"; // Assuming your Thymeleaf template is named "contact.html"
    }

    @PostMapping("/submitContactForm")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "contact";
    }

    @PostMapping("/changePass")
    private String changePass(@RequestParam String currentPassword,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword, Model model,HttpSession session){
        User currentUser= (User) session.getAttribute("User");
        if(!(currentPassword.equals(currentUser.getPassword()))){
            String exception = new PasswordNotMatchException().getMessage();
            String currentPasswordIncorrect = "true";
            String changePass = "true";
//            model.addAttribute("changePass",true);
//            model.addAttribute("currentPasswordIncorrect",true);
//            model.addAttribute("message",exception);
            return "redirect:/profile?currentPasswordIncorrect="+currentPasswordIncorrect+"&changePass="+changePass+"&messageException="+exception;
        }
        if(!(newPassword.equals(confirmPassword))){
            String exception = new PasswordNotMatchException().getMessage();
            String passwordsDontMatch = "true";
            String changePass = "true";
            //model.addAttribute("changePass",true);
            //model.addAttribute("passwordsDontMatch",true);
            //model.addAttribute("message",exception);
            return "redirect:/profile?passwordsDontMatch="+passwordsDontMatch+"&changePass="+changePass+"&messageException="+exception;
        }
//        model.addAttribute("passwordsDontMatch",false);
//        model.addAttribute("currentPasswordIncorrect",false);
//        model.addAttribute("changePass",false);
        userService.updatePassword(currentUser.getUsername(),newPassword);
        String successfullyChanged = "true";
        //model.addAttribute("successfullyChanged",true);
       // String messageForChangedPass = "Password has been successfully changed";
        //model.addAttribute("messageForChangedPass",messageForChangedPass);
        return "redirect:/profile?successfullyChanged=" + successfullyChanged;
    }
}
