package com.dmdev.spring.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
// указывает на общий префикс
@RequestMapping("/api/v1")
public class GreetingController {

    // RequestMapping довольно громоздкий, над методом лучше использовать сокращенный вариант (как GetMapping).
    // Но RequestMapping используется для указания общего префикса для всех методов контроллера
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @GetMapping("/hello")
    public ModelAndView hello(ModelAndView modelAndView) {
        modelAndView.setViewName("greeting/hello");

        return modelAndView;
    }

    //    @RequestMapping(value = "/bye", method = RequestMethod.GET)
    @GetMapping("/bye")
    public ModelAndView bye(ModelAndView modelAndView) {
        modelAndView.setViewName("greeting/bye");

        return modelAndView;
    }
}
