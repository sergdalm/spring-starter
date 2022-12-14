package com.dmdev.spring.http.rest;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final CompanyService companyService;

    // ???? ?????????????????? ?? ?????? ?????????? ???????????????????????? json
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    // ?????? ?????????????????? ?????????????? ?? ?????? ?????? ???? ???????????????????? response ?????? ????????: ???????????? ?????? ????????????
    @ResponseBody
    // ???????????? ?? ?????????????????????? ?????????????? ?????? ?? ???????????? - ?? ??????????????????
    public PageResponse<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(filter, pageable);

        return PageResponse.of(page);
    }

//    @GetMapping(value = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public byte[] findAvatar(@PathVariable("id") Long id) {
//        return userService.findAvatar(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//    }

    // ?????? ???? ?????????? ?? ???????????????????????????? ?????????????? ResponseEntity
    @GetMapping(value = "/{id}/avatar")
    public ResponseEntity<byte[]> findAvatar(@PathVariable("id") Long id) {
        return userService.findAvatar(id)
                // ???????????? exception  ???????????????????? ResponseEntity
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        // ?????????? ?????????????? header'?? ?????? ???? ???????????????????? ?????????????? ?????????????????? ????????????
                        // - ???????????? ???????????????? ?????????? ???????????? ???????? header ???????????? ?????????? header()?
//                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length)
                        // ?????????????? ???????????????? ????????: ?????????????????? ???????????? ?????? ????????????????
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

//    @GetMapping("/{id}/download")
//    public ResponseEntity<?> downloadAvatar(@PathVariable("id") Long id) {
//        return userService.findAvatar(id)
//                // ???????????? exception  ???????????????????? ResponseEntity
//                .map(content -> ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .contentLength(content.length)
//                        .body(content))
//                .orElseGet(notFound()::build);
//    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id) {
        // ???????????? ?????????? ???????? ?????????????????? ???? url, ?????????????? ???? ????????????????????, ???????? ???????????????????????? NOT_FOUND exception
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    // ?? ???????????? create ???? ???????????? ?????????????? ???? 200 ????????????, ?? 201,
    // ?????? ?????????? ???????? ?????????????????? @ResponseStatus
    @ResponseStatus(HttpStatus.CREATED)
    // ?? ???????????? ???????????? ?????????????????? @ModelAttribute ?????? ???? ???????????????????????? ??.??. ???????????? ??????.
    // ?????????????????? @RequestBody ?????????????? ?? ?????? ?????? ???????????? ???????????? ?? ???????? ??????????????
    public UserReadDto create(@Validated({javax.validation.groups.Default.class, CreateAction.class}) @RequestBody UserCreateEditDto user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable("id") Long id,
                              @Validated({Default.class, UpdateAction.class}) @RequestBody UserCreateEditDto user) {
        // ?????????????? ???????????????????? ?? ???????????? ???? ???? ????????????.
        return userService.update(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable("id") Long id) {
//        if (!userService.delete(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return userService.delete(id)
                // ?????????????????????? ???????????? ?????????????? ???????????? ResponseEntity
                ? noContent().build()
                : notFound().build();
    }
}
