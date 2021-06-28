package br.ufscar.dc.dsw.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Vaga;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {
	
	@Autowired
	private IProfissionalDAO profissionalDAO;
	
	@Autowired
	private ICandidaturaDAO candidaturaDAO;
	
	@Autowired
	private IVagaDAO vagaDAO;
	
	@Autowired
	ServletContext context;
	
	@GetMapping("/listarCandidaturas")
	public String listarCandidaturas(ModelMap model, Principal principal) {
		
		Profissional profissionalLogado = profissionalDAO.findByUsername(principal.getName());
		
		List<Candidatura> candidaturas = candidaturaDAO.findByProfissional(profissionalLogado);
		
		model.addAttribute("candidaturas", candidaturas);
		model.addAttribute("profissionalLogado", profissionalLogado);
		return "profissional/listaCandidatura";
	}
	
	@GetMapping("/aplicar")
	public String aplicar(ModelMap model, Principal principal) throws java.text.ParseException {
		Profissional profissional = profissionalDAO.findByUsername(principal.getName());
		List<Vaga> todasVagas = vagaDAO.findAll();
		List<Vaga> vagasAbertas =  new ArrayList<Vaga>();
		
		for(int i = 0; i < todasVagas.size(); i++) {
			if(todasVagas.get(i).isAberta())
				if(candidaturaDAO.findByProfissionalAndVaga(profissional, todasVagas.get(i)) == null)
					vagasAbertas.add(todasVagas.get(i));
		}
		
		model.addAttribute("vagas", vagasAbertas);
		return "profissional/aplicar";
	}
	
	@GetMapping("/aplicarVaga/{id}")
	public String aplicarCurriculo(@PathVariable("id") long id, ModelMap model, Principal principal) throws java.text.ParseException {
		
		Vaga vaga = vagaDAO.findById(id);
		
		if(vaga.isAberta()) {
			model.addAttribute("vaga", vaga);
			return "profissional/listaVaga";
		}
		else
			return "redirect:/profissionais/aplicar";
	}
	
	
	// Aqui chega o arquivo em file por um post
	@PostMapping("/uploadFile/{id}")
	public String aplicarVaga(@RequestParam("file") MultipartFile file, @PathVariable("id") long id, ModelMap model, Principal principal, RedirectAttributes attr) throws IllegalStateException, IOException {
		
		String fileName = file.getOriginalFilename();
		
		String uploadPath = context.getRealPath("") + File.separator + "upload";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		file.transferTo(new File(uploadDir, fileName));
		
		attr.addFlashAttribute("sucess", "File " + fileName + " has uploaded successfully!");
		
		
		
		Profissional profissional = profissionalDAO.findByUsername(principal.getName());
		Vaga vaga = vagaDAO.findById(id);
		

		Candidatura candidatura = new Candidatura();
		candidatura.setCurriculo(fileName);
		candidatura.setProfissional(profissional);
		candidatura.setVaga(vaga);
		candidatura.setStatus("ABERTO");
		candidaturaDAO.save(candidatura);
		
		return "redirect:/profissionais/aplicar";
	}
	
	@GetMapping("/excluirCandidatura/{id}")
	public String excluirCandidatura(@PathVariable("id") long id, ModelMap model, Principal principal) {
		Candidatura candidatura = candidaturaDAO.findById(id);
		Profissional profissional = profissionalDAO.findByUsername(principal.getName());
		
		if(candidatura.getProfissional() != profissional) {
			model.addAttribute("error", "403.error");
			model.addAttribute("message", "403.message");
			return "error";
		}
		
		candidaturaDAO.deleteById(id);
		
		return "redirect:/profissionais/listarCandidaturas";
	}
}
