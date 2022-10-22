package com.dmdev.spring.config;

import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.database.repository.CrudRepository;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.web.config.WebConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// По умолчанию proxyBeanMethod - true, он создаёт прокси на основании configuration'а.
// Без прокси мы каждый рза будем создавать новый бин
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.dmdev.spring",
        // отключаем автоматические фильтры
        useDefaultFilters = false,
        includeFilters = {
                // FilterType.ANNOTATION и так выбран по умолчанию
                @Filter(type = FilterType.ANNOTATION, value = Component.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, value = CrudRepository.class),
                @Filter(type = FilterType.REGEX, pattern = "com\\..+Repository")
        })
//@ImportResource("classpath:application.xml")
// В аннотации @Import мы указываем другие configuration классы, которые мы хотим подключить в наше приложение.
// Обычно там указываются те конфиги, которые сами автоматически не сканируются
// например конфигурации SpringData, Spring Web.
// Вся конфигурация от туда по сути будет вставлена сюда
@Import(WebConfiguration.class)
public class ApplicationConfiguration {

        // для того чтобы ConnectionPool был бином и добавился в applicationContext
        // его нужно пометить аннотацией @Bean.
        // ConfigurationBeanDefinitionReader будет считывать все аннотации Bean в классе Configuration.
        // Bean можно ставить и в других классах, но так не принято делать
        // потому что классы Configuration сканируются в первую очередь BeanFactoryPostProcessor'ом.
        // Если в скобочках указать id бина самим - это id и будет использоваться (иначе будет браться по названию метода)
        // Аннотация @Bean позволяет создавать бины на основание Java конфига
        @Bean("pool2")
        // можно контролировать scope, по умолчанию он singleton
        @Scope(BeanDefinition.SCOPE_SINGLETON)
        public ConnectionPool pool2(@Value("${db.username}") String username) {
                return new ConnectionPool(username, 20);
        }

        @Bean
        public ConnectionPool pool3() {
                return new ConnectionPool("test-pool", 25);
        }

        @Bean
        // Мы можем тут заинжектить и другие бины, перечислив их в параметрах метода
        // Если есть несколько бинов одного и того же типа мы можем использовать Qualifier
        // или id на основании названия параметра
        public UserRepository userRepository2(ConnectionPool pool2) {
                return new UserRepository(pool2);
        }

        @Bean
        // другой вариант заинжектить другой бин - вызвать метод в configuration классе
        // этот вариант не так часто используется, потому что неудобно передавать аргументы метода когда они есть.
        // Это будет работать только если proxyBeanMethods - true
        public UserRepository userRepository3() {
                final ConnectionPool connectionPool1 = pool3();
                final ConnectionPool connectionPool2 = pool3();
                final ConnectionPool connectionPool3 = pool3();
                return new UserRepository(pool3());
        }
}
