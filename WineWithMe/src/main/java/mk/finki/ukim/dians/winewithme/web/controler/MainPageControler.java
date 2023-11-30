package mk.finki.ukim.dians.winewithme.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@AllArgsConstructor
@RequestMapping("")
public class MainPageControler {
    private final WineryService wineryService;
    @GetMapping("")
    private String listAllWiniers(Model model) throws JsonProcessingException {
        model.addAttribute("wineries",wineryService.getAllWineries());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(wineryService.getAllWineries());
        model.addAttribute("list0f", jsonString);
        return "main";
    }
    @GetMapping("/login")
    private String login(){
        return "login";
    }
    @GetMapping("/register")
    private String register(){
        return "register";
    }

    @GetMapping("/main")
    private String mainPage(){
        return "index";
    }

}
