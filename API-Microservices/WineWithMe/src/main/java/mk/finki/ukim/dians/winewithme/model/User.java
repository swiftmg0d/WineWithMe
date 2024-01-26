package mk.finki.ukim.dians.winewithme.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user-winery")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Winery> list0fWineries;
    @ElementCollection
    @CollectionTable(name = "winery_reviews", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "winery_id")
    @Column(name = "review")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Map<Winery,Integer> wineryReview=new HashMap<>();


    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        list0fWineries=new ArrayList<>();
    }
    public User(){}
}
