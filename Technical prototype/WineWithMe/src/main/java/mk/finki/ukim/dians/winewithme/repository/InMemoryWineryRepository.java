package mk.finki.ukim.dians.winewithme.repository;

import mk.finki.ukim.dians.winewithme.bootstrap.DataHolder;
import mk.finki.ukim.dians.winewithme.model.Winery;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public class InMemoryWineryRepository {
    public List<Winery> getAllWineries(){
       return DataHolder.list0fWineries;
    }
}
