package mk.finki.ukim.dians.winewithme.service;

import mk.finki.ukim.dians.winewithme.model.Winery;

import java.util.List;
import java.util.Optional;

public interface WineryService {
    List<Winery>getAllWineries();

    Optional<Winery> findById(Long id);
}
