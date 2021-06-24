package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.EmailService;
import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;

@RestController
public class ProfissionalRestController {

	
	@Autowired
	private IProfissionalDAO service;
	
	private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}
	
	private void parse(Empresa empresa, JSONObject json) {

		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				empresa.setId(((Integer) id).longValue());
			} else {
				empresa.setId((Long) id);
			}
		}

		empresa.setCNPJ((String) json.get("cnpj"));
		empresa.setName((String) json.get("name"));
	}
	
	@GetMapping(path = "/profissionais")
	public ResponseEntity<List<Profissional>> lista() {
		List<Profissional> lista = service.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(path = "/profissionais/{id}")
	public ResponseEntity<Profissional> lista(@PathVariable("id") long id) {
		Profissional profissional = service.findById(id);
		if (profissional == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(profissional);
	}
	
	@DeleteMapping(path = "/profissionais/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Profissional profissional = service.findById(id);
		if (profissional == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.deleteById(id);
			return ResponseEntity.noContent().build();
		}
	}
}
