package mk.finki.ukim.dians.winewithme.repository;

import mk.finki.ukim.dians.winewithme.model.Winery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineryRepository extends JpaRepository<Winery,Long> {
    List<Winery> findWineriesByCityEqualsIgnoreCase(String city);
    List<Winery>findWineriesByTitleContainsIgnoreCase(String title);
    List<Winery>findWineriesByCityEqualsIgnoreCaseAndTitleContainsIgnoreCase(String city,String title);
}
