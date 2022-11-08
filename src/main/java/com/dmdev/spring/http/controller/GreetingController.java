package com.dmdev.spring.http.controller;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.dto.UserReadDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Controller
// Спринг собирает все атрибуты и проверяет есть ли в SessionAttributes такие же ключи,
// иначе делает атрибутом request'а
@SessionAttributes({"user"})
// указывает на общий префикс
// ПРОБЛЕМА: У МЕНЯ С ПЕРФИКСОМ НЕ РАБОТАЕТ
//@RequestMapping("/api/v1")
public class GreetingController {

    // RequestMapping довольно громоздкий, над методом лучше использовать сокращенный вариант (как GetMapping).
    // Но RequestMapping используется для указания общего префикса для всех методов контроллера
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @GetMapping("/hello/{id}")
    // С помощью аннотации @RequestParam можно сразу указывать в каком типе должны прийти параметры,
    // этот параметр маппиться на параметр Http запроса с указанным названием,
    // по умолчанию такой параметр обязательный (иначе будет статус 400)
    public ModelAndView hello(ModelAndView modelAndView, HttpServletRequest request,
                              @RequestParam Integer age,
                              @RequestHeader String accept,
                              @CookieValue("Idea-f284a24d") String Idea_f284a24d,
                              @PathVariable("id") Integer id) {
        String ageParamValue = request.getParameter("age");
        String acceptHeader = request.getHeader("accept");
        Cookie[] cookies = request.getCookies();

        // в объект modelAndView мы устанавливаем путь к нашей view
        modelAndView.setViewName("greeting/hello");

        return modelAndView;
    }

    @GetMapping("/hello")
    // Если мы возвращаем только строку, му можем инжектить объект Model
    // Чтобы смаппить userReadDto нужно в адресной строке передавать параметры идентичные полям UserReadDto.
    // Аннотацию @ModelAttribute часто опускают, такую аннотацию можно поставить над методом - тогда
    // возвращаемое значение этого метода добавиться в model как атрибут
    public String hello2(Model model, HttpServletRequest request,
                         @ModelAttribute("userReadDto") UserReadDto userReadDto) {
        // addObject() - добавить атрибуты, по умолчанию он будет атрибутом на уровне request'а.
        // под капотом будет вызван метод request.setAttribute(); requestScope
        // request.getSession().setAttribute(); sessionScope
//        modelAndView.addObject("user", new UserReadDto(1L, "Ivan"));
        System.out.println();

        model.addAttribute("user", new UserReadDto(1L, "Ivan", null, null, null, null, null));

        return "greeting/hello";
    }

    // Когда ставим эту аннотацию над методом лучше указывать название в скобочках.
    // Этот метод будет вызываться каждый раз на каждый запрос чтобы добавить этот атрибут.
    @ModelAttribute("roles")
    public List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    //    @RequestMapping(value = "/bye", method = RequestMethod.GET)
    @GetMapping("/bye")
    // Если мы хотим вернуть статическую страницу, то мы можем возвращать просто String, без modelAndView.
    public String bye(@SessionAttribute("user") UserReadDto user, Model model) {
        System.out.println();
        return "greeting/bye";
    }
}
