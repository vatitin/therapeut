package com.acme.therapeut;

import com.acme.therapeut.repository.MitgliedRestRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

/**
 * Beans für die REST-Schnittstelle zu "kunde" (WebClient) und für die GraphQL-Schnittstelle zu "kunde"
 * (HttpGraphQlClient).
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
interface HttpClientConfig {
    String GRAPHQL_PATH = "/graphql";
    int MITGLIED_DEFAULT_PORT = 8080;
    int TIMEOUT_IN_SECONDS = 10;

    @Bean
    default WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @SuppressWarnings("CallToSystemGetenv")
    default UriComponentsBuilder uriComponentsBuilder() {
        // Umgebungsvariable in Kubernetes
        final var mitgliedHostEnv = System.getenv("MITGLIED_SERVICE_HOST");
        final var mitgliedPortEnv = System.getenv("MITGLIED_SERVICE_PORT");

        @SuppressWarnings("VariableNotUsedInsideIf")
        final var schema = mitgliedHostEnv == null ? "https" : "http";
        final var mitgliedHost = mitgliedHostEnv == null ? "localhost" : mitgliedHostEnv;
        final int mitgliedPort = mitgliedPortEnv == null ? MITGLIED_DEFAULT_PORT : Integer.parseInt(mitgliedPortEnv);

        final var log = LoggerFactory.getLogger(HttpClientConfig.class);
        log.error("mitgliedHost: {}, mitgliedPort: {}", mitgliedHost, mitgliedPort);

        return UriComponentsBuilder.newInstance()
            .scheme(schema)
            .host(mitgliedHost)
            .port(mitgliedPort);
    }

    // siehe org.springframework.web.reactive.function.client.DefaultWebClient
    @Bean
    default WebClient webClient(
        final WebClient.Builder webClientBuilder,
        final UriComponentsBuilder uriComponentsBuilder,
        final WebClientSsl ssl
    ) {
        final var baseUrl = uriComponentsBuilder.build().toUriString();
        return webClientBuilder
            .baseUrl(baseUrl)
            // siehe Property spring.ssl.bundle.jks.microservice
            // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.ssl
            // https://spring.io/blog/2023/06/07/securing-spring-boot-applications-with-ssl
            .apply(ssl.fromBundle("microservice"))
            .build();
    }

    @Bean
    default MitgliedRestRepository mitgliedRestRepository(final WebClient builder) {
        final var clientAdapter = WebClientAdapter.forClient(builder);
        final var proxyFactory = HttpServiceProxyFactory
            .builder(clientAdapter)
            .blockTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
            .build();
        return proxyFactory.createClient(MitgliedRestRepository.class);
    }

    // siehe org.springframework.graphql.client.DefaultHttpGraphQlClientBuilder.DefaultHttpGraphQlClient
    @Bean
    default HttpGraphQlClient graphQlClient(
        final WebClient.Builder webClientBuilder,
        final UriComponentsBuilder uriComponentsBuilder
    ) {
        final var baseUrl = uriComponentsBuilder
            .path(GRAPHQL_PATH)
            .build()
            .toUriString();
        final var webclient = webClientBuilder
            .baseUrl(baseUrl)
            .build();
        return HttpGraphQlClient.builder(webclient).build();
    }
}
