package com.spe.calc_proj;

import java.util.Map;

public class ResponseMessage {
    private Map<String, String> info;

    public ResponseMessage() {}

    public ResponseMessage(Map<String, String> info) {
        this.info = info;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }
}