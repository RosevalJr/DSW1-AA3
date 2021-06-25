package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(value = { "candidaturas"})
@Entity
@Table(name = "Vaga")
public class Vaga extends AbstractEntity<Long>{
	
	@NotBlank(message = "{NotBlank.vaga.descricao}")
	@Column(nullable = false, length = 256)
	private String descricao;
	
    @NotBlank(message = "{NotBlank.vaga.datalimite}")
	@Column(nullable = false, length = 19)
	private String datalimite;
    
	@NotNull(message = "{NotNull.vaga.remuneracao}")
	@Column(nullable = false, columnDefinition = "DECIMAL(8,2) DEFAULT 0.0")
	private BigDecimal remuneracao;
	
	@NotNull(message = "{NotNull.vaga.empresa}")
	@ManyToOne
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;
	
	@OneToMany(mappedBy = "vaga")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Candidatura> candidaturas;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDatalimite() {
		return datalimite;
	}

	public void setDatalimite(String dataLimite) {
		this.datalimite = dataLimite;
	}
	
	public BigDecimal getRemuneracao() {
		return remuneracao;
	}

	public void setRemuneracao(BigDecimal remuneracao) {
		this.remuneracao = remuneracao;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public List<Candidatura> getCandidaturas(){
		return candidaturas;
	}
	
	public void setCandidaturas(List<Candidatura> candidaturas) {
		this.candidaturas = candidaturas;
	}
	
	/* Checa se a vaga ainda esta aberta. */
	public boolean isAberta() throws ParseException {
		SimpleDateFormat formato;
		Date dateLimite;

		formato = new SimpleDateFormat("dd/MM/yyyy");
		
		
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format1.format(cal.getTime());
        
        
		dateLimite = formato.parse(datalimite);
		Date dateHoje = formato.parse(formatted);
		
		
		if(dateHoje.compareTo(dateLimite) > 0) {
			return false;
		}else if(dateHoje.compareTo(dateLimite) < 0) {
			return true;
		}else return true;
		
	}
}
