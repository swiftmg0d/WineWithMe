package mk.finki.ukim.dians.winewithme.web.controler.mk.REST;


import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Contact;
import mk.finki.ukim.dians.winewithme.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mk")
@AllArgsConstructor
public class RestHomePageControllerMK {
    private final ContactService contactService;

    @GetMapping("/homepage")
    public ResponseEntity<Map<String, String>>mainPage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the homepage!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/about")
    public ResponseEntity<Map<String, String>> aboutPage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the about page.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contact")
    public ResponseEntity<Map<String, String>> showContactForm() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the contact form.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submitContactForm")
    public ResponseEntity<Map<String, String>> submitContactForm(@RequestBody Contact contact) {
        // Assuming you have a ContactService to save the contact
        contactService.save(contact);

        // You can customize the response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contact form submitted successfully!");

        // Respond with a 201 status code and the response map
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
