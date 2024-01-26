package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.Winery;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.repository.WineryRepository;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WineryServiceImpl implements WineryService {
    private final WineryRepository wineryRepository;
    private final UserRepository userRepository;

    @Override
    public List<Winery> getAllWineries() {
        return wineryRepository.findAll();
    }

    @Override
    public Optional<Winery> findById(Long id) {
        return wineryRepository.findById(id);
    }

    private Double getNewScore(Double score, Double cntReview) {
        double newScore = score / cntReview;
        long number = Math.round(newScore * 100);
        newScore = number / 100.00;
        return newScore;
    }


    @Override
    public void addReview(Winery winery, User user, Integer review) {
        Double score = winery.scoreCalculate();
        double cntReview = Double.parseDouble(winery.getReviewsCount());
        if (review == null) {
            if (user.getWineryReview().containsKey(winery)) {

                cntReview -= 1;
                score -= user.getWineryReview().get(winery);
                user.getWineryReview().remove(winery);
                winery.setReviewsCount(Double.toString(cntReview));


                winery.setTotalScore(String.valueOf(getNewScore(score, cntReview)));

                wineryRepository.save(winery);
                userRepository.save(user);
            }
        } else {
            if (!user.getWineryReview().containsKey(winery)) {
                cntReview += 1;
                score += review;
                winery.setReviewsCount(Double.toString(cntReview));


                winery.setTotalScore(String.valueOf(getNewScore(score, cntReview)));

                user.getWineryReview().put(winery, review);
                wineryRepository.save(winery);
                userRepository.save(user);
            } else {
                score -= user.getWineryReview().get(winery);
                score += review;
                user.getWineryReview().remove(winery);
                userRepository.save(user);

                winery.setTotalScore(String.valueOf(getNewScore(score, cntReview)));

                user.getWineryReview().put(winery, review);

                userRepository.save(user);
                wineryRepository.save(winery);
            }

        }

    }

    @Override
    public List<Winery> filter(String city, String title) {
        if (title != null && city != null) {
            return wineryRepository.findWineriesByCityEqualsIgnoreCaseAndTitleContainsIgnoreCase(city, title);
        } else if (title != null) {
            return wineryRepository.findWineriesByTitleContainsIgnoreCase(title);
        } else if (city != null) {
            return wineryRepository.findWineriesByCityEqualsIgnoreCase(city);
        }
        return wineryRepository.findAll();

    }
}
