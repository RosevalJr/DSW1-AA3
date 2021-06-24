package br.ufscar.dc.dsw.controller;

import java.io.IOException;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Empresa;

@RestController
public class EmpresaRestController {
	
	@Autowired
	private IEmpresaDAO service;
	
	private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}
	
	private void parse(Empresa empresa, JSONObject json) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				empresa.setId(((Integer) id).longValue());
			} else {
				empresa.setId((Long) id);
			}
		}
		
		if(json.get("cnpj") != null)
			empresa.setCNPJ((String) json.get("cnpj"));
		if(json.get("name") != null)
			empresa.setName((String) json.get("name"));
		if(json.get("cidade") != null)
			empresa.setCidade((String) json.get("cidade"));
		if(json.get("descricao") != null)
			empresa.setDescricao((String) json.get("descricao"));
		if(json.get("username") != null)
			empresa.setUsername((String) json.get("username"));
		if(json.get("password") != null)
			empresa.setPassword(encoder.encode((String) json.get("password")));
		empresa.setRole("userEmpresa");
		empresa.setEnabled(true);
		
	}
	
	@GetMapping(path = "/empresas")
	public ResponseEntity<List<Empresa>> lista() {
		List<Empresa> lista = service.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(path = "/empresas/{id}")
	public ResponseEntity<Empresa> lista(@PathVariable("id") long id) {
		Empresa empresa = service.findById(id);
		if (empresa == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(empresa);
	}
	
	@GetMapping(path = "/empresas/cidades/{nome}")
	public ResponseEntity<List<Empresa>> lista(@PathVariable("nome") String nome) {
		List<Empresa> empresas = service.findByCidade(nome);
		if (empresas == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(empresas);
	}
	
	@DeleteMapping(path = "/empresas/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Empresa empresa = service.findById(id);
		if (empresa == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.deleteById(id);
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping(path = "/empresas")
	@ResponseBody
	public ResponseEntity<Empresa> cria(@RequestBody JSONObject json){
		try {
			if(isJSONValid(json.toString())){
				Empresa empresa = new Empresa();
				parse(empresa, json);
				service.save(empresa);
				return ResponseEntity.ok(empresa);
			} else {
				return ResponseEntity.badRequest().body(null);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	
	@PutMapping(path = "/empresas/{id}")
	public ResponseEntity<Empresa> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if(isJSONValid(json.toString())){
				Empresa empresa = service.findById(id);
				if(empresa == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(empresa, json);
					service.save(empresa);
					return ResponseEntity.ok(empresa);
				}
			} else { 
				return ResponseEntity.badRequest().body(null);
			}
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
}
