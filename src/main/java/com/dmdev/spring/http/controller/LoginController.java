package com.dmdev.spring.http.controller;

import com.dmdev.spring.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "users/login";
    }

//    Удаляем PostMapping, потому что он предоставляется SpringSecurity
//    @PostMapping("/login")
//    // ВОПРОС: получается аннотация @ModelAttribute позволяет инжектить параметр не только из
//    // параметров адресной строки, но и из httpBody? Спринг сам понимает откуда брать атрибут?
//    public String login(Model model, @ModelAttribute("login") LoginDto loginDto) {
//        // Когда мы возвращаем view по умолчанию происходит forward.
//        // Можно добавить ключевое слово: forward или redirect
//        // В случае forward - viewResolver использоваться не будет, надо прописать полный путь ("forward: /WEB_INF/jsp/user/login.jsp").
//        // Поэтому на практике forward не принято использовать.
//        // redirect удобнее использовать и он более универсальный (не только jsp и сервлет),
//        // в redirect указывается не расположение view, а url.
//        // А также в случае forward нужно в первоначальном методе подготавливать всю необходимую модель
//        // чтобы страница отобразилась правильно, гораздо проще сделать redirect на другую страницу
//        // чтобы она запустила механизм по установке необходимых атрибутов в моделе
//        // - так весь функционал будет лежать в одном методе, мы не должны его распределять на несколько разных.
//        return "redirect:https:google.com";
//    }
}
