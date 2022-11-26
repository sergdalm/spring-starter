package com.dmdev.spring.service;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.querydsl.QPredicates;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.database.repository.CriteriaPredicate;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.dto.UserFilter;
import com.dmdev.spring.dto.UserReadDto;
import com.dmdev.spring.mapper.UserCreateEditMapper;
import com.dmdev.spring.mapper.UserReadMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dmdev.spring.database.entity.QUser.user;

@Service
@AllArgsConstructor
// @Transactional - над каждым методом автоматически будет открываться транзакция
// и после завершения будет закрываться.
// По правилам хорошего тона по умолчанию лучше ставить readOnly - true
// (это добавляет оптимизации на уровне БД и оптимизации на уровне application'а - т.е. в Хибернейте
// - он не будет делать flush сессии потому что нет никаких изменений),
// и в методах, где нужны изменения отдельно указывать без флага readOnly
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final ImageService imageService;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .add(filter.roles(), user.role::in)
                .build();

        return userRepository.findAll(predicate,pageable)
                .map(userReadMapper::map);
    }

    public Iterable<User> findAll(UserFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .add(filter.roles(), user.role::in)
                .build();

        return userRepository.findAll(predicate);
    }


    // Мы можем перегрузить метод findById и findAll и передавать дополнительным параметром
    // mapper и в таком случае использовать именно его, а по умолчанию использовать userReadMapper
    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    // тут не readOnly, поэтому отдельно ставим аннотацию
    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        // "специально делаем не ofNullable чтобы был fail fast принцип" -???
        return Optional.of(userDto)
                .map(dto -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(dto);
                })
                // В данном случае мы не делаем flush потому что нам нужен id сущности.
                // Если id не автогенерируемый, то лучше делать saveAndFlush() чтобы
                // сразу же отловить exception если он прозойдет в момент сохранения в БД
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(userDto, entity);
                })
                // тут важно вызвать flush() потому что иначе мы можем отловить excpetion позже,
                // а так мы сразу увидим проблему
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if(!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    // необходимо сделать flush() явно, иначе не получится протестировать метод
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user " + username));
    }
}
