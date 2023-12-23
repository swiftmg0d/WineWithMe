package mk.finki.ukim.dians.winewithme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "contactInfo")
@Data
public class Contact {
   private String Name;
   @Id
 private    String Email;
  private String Subject;
  private String Message;

    public Contact(String name, String email, String subject, String message) {
        Name = name;
        Email = email;
        Subject = subject;
        Message = message;
    }

    public Contact() {
    }
}
