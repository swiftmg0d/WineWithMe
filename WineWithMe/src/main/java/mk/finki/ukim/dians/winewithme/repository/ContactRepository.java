package mk.finki.ukim.dians.winewithme.repository;

import mk.finki.ukim.dians.winewithme.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,String> {
public Contact save(Contact contact);
}
