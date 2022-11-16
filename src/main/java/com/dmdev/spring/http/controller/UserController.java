package com.dmdev.spring.http.controller;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.PageResponse;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.dto.UserFilter;
import com.dmdev.spring.dto.UserReadDto;
import com.dmdev.spring.service.CompanyService;
import com.dmdev.spring.service.UserService;
import com.dmdev.spring.validation.group.CreateAction;
import com.dmdev.spring.validation.group.UpdateAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.groups.Default;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;


    @GetMapping
    // Методы в контроллере именуем как и раньше - с глаголами
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        final Page<UserReadDto> page = userService.findAll(filter, pageable);
        model.addAttribute("users",  PageResponse.of(page));
        model.addAttribute("filter",  filter);
        return "users/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        // Всегда когда идёт обращение по url, который не существует, надо пробрасывать NOT_FOUND exception
        return userService.findById(id)
                .map(user -> {
                            model.addAttribute("user", user);
                            model.addAttribute("roles", Role.values());
                            model.addAttribute("companies", companyService.findAll());
                            return "users/user";
                        }
                )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    // Если объекта user не будет, то все его поля будут null
    // Тут чтобы не было проблем с неймингом мы явно используем аннотацию @ModelAttribute
    // и указываем в скобочках имя user (потому что до этого клали этот объект в сессию с помощью метода redirectAttributes.addFlashAttribute()
    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user) {
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
    // В аннотацию @Validated можно передать список групп, по умолчанию всегда вызывается дефолтная группа (javax.validation.groups.Default)
    // - она запускает все аннотации, которые не содержит группы.
    // Группа это класс или интерфейс-метка, никакого функционала он не несёт.
    // Если указать какую-то свою группу и не поставвить дефолтную, то это будет переопределение дефолтного поведения
    // и будут отслеживаться только те аннотации и запускаться только те валидаторы, которые придерживаются CreateAction группы.
    // Чаще всего мы явно будет указывать Default в аннотациях @Validated.
    public String create(@Validated({Default.class, CreateAction.class}) UserCreateEditDto user,
                         // сразу же после модели, которую мы валидируем, мы инжектаем объект BindingRedult (порядок важен!)
                         // для чтого чтобы не возвращать сразу пользователю 400 ошибку, а сами контролировали ответ,
                         // именно этот результат будет хранить в себе ошибки.
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) { // тут должна быть валидация
//            return "redirect:/users/registration=" + user.getUsername();

            // другой способ добавить юзера: добавить каждое поле отдельности, эти значения будут переданы в адрес строки (в параметры url)
//            redirectAttributes.addAttribute("username", user.getUsername());

            // использует не параметры в url, а обычный session attribute - этот объект будет в сессии
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("user", user);

            return "redirect:/users/registration";
        }
        return "redirect:/users/" + userService.create(user).getId();
    }

    // Мы не можем из нашей формы отправлять put запрос, поэтому тут мы нарушим best practice по API и добавим "/update"
//    @PutMapping("/{id}")
    @PostMapping("/{id}/update")
    // @Validated позволяет передавать группы, которые мы валидируем
    public String update(@PathVariable("id") Long id, @Validated({Default.class, UpdateAction.class}) UserCreateEditDto user) {
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
