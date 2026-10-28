package main.java.com.spe.calc_proj;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {
    
    @GetMapping("/api/v1/add")
    public ResponseEntity<ResponseMessage> add(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        Map<String, String> info = new HashMap<>();
        info.put("operation", "add");
        info.put("a", String.valueOf(a));
        info.put("b", String.valueOf(b));
        info.put("result", String.valueOf(a + b));

        ResponseMessage msg = new ResponseMessage(info);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/api/v1/subtract")
    public ResponseEntity<ResponseMessage> subtract(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        Map<String, String> info = new HashMap<>();
        info.put("operation", "subtract");
        info.put("a", String.valueOf(a));
        info.put("b", String.valueOf(b));
        info.put("result", String.valueOf(a - b));

        ResponseMessage msg = new ResponseMessage(info);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/api/v1/multiply")
    public ResponseEntity<ResponseMessage> multiply(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "0") int b) {
        Map<String, String> info = new HashMap<>();
        info.put("operation", "multiply");
        info.put("a", String.valueOf(a));
        info.put("b", String.valueOf(b));
        info.put("result", String.valueOf(a * b));

        ResponseMessage msg = new ResponseMessage(info);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/api/v1/divide")
    public ResponseEntity<ResponseMessage> divide(@RequestParam(defaultValue = "0") int a, @RequestParam(defaultValue = "1") int b) {
        if (b == 0) 
            return ResponseEntity.badRequest().body("Arithmetic Error - Division by zero is not allowed.");

        Map<String, String> info = new HashMap<>();
        info.put("operation", "divide");
        info.put("a", String.valueOf(a));
        info.put("b", String.valueOf(b));
        info.put("result", String.valueOf((double) a / b));

        ResponseMessage msg = new ResponseMessage(info);
        return ResponseEntity.ok(msg);
    }
}
