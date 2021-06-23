package br.ufscar.dc.dsw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuario")
public class Usuario extends AbstractEntity<Long> {
	
	@NotBlank(message = "{NotBlank.usuario.username}")
	@Size(min = 5, max = 64, message = "{Size.usuario.username}")
    @Column(nullable = false, length = 64, unique = true)
    private String username;
    
	@NotBlank(message = "{NotBlank.usuario.password}")
    @Column(nullable = false, length = 64)
    private String password;
       
    @NotBlank(message = "{NotBlank.usuario.name}")
    @Column(nullable = false, length = 60)
    private String name;
    
    @NotBlank(message = "{NotBlank.usuario.role}")
    @Column(nullable = false, length = 16)
    private String role;
    
    @Column(nullable = false)
    private boolean enabled;
		
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}