package mk.finki.ukim.dians.winewithme.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.repository.UserRepository;
import mk.finki.ukim.dians.winewithme.service.UserService;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping
public class MainPageControler {
    private final WineryService wineryService;
    private final UserRepository userRepository;

    @GetMapping("/mainpage")
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

        jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());


        model.addAttribute("list0f", jsonString);
        if (id != null) {
            wineryService.findById(id).ifPresent(i -> {
                model.addAttribute("wine", i);
                model.addAttribute("lat", i.getLocation().split(" ")[0]);
                model.addAttribute("lng", i.getLocation().split(" ")[1]);

            });
        }
        return "main";
    }

    @PostMapping("/showinfo")
    private String post() {
        return "redirect:/mainpage";
    }

    @GetMapping("/homepage")
    private String mainPage() {
        return "index";
    }
    @GetMapping("/profile")
    private String profile(Model model, HttpSession session){
        User currentUser= (User) session.getAttribute("User");
        model.addAttribute("user",currentUser);
        return "profile";
    }
    @GetMapping("/changePass")
    private String changePass(Model model, HttpSession session){
        User currentUser= (User) session.getAttribute("User");
        model.addAttribute("user",currentUser);
        model.addAttribute("changePass",true);
        return "profile";
    }



    @PostMapping("/mainpage/{id}/favorite")
    private String favoriteWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().add(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage?id=" + id;
    }

    @PostMapping("/mainpage/{id}/undo")
    private String undoWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage?id=" + id;
    }

    @GetMapping("/mainpage/mywineries")
    private String showMyWineries(HttpSession session, Model model) {
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        model.addAttribute("wineries", currentUser[0].getList0fWineries());
        model.addAttribute("user", currentUser[0]);

        return "mywineries";
    }

    @PostMapping("/mainpage/mywineries/{id}/undo")
    private String undoShowMyWinery(@RequestParam Long user, @PathVariable Long id, Model model) {
        wineryService.findById(id).ifPresent(i -> {
            userRepository.findById(user).ifPresent(k -> {
                k.getList0fWineries().remove(i);
                userRepository.save(k);
            });
        });
        return "redirect:/mainpage/mywineries";
    }
    @PostMapping("/mainpage/{id}/addreview")
    private String addReviewWinery(HttpSession session,@PathVariable Long id,@RequestParam(required = false)Integer review){
        final User[] currentUser = {(User) session.getAttribute("User")};
        userRepository.findById(currentUser[0].getId()).ifPresent(i -> {
            currentUser[0] = i;
        });
        wineryService.addReview(wineryService.findById(id).get(),currentUser[0],review);



        return "redirect:/mainpage?id="+id;
    }
}
