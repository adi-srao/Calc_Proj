package com.spe.calc_proj;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class CalcProjApplicationIntegrationTest {

    @Test
    @DisplayName("application starts and exposed calculator endpoints work end-to-end")
    void contextLoadsAndCalculatorEndpointsWork() throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(CalcProjApplication.class, "--server.port=0")) {
            int port = Integer.parseInt(context.getEnvironment().getProperty("local.server.port", "8080"));
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest addRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + "/api/v1/add?a=7&b=3"))
                    .GET()
                    .build();

            HttpResponse<String> addResponse = client.send(addRequest, HttpResponse.BodyHandlers.ofString());
            assertThat(addResponse.statusCode()).isEqualTo(200);
            assertThat(addResponse.body()).contains("\"operation\":\"add\"");
            assertThat(addResponse.body()).contains("\"result\":\"10.0\"");

            HttpRequest divideRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + "/api/v1/divide?a=8&b=0"))
                    .GET()
                    .build();

            HttpResponse<String> divideResponse = client.send(divideRequest, HttpResponse.BodyHandlers.ofString());
            assertThat(divideResponse.statusCode()).isEqualTo(400);
            assertThat(divideResponse.body()).contains("Arithmetic Error - Division by zero is not allowed.");
        }
    }
}
