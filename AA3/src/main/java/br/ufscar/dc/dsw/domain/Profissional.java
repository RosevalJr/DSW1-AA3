package br.ufscar.dc.dsw.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(value = { "candidaturas" })
@Entity
@Table(name = "Profissional")
public class Profissional extends Usuario {
	
	@NotBlank(message = "{NotBlank.profissional.cpf}")
	@Size(min = 14, max = 14, message = "{Size.profissional.cpf}")
    @Column(nullable = false, length = 14, unique = true)
    private String CPF;
    
	@NotBlank(message = "{NotBlank.profissional.telefone}")
    @Size(min = 16, max = 16, message = "{Size.profissional.telefone}")
    @Column(nullable = false, length = 15)
    private String telefone;
    
    @Column(nullable = false, length = 13)
    private String sexo = "Indeterminado";
    
    @NotBlank(message = "{NotBlank.profissional.nascimento}")
	@Column(nullable = false, length = 19)
	private String nascimento;
    	
	@OneToMany(mappedBy = "profissional")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Candidatura> candidaturas;
    
	public String getCPF() {
		return CPF;
	}

	public void setCPF(String CPF) {
		this.CPF = CPF;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}
		
	public List<Candidatura> getCandidaturas(){
		return candidaturas;
	}
	
	public void setCandidaturas(List<Candidatura> candidaturas) {
		this.candidaturas = candidaturas;
	}
}
