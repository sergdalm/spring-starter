package com.dmdev.spring.service;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.dto.UserReadDto;
import com.dmdev.spring.mapper.UserCreateEditMapper;
import com.dmdev.spring.mapper.UserReadMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
// @Transactional - над каждым методом автоматически будет открываться транзакция
// и после завершения будет закрываться.
// По правилам хорошего тона по умолчанию лучше ставить readOnly - true
// (это добавляет оптимизации на уровне БД и оптимизации на уровне application'а - т.е. в Хибернейте
// - он не будет делать flush сессии потому что нет никаких изменений),
// и в методах, где нужны изменения отдельно указывать без флага readOnly
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
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
                .map(userCreateEditMapper::map)
                // В данном случае мы не делаем flush потому что нам нужен id сущности.
                // Если id не автогенерируемый, то лучше делать saveAndFlush() чтобы
                // сразу же отловить exception если он прозойдет в момент сохранения в БД
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> userCreateEditMapper.map(userDto, entity))
                // тут важно вызвать flush() потому что иначе мы можем отловить excpetion позже,
                // а так мы сразу увидим проблему
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
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

}
