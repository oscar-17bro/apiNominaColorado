package co.edu.udistrital.ApiNominaMunoz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prueba")
public class PruebaController {

	@GetMapping
	public String getMensage() {
		return "Ok Api Funcinando";
	} 
}
