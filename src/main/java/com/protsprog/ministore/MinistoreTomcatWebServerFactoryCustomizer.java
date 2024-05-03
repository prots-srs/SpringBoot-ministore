package com.protsprog.ministore;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class MinistoreTomcatWebServerFactoryCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory server) {
        // server.addConnectorCustomizers((connector) ->
        // connector.setAsyncTimeout(Duration.ofSeconds(20).toMillis()));
        // server.setPort(80);
    }
}
