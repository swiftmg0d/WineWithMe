package mk.finki.ukim.dians.winewithme.web.controler.mk;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Contact;
import mk.finki.ukim.dians.winewithme.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class HomePageControllerMK {
    private final ContactService contactService;

    /**
     * Display the mainpage
     *
     * @return indexMk.html
     */
    @GetMapping("/homepage/mk")
    private String mainPage() {
        return "mk/indexMk";
    }

    /**
     * Display the about page
     *
     * @return aboutMk.html
     */
    @GetMapping("/about/mk")
    private String aboutPageMk() {
        return "mk/aboutMk";
    }

    /**
     * Display the contact form
     *
     * @param model
     * @return contactMk.html
     */
    @GetMapping("/contact/mk")
    public String showContactFormMk(Model model) {
        model.addAttribute("contact", new Contact());

        return "mk/contactMk";
    }

    /**
     * Submit a contact message
     *
     * @param contact
     * @param model
     * @return contactMk.html
     */
    @PostMapping("/submitContactForm/mk")
    public String submitContactFormMk(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "mk/contactMk";
    }
}
