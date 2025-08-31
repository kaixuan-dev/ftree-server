package org.example.ftree.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Configuration
@ConfigurationProperties("project.none-auth")
public class NoneAuthUri {

    @Setter
    @Getter
    private Set<String> uris;


    @PostConstruct
    public void init() {
        if ((Objects.isNull(uris))) {
            uris = new HashSet<>();
        }
        uris.forEach(uri -> {
            log.info("none auth uri: {}", uri);
        });
    }

    public boolean isNoneAuthUri(String uri) {
        return uris.contains(uri);
    }

}
