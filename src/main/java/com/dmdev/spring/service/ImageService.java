package com.dmdev.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
//@RequiredArgsConstructor
public class ImageService {

    // Если передадим такую property, то она будет использоваться,
    // иначе будет использоваться значение по умолчанию (после двоеточия)
//    @Value("${app.image.bucket:C:/Users/Hello/Documents/GitHub/spring-starter/images}")
    private final String bucket = "C:/Users/Hello/Documents/GitHub/spring-starter/images";

    @SneakyThrows
    public void upload(String imagePath, InputStream content) {
        Path fullImagePath = Path.of(bucket, imagePath);

        try(content) {
            // На случай если у нас папка bucket не создана,
            // иначе Files.write() может пробросить exception, так как он не может создать папку
            Files.createDirectories(fullImagePath.getParent());
            // Есл файл большой - надо из InputStream по чуть-чуть пихать байты в OutputStream
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    // Если бы это был большой файл, лучше возвращать InputStream
    public Optional<byte[]> get(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);

        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }
}
