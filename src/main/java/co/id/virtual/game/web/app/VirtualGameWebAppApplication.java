package co.id.virtual.game.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Virtual Casino Chips Game Platform.
 * This application provides a platform for virtual casino games using toy chips
 * for entertainment without involving real money.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class VirtualGameWebAppApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VirtualGameWebAppApplication.class, args);
    }
}
