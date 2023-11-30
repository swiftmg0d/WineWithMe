package mk.finki.ukim.dians.winewithme.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.dians.winewithme.model.Winery;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataHolder {
    public static List<Winery> list0fWineries = new ArrayList<>();

    @PostConstruct
     void init() {
        inputWineries();
    }

     void inputWineries() {
        List<Winery> wineries = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String line = "";
        try {
            bufferedReader = new BufferedReader(new FileReader("../FilterWineryData/output.csv"));
            list0fWineries = bufferedReader.lines().map(Winery::createWinery).toList().stream().skip(1).toList();
        } catch (Exception e) {
        }
    }
}
