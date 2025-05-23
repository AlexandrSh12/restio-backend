package com.restio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SpaController {

    @GetMapping({"/login", "/other-path", "/another-path/**"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
