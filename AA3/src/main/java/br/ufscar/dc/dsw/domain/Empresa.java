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

@SuppressWarnings("serial")
@Entity
@Table(name = "Empresa")
public class Empresa extends Usuario {
	
	@NotBlank(message = "{NotBlank.empresa.cnpj}")
	@Size(min = 18, max = 18, message = "{Size.empresa.cnpj}")
	@Column(nullable = false, unique = true, length = 60)
	private String CNPJ;
	
	@NotBlank(message = "{NotBlank.empresa.cidade}")
	@Column(nullable = false, length = 60)
	private String cidade;
	
	@NotBlank(message = "{NotBlank.empresa.descricao}")
	@Column(nullable = false, length = 256)
	private String descricao;
    
	@OneToMany(mappedBy = "empresa")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Vaga> vagas;
	
	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}
	
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
		
	public List<Vaga> getVagas() {
		return vagas;
	}

	public void setVagas(List<Vaga> vagas) {
		this.vagas = vagas;
	}
}
