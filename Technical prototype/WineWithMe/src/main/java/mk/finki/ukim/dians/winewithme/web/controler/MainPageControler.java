package mk.finki.ukim.dians.winewithme.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.model.User;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping
public class MainPageControler {
    private final WineryService wineryService;
    @GetMapping("/mainpage")
    private String listAllWiniers(HttpSession session,Model model, @RequestParam(required = false) Long id) throws JsonProcessingException {
        User currentUser= (User) session.getAttribute("User");
        model.addAttribute("wineries",wineryService.getAllWineries());
        model.addAttribute("user",currentUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        int number0f=wineryService.getAllWineries().size();
        model.addAttribute("list0f", jsonString);
        if(id!=null){
            wineryService.findById(id).ifPresent(i->{
                model.addAttribute("wine",i);

            });
        }
        return "main";
    }

    @PostMapping("/showinfo")
    private String post(){
        return "redirect:/mainpage";
    }
    @GetMapping("/homepage")
    private String mainPage(){
        return "index";
    }

}
