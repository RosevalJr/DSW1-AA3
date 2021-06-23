package br.ufscar.dc.dsw.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.EmailService;
import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {
	
	@Autowired
	private IEmpresaDAO empresaDAO;
	
	@Autowired
	private IVagaDAO vagaDAO;
	
	@Autowired
	private ICandidaturaDAO candidaturaDAO;
	
	@Autowired
	private EmailService servicoEmail;
	
	@GetMapping("/listarVagas")
	public String listarEmpresas(ModelMap model, Principal principal) {
		
		Empresa empresaLogada = empresaDAO.findByUsername(principal.getName());
		
		List<Vaga> vagas = vagaDAO.findByEmpresa(empresaLogada);
		
		for(int i = 0 ; i < vagas.size(); i++) {
			System.out.println(vagas.get(i).getRemuneracao());
		}
		
		model.addAttribute("vagas", vagas);
		model.addAttribute("empresaLogada", empresaLogada);
		return "empresa/listaVaga";
	}
	
	@GetMapping("/cadastrarVagas")
	public String cadastrar(Vaga vaga, Principal principal) {
		Empresa empresaLogada = empresaDAO.findByUsername(principal.getName());
		vaga.setEmpresa(empresaLogada);
		return "empresa/cadastroVaga";
	}
	
	@PostMapping("/salvarVagas")
	public String salvar(@Valid Vaga vaga, BindingResult result, RedirectAttributes attr, Principal principal) throws ParseException {
		
		String[] partesData = vaga.getDatalimite().split("-");
		
		if(partesData.length == 3) {
			String dataCorreta = partesData[2] + "/" + partesData[1] + "/" + partesData[0];
			vaga.setDatalimite(dataCorreta);
		}

		if (result.hasErrors()) {
			return "empresa/cadastroVaga";
		}
		
		vagaDAO.save(vaga);
		
		attr.addFlashAttribute("sucess", "Vaga inserida com sucesso.");
		return "redirect:/empresas/listarVagas";
	}
	
	@GetMapping("/listarCandidaturas/{id}")
	public String editarProfissional(@PathVariable("id") Long id, ModelMap model, Principal principal) {
		Vaga vaga = vagaDAO.findById(id).get();
		Empresa empresa = empresaDAO.findByUsername(principal.getName());
		
		
		if(empresa.getId() != vaga.getEmpresa().getId()) {
			model.addAttribute("error", "403.error");
			model.addAttribute("message", "403.message");
			return "error";
		}
		
		List<Candidatura> candidaturas = candidaturaDAO.findByVaga(vaga);

		model.addAttribute("candidaturas", candidaturas);
		model.addAttribute("vaga", vaga);
		return "empresa/listaCandidatura";
	}
	
	@GetMapping("/analisarCandidaturas/{id}")
	public String analisarCandidatura(@PathVariable("id") Long id, ModelMap model, Principal principal) {
		Candidatura candidatura= candidaturaDAO.findById(id).get();
		Empresa empresa = empresaDAO.findByUsername(principal.getName());
		
		
		if(empresa.getId() != candidatura.getVaga().getEmpresa().getId()) {
			model.addAttribute("error", "403.error");
			model.addAttribute("message", "403.message");
			return "error";
		}
		
		//List<Candidatura> candidaturas = candidaturaDAO.findByVaga(vaga);

		model.addAttribute("candidatura", candidatura);
		//model.addAttribute("queroid", candidaturas);
		model.addAttribute("vaga", candidatura.getVaga());
		return "empresa/analisaCandidatura";
	}
	
	@PostMapping("/statusCandidatura/{id}")
	public String statusCandidatura(@PathVariable("id") Long id, @RequestParam("status") String status, @RequestParam("linkEntrevista") String linkEntrevista, @RequestParam("infoAdicional") String infoAdicional, ModelMap model, Principal principal) throws UnsupportedEncodingException {
		// Aqui e necessario alterar o status da candidatura.
		// Enviar o email correto para o concorrente da vaga.
		Candidatura candidatura = candidaturaDAO.findById(id).get();
		Empresa empresa = empresaDAO.findByUsername(principal.getName());
		
		
		if(empresa != candidatura.getVaga().getEmpresa()) {
			model.addAttribute("error", "403.error");
			model.addAttribute("message", "403.message");
			return "error";
		}
		
        InternetAddress from = new InternetAddress("aa1seedsw1@gmail.com", "AA2");
	    InternetAddress to = new InternetAddress(candidatura.getProfissional().getUsername(), candidatura.getProfissional().getName());
		
		if(status.equals("NaoSelecionado")) {
			candidatura.setStatus("Não Selecionado");
			String cabecalho = "NÃO SELECIONADO !";
			String corpo = "INFELIZMENTE VOCÊ NÃO FOI SELECIONADO.";
			servicoEmail.send(from, to, cabecalho, corpo);
		}
		else {
			candidatura.setStatus("Entrevista");
			String cabecalho = "SELECIONADO !";
			String corpo = "LINK DA ENTREVISTA: " + linkEntrevista + "\n";
			corpo = corpo + infoAdicional;
			servicoEmail.send(from, to, cabecalho, corpo);
			
		}
		
		candidaturaDAO.save(candidatura);

		return "redirect:/empresas/listarCandidaturas/" + String.valueOf(candidatura.getVaga().getId());
	}
	
}
