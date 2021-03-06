package com.dalbit.config;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HTTPS를 사용하기 위한 설정
 * springboot의 특성상 내장 tomcat을 사용하는데 내장 톰캣은 1개의 포트만 제공하고 있어
 * 추가로 포트를 사용하려면 커스터마이징이 필요함.
 */
@Configuration
public class ServletConfig {

    @Value("${server.http.port}")
    private int serverHttpPort;

    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcatServletWebServerFactory;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (serverFactory) -> serverFactory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }

    private Connector createStandardConnector(){
        Connector connector = new Connector();
        connector.setPort(serverHttpPort);
        return connector;
    }
}
