package mk.finki.ukim.dians.winewithme.service.impl;

import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.model.Winery;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.repository.WineryRepository;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public void addReview(Winery winery, User user, Integer review) {
        if (review == null) {
            if(user.getWineryReview().containsKey(winery)){

                Double score = winery.scoreCalculate();
                double cntReview = Double.parseDouble(winery.getReviewsCount());
                cntReview-=1;
                score -= user.getWineryReview().get(winery);
                user.getWineryReview().remove(winery);
                winery.setReviewsCount(Double.toString(cntReview));
                Double newScore=score/cntReview;
                long number = Math.round(newScore * 100);
                newScore = number/100.00;

                winery.setTotalScore(String.valueOf(newScore));

                wineryRepository.save(winery);
                userRepository.save(user);
            }
        } else {

            Double score = winery.scoreCalculate();
            double cntReview = Double.parseDouble(winery.getReviewsCount());
            cntReview+=1;
            score += review;
            winery.setReviewsCount(Double.toString(cntReview));
            Double newScore=score/cntReview;
            long number = Math.round(newScore * 100);
            newScore = number/100.00;

            winery.setTotalScore(String.valueOf(newScore));
            user.getWineryReview().put(winery, review);
            wineryRepository.save(winery);
            userRepository.save(user);
        }

    }
    public List<Winery> filter(String city,String title){
        if (title != null) {
            return wineryRepository.findWineriesByTitleContainsIgnoreCase(title);
        }
        else if (city!=null){
            return  wineryRepository.findWineriesByCityContainingIgnoreCase(city);
        }
        else if(title != null && city != null){
            return wineryRepository.findWineriesByCityEqualsIgnoreCaseAndTitleContainsIgnoreCase(city,title);
        }
        else {
            return wineryRepository.findAll();
        }
    }


}
