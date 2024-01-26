package mk.finki.ukim.dians.winewithme.service;

import mk.finki.ukim.dians.winewithme.model.Contact;
import org.springframework.stereotype.Service;


public interface ContactService {
    public Contact save(Contact contact);
}
