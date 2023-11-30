package mk.finki.ukim.dians.winewithme.web.controler;

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
    private String listAllWiniers(Model model){
        model.addAttribute("wineries",wineryService.getAllWineries());
        return "main";
    }
}
