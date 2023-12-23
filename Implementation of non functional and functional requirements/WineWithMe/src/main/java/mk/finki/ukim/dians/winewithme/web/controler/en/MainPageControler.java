package mk.finki.ukim.dians.winewithme.web.controler.en;

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
public class MainPageControler {
    private final WineryService wineryService;
    private final UserRepository userRepository;

    @GetMapping("/mainpage/en")
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
        return "en/main";
    }

    @PostMapping("/showinfo/en")
    private String post() {
        return "redirect:/mainpage/en";
    }

    @GetMapping("/homepage/en")
    private String mainPage() {
        return "en/index";
    }

    @GetMapping("/profile/en")
    private String profile(Model model, HttpSession session,
                           @RequestParam(required = false) String changePass,
                           @RequestParam(required = false) String successfullyChanged,
                           @RequestParam(required = false) String passwordsDontMatch,
                           @RequestParam(required = false) String messageException,
                           @RequestParam(required = false) String currentPasswordIncorrect) {
        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());
        model.addAttribute("changePass", changePass);
        model.addAttribute("successfullyChanged", successfullyChanged);
        model.addAttribute("message", messageException);
        model.addAttribute("passwordsDontMatch", passwordsDontMatch);
        model.addAttribute("currentPasswordIncorrect", currentPasswordIncorrect);
        return "en/profile";
    }

    @GetMapping("/changePass/en")
    private String changePass(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("User");
        model.addAttribute("user", userRepository.findById(currentUser.getId()).get());
        String changePass = "true";
        return "redirect:/profile/en?changePass=" + changePass;
    }



    @PostMapping("/mainpage/{id}/favorite/en")
    private String favoriteWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().add(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/en?id=" + id;
    }

    @PostMapping("/mainpage/{id}/undo/en")
    private String undoWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/en?id=" + id;
    }

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

    @PostMapping("/mainpage/mywineries/{id}/undo/en")
    private String undoShowMyWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/mywineries/en";
    }

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
