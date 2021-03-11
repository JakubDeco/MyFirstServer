package sk.kosickaakademia.deco.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.deco.database.JokeDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class JokeController {
    private List<String> jokes = new ArrayList<>();

    public JokeController() {
        jokes.add("Hear about the new restaurant Karma? ... There's no menu, you get what you deserve.");
        jokes.add("Why don't scientist trust atoms? ... Because they make up everything.");
        jokes.add("How do you drown a hipster? ... Throw him in the mainstream.");
    }

    @GetMapping(path = "/jokes")
    public ResponseEntity<String> getJokes(){
        JSONObject root = new JSONObject();
        JSONArray array = new JSONArray();
        for (String joke :
                jokes) {
            array.add(joke);
        }
        root.put("jokes",array);

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(root.toJSONString());
    }

    @GetMapping(path = "/joke/{id}")
    public ResponseEntity<String> getJokeByID(@PathVariable int id){
        JokeDatabase database = new JokeDatabase();
        String joke = database.getJoke(id);
        JSONObject object = new JSONObject();
        if (!joke.equals("")){
            object.put("joke",joke);

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(object.toJSONString());
        }

        object.put("error", "Wrong request");
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(object.toJSONString());
    }

    @GetMapping(path = "/joke")
    public ResponseEntity<String> getRandomJoke(){
        JokeDatabase database = new JokeDatabase();
        JSONObject root = new JSONObject();
        root.put("joke", database.getRandomJoke());

        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(root.toJSONString());
    }

    @PostMapping(path = "/addJoke")
    public ResponseEntity<String> addJoke(@RequestBody String body){
        JSONObject response = new JSONObject();
        int status;
        try {
            JSONObject jsonObject =(JSONObject) new JSONParser().parse(body);
            JokeDatabase database = new JokeDatabase();
            String newJoke = (String) jsonObject.get("joke");
            if (newJoke == null || newJoke.isEmpty()){
                response.put("error","Incorrect body of request.");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
            }

            if (database.insertJoke(newJoke)){
                status = 201;
                response.put("info","Joke successfully added.");
            } else {
                status = 500;
                response.put("info","Joke successfully added.");
            };
            return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
        } catch (ParseException e) {
            response.put("error","Incorrect body of request.");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(response.toJSONString());
        }

    }
}
