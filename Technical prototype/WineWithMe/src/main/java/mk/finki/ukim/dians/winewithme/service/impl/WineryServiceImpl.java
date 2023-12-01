package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.Winery;
import mk.finki.ukim.dians.winewithme.repository.InMemoryWineryRepository;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WineryServiceImpl implements WineryService {
    private final InMemoryWineryRepository inMemoryWineryRepository;

    @Override
    public List<Winery> getAllWineries() {
        return inMemoryWineryRepository.getAllWineries();
    }
}
