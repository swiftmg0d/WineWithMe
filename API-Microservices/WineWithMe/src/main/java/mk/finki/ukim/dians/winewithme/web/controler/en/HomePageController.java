package mk.finki.ukim.dians.winewithme.web.controler.en;

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
public class HomePageController {
    private final ContactService contactService;

    /**
     * Display the mainpage
     *
     * @return index.html
     */
    @GetMapping("/homepage/en")
    private String mainPage() {
        return "en/index";
    }

    /**
     * Display the about page
     *
     * @return about.html
     */

    @GetMapping("/about/en")
    private String aboutPage() {
        return "en/about";
    }

    /**
     * Display the contact form
     *
     * @param model
     * @return contact.html
     */

    @GetMapping("/contact/en")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());

        return "en/contact";
    }

    /**
     * Submit a contact message
     *
     * @param contact
     * @param model
     * @return contact.html
     */

    @PostMapping("/submitContactForm/en")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("thankYou", true);

        return "en/contact";
    }


}
