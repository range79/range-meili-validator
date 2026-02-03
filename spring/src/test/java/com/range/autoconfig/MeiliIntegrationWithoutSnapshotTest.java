package com.range.autoconfig;

import com.range.meili.validator.MeiliStartupValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = MeiliStartupAutoConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Testcontainers
class MeiliIntegrationWithoutSnapshotTest {

    @Container
    static final GenericContainer<?> meili =
            new GenericContainer<>("getmeili/meilisearch:latest")
                    .withExposedPorts(7700)
                    .withCommand("meilisearch")
                    .waitingFor(
                            new MultiEndpointWaitStrategy(
                                    List.of("/health", "/indexes", "/stats")
                            ).withStartupTimeout(Duration.ofSeconds(120))
                    );

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add(
                "meili.startup.url",
                () -> "http://" + meili.getHost() + ":" + meili.getMappedPort(7700)
        );
        registry.add("meili.startup.timeout", () -> 60);
        registry.add("meili.startup.interval", () -> 1);
    }

    @Autowired
    MeiliStartupValidator validator;

    @Test
    void contextLoads_andValidatorBeanCreated() {
        assertThat(validator).isNotNull();
    }

    @Test
    void meiliIsFullyReady_withoutSnapshot() {
        validator.validate();
    }

    static class MultiEndpointWaitStrategy extends AbstractWaitStrategy {

        private final List<String> endpoints;

        MultiEndpointWaitStrategy(List<String> endpoints) {
            this.endpoints = endpoints;
        }

        @Override
        protected void waitUntilReady() {
            String host = waitStrategyTarget.getHost();
            int port = waitStrategyTarget.getMappedPort(7700);

            for (String endpoint : endpoints) {
                waitForEndpoint(host, port, endpoint);
            }
        }

        private void waitForEndpoint(String host, int port, String endpoint) {
            long deadline = System.currentTimeMillis() + startupTimeout.toMillis();

            while (System.currentTimeMillis() < deadline) {
                try {
                    HttpURLConnection con =
                            (HttpURLConnection) new URL(
                                    "http://" + host + ":" + port + endpoint
                            ).openConnection();

                    con.setRequestMethod("GET");
                    con.setConnectTimeout(500);
                    con.setReadTimeout(500);

                    if (con.getResponseCode() == 200) {
                        return;
                    }
                } catch (Exception ignored) {}

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(
                            "Interrupted while waiting for " + endpoint
                    );
                }
            }

            throw new IllegalStateException(
                    "Endpoint " + endpoint + " not ready within "
                            + startupTimeout.getSeconds() + "s"
            );
        }
    }
}
