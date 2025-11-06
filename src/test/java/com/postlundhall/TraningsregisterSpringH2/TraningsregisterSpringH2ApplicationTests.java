package com.postlundhall.TraningsregisterSpringH2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TraningsregisterSpringH2ApplicationTests {

    @Test
    void contextLoads() {
        // Ensures Spring context can load (uses real Spring, not mocked)
    }

    @Test
    void mainMethod_callsSpringApplicationRun_withCorrectArguments() {
        String[] args = new String[]{};

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mocked.when(() -> SpringApplication.run(
                            eq(TraningsregisterSpringH2Application.class),
                            eq(args)
                    ))
                    .thenReturn(mockContext);

            // This calls runApplication(args) → which calls SpringApplication.run()
            TraningsregisterSpringH2Application.main(args);

            // Verify it was called
            mocked.verify(() -> SpringApplication.run(
                    TraningsregisterSpringH2Application.class,
                    args
            ));
        }
    }

    @Test
    void runApplication_callsSpringApplicationRun_andReturnsContext() {
        String[] args = {"--debug"};

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mocked.when(() -> SpringApplication.run(
                            eq(TraningsregisterSpringH2Application.class),
                            eq(args)
                    ))
                    .thenReturn(mockContext);

            // Now this compiles and works
            ConfigurableApplicationContext result = TraningsregisterSpringH2Application.runApplication(args);

            // Assert correct return value
            org.junit.jupiter.api.Assertions.assertEquals(mockContext, result);

            // Verify interaction
            mocked.verify(() -> SpringApplication.run(
                    TraningsregisterSpringH2Application.class,
                    args
            ));
        }
    }
}