package sk.kosickaakademia.deco.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MathController {

    @PostMapping(path = "/add")
    public ResponseEntity<String> sum(@RequestBody String body){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject =(JSONObject) parser.parse(body);
            int a = Integer.parseInt(String.valueOf(jsonObject.get("a")));
            int b = Integer.parseInt(String.valueOf(jsonObject.get("b")));
            //long b = (long) jsonObject.get("b");

            JSONObject result = new JSONObject();
            result.put("result", a + b);

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(result.toJSONString());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            JSONObject errorJson = new JSONObject();
            errorJson.put("error","Incorrect body of request.");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(errorJson.toJSONString());
        }

        return null;
    }
    
    @GetMapping(path = "/multiply")
    public ResponseEntity<String> multiply(@RequestParam(value = "a") int value1, @RequestParam(value = "a") int value2){
        int result = value1 * value2;
        JSONObject resultJson = new JSONObject();
        resultJson.put("result", result);

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(resultJson.toJSONString());
    }
}
