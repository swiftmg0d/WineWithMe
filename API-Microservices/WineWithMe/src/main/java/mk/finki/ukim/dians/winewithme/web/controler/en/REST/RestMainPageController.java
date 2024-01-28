package mk.finki.ukim.dians.winewithme.web.controler.en.REST;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
@RestController
@AllArgsConstructor
@RequestMapping("/api/main/en")
public class RestMainPageController {
    private final WineryService wineryService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/mainpage/en")
    public ResponseEntity<?> listAllWineries(@RequestParam(required = false) Long id, String city, String title) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        if (city == null && title == null) {
            jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        } else if(Objects.equals(city, "") && Objects.equals(title, "")){
            jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        }else{
            jsonString = objectMapper.writeValueAsString(wineryService.filter(city, title));
        }

        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

    @PostMapping("/mainpage/{id}/favorite/en")
    public ResponseEntity<?> favoriteWinery(@RequestParam Long user, @PathVariable Long id) {
        userService.favoriteWinery(user,id);
        return new ResponseEntity<>("Winery added to favorites", HttpStatus.OK);
    }

    @PostMapping("/mainpage/{id}/undo/en")
    public ResponseEntity<?> undoWinery(@RequestParam Long user, @PathVariable Long id) {
        userService.undoFavorityWinery(user,id);
        return new ResponseEntity<>("Winery removed from favorites", HttpStatus.OK);
    }

    @GetMapping("/mainpage/mywineries/en")
    public ResponseEntity<?> showMyWineries(@SessionAttribute("User") User user) {
        final User[] currentUser = {null};
        userRepository.findById(user.getId()).ifPresent(i -> {
            currentUser[0] = i;
        });

        return new ResponseEntity<>(currentUser[0].getList0fWineries(), HttpStatus.OK);
    }

    @PostMapping("/mainpage/mywineries/{id}/undo/en")
    public ResponseEntity<?> undoShowMyWinery(@RequestParam Long user, @PathVariable Long id) {
        userService.undoShowMyWinery(user,id);
        return new ResponseEntity<>("Winery removed from favorites", HttpStatus.OK);
    }
    @PostMapping("/mainpage/{id}/addreview/en")
    public ResponseEntity<?> addReviewWinery(@PathVariable Long id, @RequestParam(required = false) Integer review, @SessionAttribute("User") User user) {
        final User[] currentUser = {null};
        userRepository.findById(user.getId()).ifPresent(i -> {
            currentUser[0] =i;
        });
        wineryService.addReview(wineryService.findById(id).get(), currentUser[0], review);

        return new ResponseEntity<>("Review added", HttpStatus.OK);
    }

}
