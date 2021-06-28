package br.ufscar.dc.dsw.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Empresa;

@RestController
public class VagaRestController {

	@Autowired
	private IVagaDAO serviceVaga;
	
	@Autowired
	private IEmpresaDAO serviceEmpresa;
	
	@GetMapping(path = "/vagas")
	public ResponseEntity<List<Vaga>> lista() {
		List<Vaga> lista = serviceVaga.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(path = "/vagas/{id}")
	public ResponseEntity<Vaga> listaLocacao(@PathVariable("id") long id) {
		Vaga vaga = serviceVaga.findById(id);
		
		if (vaga == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(vaga);
	}
	
	@GetMapping(path = "/vagas/empresas/{id}")
	public ResponseEntity<List<Vaga>> lista(@PathVariable("id") long id) throws ParseException {
		Empresa empresa = serviceEmpresa.findById(id);
		List<Vaga> vagas = serviceVaga.findByEmpresa(empresa);
		
		List<Vaga> vagasAbertas = new ArrayList<>();
		
		for(int i = 0 ; i < vagas.size(); i++)
			if(vagas.get(i).isAberta())
				vagasAbertas.add(vagas.get(i));
		
		if (vagasAbertas.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(vagasAbertas);
		
	}
	
}
