package br.ufscar.dc.dsw;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Vaga;

@SpringBootApplication
public class Aa2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Aa2Application.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(IUsuarioDAO usuarioDAO, BCryptPasswordEncoder encoder, IProfissionalDAO profissionalDAO, IEmpresaDAO empresaDAO, IVagaDAO vagaDAO, ICandidaturaDAO candidaturaDAO) {
		return (args) -> {
						// Populando banco de dados.
						
						// Inserindo administrador
						Usuario u1 = new Usuario();
						u1.setUsername("admin");
						u1.setPassword(encoder.encode("admin"));
						u1.setName("Carlos da Silva");
						u1.setRole("admin");
						u1.setEnabled(true);
						usuarioDAO.save(u1);
						
						// Inserindo profissionais.
						Profissional p1 = new Profissional();
						p1.setCPF("717.076.210-20");
						p1.setSexo("Masculino");
						p1.setNascimento("25/06/2000");
						p1.setTelefone("55(39)80679-8860");
						p1.setUsername("rdmaljr@hotmail.com");
						p1.setPassword(encoder.encode("123password"));
						p1.setName("Roseval Junior");
						p1.setRole("userProfissional");
						p1.setEnabled(true);
						profissionalDAO.save(p1);
						
						Profissional p2 = new Profissional();
						p2.setCPF("171.153.090-51");
						p2.setSexo("Feminino");
						p2.setNascimento("15/04/1999");
						p2.setTelefone("55(77)86713-4261");
						p2.setUsername("marcela@hotmail.com");
						p2.setPassword(encoder.encode("123password"));
						p2.setName("Marcela Ribeiro");
						p2.setRole("userProfissional");
						p2.setEnabled(true);
						profissionalDAO.save(p2);
						
						Profissional p3 = new Profissional();
						p3.setCPF("807.705.070-00");
						p3.setSexo("Masculino");
						p3.setNascimento("02/01/1995");
						p3.setTelefone("55(79)76804-2305");
						p3.setUsername("jose@estudante.ufscar.br");
						p3.setPassword(encoder.encode("123password"));
						p3.setName("José da Silva");
						p3.setRole("userProfissional");
						p3.setEnabled(true);
						profissionalDAO.save(p3);
						
						// Inserindo empresa.
						Empresa e1 = new Empresa();
						e1.setCNPJ("66.520.072/0001-78");
						e1.setDescricao("Essa empresa vende windows.");
						e1.setCidade("São Carlos");
						e1.setUsername("microsoft@hotmail.com");
						e1.setPassword(encoder.encode("123password"));
						e1.setName("Microsoft");
						e1.setRole("userEmpresa");
						e1.setEnabled(true);
						empresaDAO.save(e1);
						
						Empresa e2 = new Empresa();
						e2.setCNPJ("79.611.145/0001-89");
						e2.setDescricao("Essa empresa vende programas;");
						e2.setCidade("Curitiba");
						e2.setUsername("roseval@estudante.ufscar.br");
						e2.setPassword(encoder.encode("123password"));
						e2.setName("Programas .Inc");
						e2.setRole("userEmpresa");
						e2.setEnabled(true);
						empresaDAO.save(e2);
						
						// Inserindo vagas.
						Vaga v1 = new Vaga();
						v1.setDescricao("Vendedor de windows.");
						v1.setRemuneracao(BigDecimal.valueOf(980.50));
						v1.setDatalimite("02/06/2022");
						v1.setEmpresa(e1);
						vagaDAO.save(v1);
						
						Vaga v2 = new Vaga();
						v2.setDescricao("Vendedor de programas.");
						v2.setRemuneracao(BigDecimal.valueOf(650.20));
						v2.setDatalimite("10/06/2022");
						v2.setEmpresa(e2);
						vagaDAO.save(v2);
						
						Vaga v3 = new Vaga();
						v3.setDescricao("Essa aqui vai dar fechada!");
						v3.setRemuneracao(BigDecimal.valueOf(15.40));
						v3.setDatalimite("01/01/2020");
						v3.setEmpresa(e2);
						vagaDAO.save(v3);
						
						Vaga v4 = new Vaga();
						v4.setDescricao("Mesmo dia fica aberta!");
						v4.setRemuneracao(BigDecimal.valueOf(300.26));
						v4.setDatalimite("09/06/2024");
						v4.setEmpresa(e2);
						vagaDAO.save(v4);
						
						// Inserindo candidaturas
						Candidatura c1 = new Candidatura();
						c1.setCurriculo("CurriculoRosevalJunior.pdf");
						c1.setProfissional(p1);
						c1.setVaga(v1);
						candidaturaDAO.save(c1);
						
						Candidatura c2 = new Candidatura();
						c2.setCurriculo("CurriculoMarcelaRibeiro.pdf");
						c2.setProfissional(p2);
						c2.setVaga(v2);
						candidaturaDAO.save(c2);
						
						/*Empresa e1 = new Empresa();
						e1.setUsername("Empresa1");
						e1.setPassword("senha123");
						e1.setName("EmpresaMassa");
						e1.setRole("userEmpresa");
						e1.setEnabled(true);
						e1.setCNPJ("235485698712518645");
						e1.setCidade("campinas");
						e1.setDescricao("belaempresa");
						empresaDAO.save(e1);
						
						Vaga v1 = new Vaga();
						v1.setDescricao("Vagamassa");
						v1.setDatalimite("25/02/2022");
						v1.setEmpresa(e1);
						v1.setRemuneracao(120.00);
						vagaDAO.save(v1);*/
		};
	}

}
