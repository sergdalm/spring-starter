package com.dmdev.spring.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
// Эта аннотация используется чтобы ExceptionHandler'ы были глобальными для всех контроллеров.
// В скобочках указываем что этот Handler будет работать только для контроллеров (чтобы не распространялся на ресты)
@ControllerAdvice(basePackages = "com.dmdev.http.controller")
// Мы можем унаследовать этот класс от ResponseEntityExceptionHandler, в котором есть функционал отлавливания многих ошибок
public class ControllerExceptionHandler {

    // Такой метод можно создать и в контроллере, но тогда он будет действовать только в одном контроллере.
    // В этот метод мы будем попадать тогда, когда в начальном методе в параметрах не установлен BindingResult.
    // В аннотации указываем какие ошибки мы должны отлавливать в этом методе
    @ExceptionHandler(Exception.class)
    // В метод можно инжектить любые бины, HttpServletRequest и т.д.
    public String handleException(Exception exception, HttpServletRequest request) {
        log.error("Failed to return response", exception);
        return "error/error500";
    }
}
