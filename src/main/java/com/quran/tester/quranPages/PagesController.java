package com.quran.tester.quranPages;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quran")
public class PagesController {

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String getRandomAyah(){
        return "index";
    }
}
