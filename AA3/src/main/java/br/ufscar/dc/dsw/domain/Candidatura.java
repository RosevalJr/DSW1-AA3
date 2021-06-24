package br.ufscar.dc.dsw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@SuppressWarnings("serial")
@JsonIgnoreProperties(value = { "profissional", "vaga"})
@Entity
@Table(name = "Candidatura", 
	   uniqueConstraints = {@UniqueConstraint(columnNames = {"vaga_id", "profissional_id"})}
)
public class Candidatura extends AbstractEntity<Long>{
	
	@Column(nullable = false, length = 60)
	private String status = "ABERTO";
	
    @NotBlank(message = "{NotBlank.candidatura.curriculo}")
	@Column(nullable = false, length = 60)
	private String curriculo;
    
	@NotNull(message = "{NotNull.candidatura.vaga}")
	@ManyToOne
	@JoinColumn(name = "vaga_id")
	private Vaga vaga;
	
	@NotNull(message = "{NotNull.candidatura.profissional}")
	@ManyToOne
	@JoinColumn(name = "profissional_id")
	private Profissional profissional;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCurriculo() {
		return curriculo;
	}

	public void setCurriculo(String curriculo) {
		this.curriculo = curriculo;
	}
	
	public Vaga getVaga() {
		return vaga;
	}
	
	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}

	public Profissional getProfissional() {
		return profissional;
	}
	
	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
}
