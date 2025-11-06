package com.postlundhall.TraningsregisterSpringH2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main entry point for the <strong>Träningsregister</strong> Spring Boot application.
 * <p>
 * This class bootstraps the entire application using {@link SpringBootApplication}, which combines:
 * </p>
 * <ul>
 *   <li>{@code @Configuration} — defines beans</li>
 *   <li>{@code @EnableAutoConfiguration} — enables auto-configuration</li>
 *   <li>{@code @ComponentScan} — scans for components in this package and subpackages</li>
 * </ul>
 * <p>
 * The {@link #main(String[])} method starts the application, while the protected
 * {@link #runApplication(String[])} method is extracted for <strong>testability</strong> purposes
 * — allowing integration tests to capture and inspect the {@link ConfigurableApplicationContext}.
 * </p>
 *
 * <strong>Features</strong>
 * <ul>
 *   <li>H2 in-memory database (dev/profile)</li>
 *   <li>Spring Security with form login</li>
 *   <li>Thymeleaf templating</li>
 *   <li>CRUD operations for exercises and workouts</li>
 *   <li>Input sanitization and validation</li>
 * </ul>
 *
 * <strong>Running the Application</strong>
 * <pre>
 * mvn spring-boot:run
 * or
 * java -jar target/*.jar
 * </pre>
 *
 * <strong>Default URL</strong>
 * <a href="http://localhost:8080">http://localhost:8080</a>
 *
 * @see SpringBootApplication
 * @see ConfigurableApplicationContext
 *  @author Patrik Östlund Hall
 *  @since 1.0
 */
@SpringBootApplication
public class TraningsregisterSpringH2Application {

    /**
     * Application entry point.
     * <p>
     * Delegates to {@link #runApplication(String[])} to start the Spring Boot application.
     * </p>
     *
     * @param args command-line arguments (e.g., {@code --spring.profiles.active=prod})
     */
    public static void main(String[] args) {
        runApplication(args);
    }

    /**
     * Starts the Spring application and returns the application context.
     * <p>
     * Extracted as a protected method for testing purposes to enable:
     * </p>
     * <ul>
     *   <li>Unit and integration testing</li>
     *   <li>Programmatic access to beans</li>
     *   <li>Graceful shutdown in tests</li>
     * </ul>
     *
     * @param args the command-line arguments passed to the application
     * @return the fully initialized {@link ConfigurableApplicationContext}
     */
    protected static ConfigurableApplicationContext runApplication(String[] args) {
        return SpringApplication.run(TraningsregisterSpringH2Application.class, args);
    }
}