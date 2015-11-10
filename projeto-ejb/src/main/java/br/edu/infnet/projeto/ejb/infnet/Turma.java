package br.edu.infnet.projeto.ejb.infnet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.edu.infnet.projeto.ejb.core.BaseEntity;

@Entity
public class Turma extends BaseEntity<Long> {
	private static final long serialVersionUID = -2191517009190792630L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_turma")
	private Long id;
	
	public Turma() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}