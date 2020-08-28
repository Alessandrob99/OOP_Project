package ProgettoPO.ProgettoProgrammazione.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

	
	
	@GetMapping("/hello")
	public String Risorsa1(@RequestParam(name = "param", defaultValue = "zibibbo") String param) {
		return "Ciao questo è il get";
	}
	@PostMapping("/hello")
	public String Risorsa2(@RequestBody String body) {
		return "Ciao Questo è il post";
	}
}
