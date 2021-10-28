package service.App;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

final class AppSpring {

    private AppSpring(){}

    public static void main(String[] args){
        try (ConfigurableApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            App app = appContext.getBean(App.class);
            try {
                app.play();
            } catch (IOException e) {
                //TODO exception handling
            }
        }
    }
}
