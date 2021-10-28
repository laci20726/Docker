package service.App;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
        "aspect",
        "service",
        "view"
})
@PropertySource(value = "classpath:filePaths.properties")
public class AppConfig {

    @Bean(initMethod="init")
    public App app() {
        return new App();
    }

}
