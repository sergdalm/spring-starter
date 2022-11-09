package com.dmdev.spring.http.controller;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.service.CompanyService;
import com.dmdev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;

    @GetMapping
    // Методы в контроллере именуем как и раньше - с глаголами
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
//        model.addAttribute("users", userService.findAll(filter));
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        // Всегда когда идёт обращение по url, который не существует, надо пробрасывать NOT_FOUND exception
        return userService.findById(id)
                .map(user -> {
                            model.addAttribute("user", user);
                            model.addAttribute("roles", Role.values());
                            model.addAttribute("companies", companyService.findAll());
                            return "user/user";
                        }
                )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    // Если объекта user не будет, то все его поля будут null
    // Тут чтобы не было проблем с неймингом мы явно используем аннотацию @ModelAttribute
    // и указываем в скобочках имя user (потому что до этого клали этот объект в сессию с помощью метода redirectAttributes.addFlashAttribute()
    public String registration(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("companies", companyService.findAll());
        return "users/registration";
    }

    // Тут неявно будет использоваться @ModelAttribute
    @PostMapping
    // В случае create мы должны вернуть не 200 статус, а 201,
    // для этого есть аннотация @ResponseStatus
//    @ResponseStatus(HttpStatus.CREATED)
    public String create(UserCreateEditDto user, RedirectAttributes redirectAttributes) {
        if (user.getUsername() != null) { // тут должна быть валидация
            // (тут должна б
//            return "redirect:/users/registration=" + user.getUsername();

            // другой способ добавить юзера: добавить каждое поле отдельности, эти значения будут переданы в адрес строки (в параметры url)
//            redirectAttributes.addAttribute("username", user.getUsername());

            // использует не параметры в url, а обычный session attribute - этот объект будет в сессии
            redirectAttributes.addFlashAttribute("user", user);

            return "redirect:/users/registration";
        }
        return "redirect:/users/" + userService.create(user).getId();
    }

    // Мы не можем из нашей формы отправлять put запрос, поэтому тут мы нарушим best practice по API и добавим "/update"
//    @PutMapping("/{id}")
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, UserCreateEditDto user) {
//        userService.update(id, user)

        // Спринг хранит все PathVariable в отдельной map по ключу и значению,
        // поэтому в redirect мы сразу можем использовать PathVariable и не делать конкатенацию
        return userService.update(id, user)
                .map(it -> "redirect:users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //    @DeleteMapping("/{id}")
    @PostMapping("/{id}/delete")
    // delete() или remove()
    public String delete(@PathVariable("id") Long id) {
//        userService.delete(id);
        // Если такого пользователя не существовало - пробрасываем exception
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users";
    }
}
