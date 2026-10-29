package com.spe.calc_proj;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResponseMessageTest {

    @Test
    @DisplayName("ResponseMessage stores and exposes info map values")
    void shouldStoreAndExposeInfoMap() {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("operation", "add");
        expected.put("result", "9.0");

        ResponseMessage responseMessage = new ResponseMessage(expected);

        assertThat(responseMessage.getInfo()).isEqualTo(expected);

        Map<String, String> updated = new LinkedHashMap<>();
        updated.put("operation", "subtract");
        updated.put("result", "1.0");

        responseMessage.setInfo(updated);

        assertThat(responseMessage.getInfo()).isEqualTo(updated);
    }
}
