package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Vaga;

import java.text.Normalizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vagas")
public class VagaController {
	
	@Autowired
	private IVagaDAO vagaDAO;
	
	@PostMapping("/listar")
	public String listar(ModelMap model) throws ParseException {
		System.out.println("ENTREI AQUI CARA!");
		
		List<Vaga> todasVagas = vagaDAO.findAll();
		List<Vaga> vagasAbertas =  new ArrayList<Vaga>();
		
		for(int i = 0; i < todasVagas.size(); i++) {
			if(todasVagas.get(i).isAberta())
				vagasAbertas.add(todasVagas.get(i));
		}
		
		model.addAttribute("vagas", vagasAbertas);
		return "vagas/listarTodas";
	}
	
	@PostMapping("/buscar")
	public String buscar(@RequestParam(name="nome") String cidade, ModelMap model) throws ParseException {
		
		List<Vaga> todasVagas = vagaDAO.findAll();
		List<Vaga> vagasAbertas =  new ArrayList<Vaga>();
		
		cidade = Normalizer.normalize(cidade, Normalizer.Form.NFD);
		cidade = cidade.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		cidade = cidade.toUpperCase();
		
		for(int i = 0; i < todasVagas.size(); i++) {
			if(todasVagas.get(i).isAberta()) {
				String cidadeVaga = todasVagas.get(i).getEmpresa().getCidade();
				cidadeVaga = Normalizer.normalize(cidadeVaga, Normalizer.Form.NFD);
				cidadeVaga = cidadeVaga.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
				cidadeVaga = cidadeVaga.toUpperCase();

				if(cidadeVaga.equals(cidade))
					vagasAbertas.add(todasVagas.get(i));
			}
		}
		
		model.addAttribute("vagas", vagasAbertas);
		return "vagas/listarTodas";
	}
}
