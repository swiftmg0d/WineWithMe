package mk.finki.ukim.dians.winewithme.web.controler.en;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping
public class MainPageController {
    private final WineryService wineryService;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Get the information needed for the main page
     *
     * @param session
     * @param model
     * @param id
     * @param city
     * @param title
     * @return the main page
     * @throws JsonProcessingException
     */
    @GetMapping("/mainpage/en")
    private String listAllWiniers(HttpSession session, Model model, @RequestParam(required = false) Long id, String city, String title) throws JsonProcessingException {

        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        if (city == null && title == null) {
            jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        } else if(Objects.equals(city, "") && Objects.equals(title, "")){
            jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        }else{
            jsonString = objectMapper.writeValueAsString(wineryService.filter(city, title));
        }


        model.addAttribute("list0f", jsonString);

        if (id != null) {
            wineryService.findById(id).ifPresent(i -> {
                model.addAttribute("wine", i);
                model.addAttribute("lat", i.getLocation().split(" ")[0]);
                model.addAttribute("lng", i.getLocation().split(" ")[1]);

            });
        }
        return "en/main";
    }

    /**
     * Display the main page view
     *
     * @return main.html
     */

    @PostMapping("/showinfo/en")
    private String post() {
        return "redirect:/mainpage/en";
    }

    /**
     * Add the winery to favorites
     *
     * @param user
     * @param id
     * @param model
     * @return thе main page with the id of the winery added to favorites in the URL
     */
    @PostMapping("/mainpage/{id}/favorite/en")
    private String favoriteWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        userService.favoriteWinery(user,id);
        return "redirect:/mainpage/en?id=" + id;
    }

    /**
     * Remove the winery from favorites
     *
     * @param user
     * @param id
     * @param model
     * @return the view with the removed winery from the favorites list
     */

    @PostMapping("/mainpage/{id}/undo/en")
    private String undoWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        userService.undoFavorityWinery(user,id);
        return "redirect:/mainpage/en?id=" + id;
    }

    /**
     * Display the wineries added to favorites
     *
     * @param session
     * @param model
     * @return view of the users favorite wineries
     */

    @GetMapping("/mainpage/mywineries/en")
    private String showMyWineries(HttpSession session, Model model) {
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        model.addAttribute("wineries", currentUser[0].getList0fWineries());
        model.addAttribute("user", currentUser[0]);

        return "en/mywineries";
    }

    /**
     * Show the favorites after removing a winery
     *
     * @param user
     * @param id
     * @param model
     * @return the view with the removed winery from the favorites list
     */
    @PostMapping("/mainpage/mywineries/{id}/undo/en")
    private String undoShowMyWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        userService.undoShowMyWinery(user,id);
        return "redirect:/mainpage/mywineries/en";
    }

    /**
     * Add review for a winery
     *
     * @param session
     * @param id
     * @param review
     * @return the main page
     */
    @PostMapping("/mainpage/{id}/addreview/en")
    private String addReviewWinery(HttpSession session, @PathVariable Long id, @RequestParam(required = false) Integer review) {
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        wineryService.addReview(wineryService.findById(id).get(), currentUser[0], review);


        return "redirect:/mainpage/en?id=" + id;
    }
}
