package mk.finki.ukim.dians.winewithme.web.controler.mk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping
public class MainPageControllerMK {
    private final WineryService wineryService;
    private final UserRepository userRepository;


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
    @GetMapping("/mainpage/mk")
    private String listAllWiniers(HttpSession session,
                                  Model model,
                                  @RequestParam(required = false) Long id,
                                  @RequestParam(required = false) String city,
                                  @RequestParam(required = false) String title) throws JsonProcessingException {
        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("wineries", wineryService.getAllWineries());
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        int number0f = wineryService.getAllWineries().size();


        if (title == null && city == null) {
            jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        } else {
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
        return "mk/mainMk";
    }

    /**
     * Display the main page view
     *
     * @return mainMk.html
     */

    @PostMapping("/showinfo/mk")
    private String post() {
        return "redirect:/mainpage/mk";
    }

    /**
     * Add the winery to favorites
     *
     * @param user
     * @param id
     * @param model
     * @return thе main page with the id of the winery added to favorites in the URL
     */
    @PostMapping("/mainpage/{id}/favorite/mk")
    private String favoriteWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().add(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/mk?id=" + id;
    }

    /**
     * Remove the winery from favorites
     *
     * @param user
     * @param id
     * @param model
     * @return the view with the removed winery from the favorites list
     */
    @PostMapping("/mainpage/{id}/undo/mk")
    private String undoWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/mk?id=" + id;
    }

    /**
     * Display the wineries added to favorites
     *
     * @param session
     * @param model
     * @return view of the users favorite wineries
     */

    @GetMapping("/mainpage/mywineries/mk")
    private String showMyWineries(HttpSession session, Model model) {
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        model.addAttribute("wineries", currentUser[0].getList0fWineries());
        model.addAttribute("user", currentUser[0]);

        return "mk/mywineriesMk";
    }

    /**
     * Show the favorites after removing a winery
     *
     * @param user
     * @param id
     * @param model
     * @return the view with the removed winery from the favorites list
     */
    @PostMapping("/mainpage/mywineries/{id}/undo/mk")
    private String undoShowMyWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/mywineries/mk";
    }

    /**
     * Add review for a winery
     *
     * @param session
     * @param id
     * @param review
     * @return the main page
     */
    @PostMapping("/mainpage/{id}/addreview/mk")
    private String addReviewWinery(HttpSession session, @PathVariable Long id, @RequestParam(required = false) Integer review) {
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        wineryService.addReview(wineryService.findById(id).get(), currentUser[0], review);


        return "redirect:/mainpage/mk?id=" + id;
    }
}
