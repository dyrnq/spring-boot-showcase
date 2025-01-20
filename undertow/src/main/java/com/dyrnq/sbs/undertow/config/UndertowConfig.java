package com.dyrnq.sbs.undertow.config;

import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;
import org.xnio.Options;

@Configuration
public class UndertowConfig {

    @Bean
    public UndertowBuilderCustomizer undertowBuilderCustomizer() {
        return builder -> {
            builder.setSocketOption(Options.KEEP_ALIVE, true);
            builder.setServerOption(UndertowOptions.ALWAYS_SET_DATE, true);
        };
    }
    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        ForwardedHeaderTransformer transformer = new ForwardedHeaderTransformer();
        return transformer;
    }
}
