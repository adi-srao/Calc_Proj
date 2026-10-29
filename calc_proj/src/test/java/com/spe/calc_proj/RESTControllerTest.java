package com.spe.calc_proj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RESTControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RESTController()).build();
    }

    @Test
    @DisplayName("add endpoint returns sum for valid inputs")
    void testAdd() throws Exception {
        mockMvc.perform(get("/api/v1/add").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.operation").value("add"))
                .andExpect(jsonPath("$.info.a").value("10.0"))
                .andExpect(jsonPath("$.info.b").value("5.0"))
                .andExpect(jsonPath("$.info.result").value("15.0"));
    }

    @Test
    @DisplayName("defaults are used when no query parameters are supplied")
    void testAddUsesDefaultValues() throws Exception {
        mockMvc.perform(get("/api/v1/add"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.operation").value("add"))
                .andExpect(jsonPath("$.info.a").value("0.0"))
                .andExpect(jsonPath("$.info.b").value("0.0"))
                .andExpect(jsonPath("$.info.result").value("0.0"));
    }

    @Test
    @DisplayName("subtract endpoint returns difference for valid inputs")
    void testSubtract() throws Exception {
        mockMvc.perform(get("/api/v1/subtract").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.operation").value("subtract"))
                .andExpect(jsonPath("$.info.result").value("5.0"));
    }

    @Test
    @DisplayName("multiply endpoint returns product for valid inputs")
    void testMultiply() throws Exception {
        mockMvc.perform(get("/api/v1/multiply").param("a", "4").param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.operation").value("multiply"))
                .andExpect(jsonPath("$.info.result").value("12.0"));
    }

    @Test
    @DisplayName("divide endpoint returns quotient for valid inputs")
    void testDivideSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "10").param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.operation").value("divide"))
                .andExpect(jsonPath("$.info.result").value("5.0"));
    }

    @Test
    @DisplayName("divide by zero returns a bad request error message")
    void testDivideByZero() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "10").param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Arithmetic Error - Division by zero is not allowed."));

        assertThat("Arithmetic Error - Division by zero is not allowed.")
                .contains("Division by zero");
    }
}