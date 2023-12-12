package mk.finki.ukim.dians.winewithme.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import mk.finki.ukim.dians.winewithme.service.WineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("")
public class MainPageControler {
    private final WineryService wineryService;
    @GetMapping("/homepage")
    private String listAllWiniers(Model model,@RequestParam(required = false) Long id) throws JsonProcessingException {
        model.addAttribute("wineries",wineryService.getAllWineries());
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
    @GetMapping("/login")
    private String login(@RequestParam int id){
        return "login";
    }
    @GetMapping("/register")
    private String register(){
        return "register";
    }
    @PostMapping("/showinfo")
    private String post(){
        return "redirect:/";
    }
    @GetMapping()
    private String mainPage(){
        return "index";
    }

}
