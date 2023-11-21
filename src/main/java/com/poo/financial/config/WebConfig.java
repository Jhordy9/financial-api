package com.poo.financial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public LocaleResolver localeResolver() {
    FixedLocaleResolver localeResolver = new FixedLocaleResolver();
    localeResolver.setDefaultLocale(new Locale("en", "US"));
    return localeResolver;
  }

}
