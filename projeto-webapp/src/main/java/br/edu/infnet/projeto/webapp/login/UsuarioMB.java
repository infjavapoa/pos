package br.edu.infnet.projeto.webapp.login;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.infnet.projeto.ejb.core.InfnetException;
import br.edu.infnet.projeto.ejb.core.Repositorio;
import br.edu.infnet.projeto.ejb.core.Seguranca;
import br.edu.infnet.projeto.ejb.usuario.Usuario;
import br.edu.infnet.projeto.ejb.usuario.UsuarioEJB;

@ManagedBean
@ViewScoped
public class UsuarioMB {
	@EJB
    private Repositorio repositorio;
	@EJB 
	private UsuarioEJB usuarioEJB;
	
    private Usuario usuario;
    private Usuario logado;
    private List<Usuario> listaUsuarios;
    private String senha;
    private String conf;
    
    @PostConstruct
    public void init() {
    	atualizaView();
    }
    
    public Usuario getUsuario() {
		return usuario;
	}
    
	public void setUsuario(Usuario usuario) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		this.usuario = usuario;		
	}
	
	public void atualizaView(){
		listaUsuarios = repositorio.listar(Usuario.class);
		if (FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() != null) {
	    	Map<String, Object> param = new HashMap<String, Object>();
			param.put("email", FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
	    	this.setLogado(repositorio.obterWithNamedQuery(Usuario.class, "Usuario.pesquisarPorEmail", param));
    	}
	}
    
    public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void salvar() {
    	try {    		
    		usuarioEJB.salvarUsuario(usuario);
    		atualizaView();
    	}
    	catch (InfnetException ex){
    		ex.printStackTrace();
        	FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", ex.getMessage()));
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
        	FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", ex.getMessage()));
    	}
    }
	
	public void remover(Usuario u) {
    	try {
    		usuarioEJB.removerUsuario(u);
    		atualizaView();
    	}
    	catch (InfnetException ex){
    		ex.printStackTrace();
        	FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", ex.getMessage()));
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
        	FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", ex.getMessage()));
    	}
    }

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) throws UnsupportedEncodingException, NoSuchAlgorithmException {		
		this.conf = Seguranca.gerarHash(conf);
	}
//Essa gambi é porque se colocar o hash na usuario ele vai fazer o hash duas vezes
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		this.senha = Seguranca.gerarHash(senha);
	}
	
	public void alterar() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		this.setSenha(logado.getSenha());
		logado.setSenha(this.getSenha());
		if (!logado.getSenha().equals(this.getConf()))
		{
			System.out.println(logado.getSenha() + " - " + this.getConf());

			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Para usuários cadastrados só é permitido alterar a senha!"));
		}
		try {
			usuarioEJB.salvarUsuario(logado);
		} catch (InfnetException e) {
			e.printStackTrace();
        	FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
		}
    	FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Senha alterada com sucesso!"));

	}

	public Usuario getLogado() {
		return logado;
	}

	public void setLogado(Usuario logado) {
		this.logado = logado;
	}
	
	
}