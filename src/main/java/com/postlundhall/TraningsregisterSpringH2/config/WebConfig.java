package com.postlundhall.TraningsregisterSpringH2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration class for managing website language sv-SE or en-US via Locale.
 * @author postlundhall
 * @since 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures a SessionLocaleResolver to set default language Locale to "sv-SE".
     * @return SessionLocaleResolver instance for setting the default language.
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("sv-SE")); // Default to Swedish (Sweden)
        return resolver;
    }

    /**
     * Configures a LocaleChangeInterceptor to intercept LocaleChanges
     * and switching to the locale's corresponding language (Swedish or English)
     * @return LocaleChangeInterceptor for changing language locale using the "lang"-parameter.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // Change locale via ?lang=sv_SE
        return interceptor;
    }

    /**
     * Registers a LocaleChangeInterceptor to handle locale changes in the application.
     * The interceptor is added to the Spring MVC interceptor registry, enabling locale switching.
     * @param registry the InterceptorRegistry to which the LocaleChangeInterceptor is added
     */
        @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}