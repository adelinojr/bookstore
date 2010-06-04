package br.com.bookstore.managedbean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.richfaces.component.html.HtmlDataTable;

import br.com.bookstore.model.exceptions.LivroException;
import br.com.bookstore.model.livro.Genero;
import br.com.bookstore.model.livro.Livro;
import br.com.bookstore.service.ServiceBookStore;
import br.com.bookstore.service.ServiceLocator;



public class LivroBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServiceBookStore livroFacade;
	private Livro livro ;
	private String busca;
	
	
	private transient HtmlDataTable datatable;
	
	public LivroBean (){

		try {
			/*Context initialContext = new InitialContext();
			this.livroFacade = (LivroBeanModel) initialContext.lookup("BookStoreEAR/LivroBeanGen/local");*/
			
			this.livroFacade = (ServiceBookStore) ServiceLocator.getInstance().getLocalEJB(ServiceBookStore.class);
			
			} catch (NamingException e) {
			/* Se alguma coisa acontecer aqui, é porque não foi possível fazer lookup,
			 * ou não foi possível criar o initial context.
			 * */
			e.printStackTrace();
		}		
	   this.livro = new  Livro();
	}
	
	public  org.richfaces.component.html.HtmlDataTable getDatatable() {
		return datatable;
	}


	public void setDatatable( org.richfaces.component.html.HtmlDataTable datatable) {
		this.datatable = datatable;
	}


	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	
	public String getBusca() {
		return busca;
	}


	public void setBusca(String busca) {
		this.busca = busca;
	}


	public String cadastra(){
		try {
			this.livroFacade.cadastrarLivro(this.livro);
		} catch (LivroException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "sucesso";
	}
	
	public String excluirLivro(){
		this.livro = (Livro) datatable.getRowData();
		try {
			this.livroFacade.removerLivro(this.livro);
		} catch (LivroException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "ficarNaMesmaPagina";
	}
	
	public String editarLivro(){
		this.livro = (Livro) datatable.getRowData();
		return "editar"; 
	}
	
	public String salvarModificacoes(){
		try {	
			this.livroFacade.editarLivro(livro);
		} catch (LivroException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "salvo";
	}	
	
	public List<Livro> getLivros(){
		return this.livroFacade.listarLivros();
	}	
	
	public List<SelectItem> getGeneros(){
		Genero[] generos = Genero.values();
		List<SelectItem> generosSI = new ArrayList<SelectItem>();
		generosSI.add(new SelectItem(""));
		
		//TODO: extrair para ficar genérico para qualquer enum
		for ( int i = 0; i < generos.length; i++ ){
			generosSI.add(new SelectItem( generos[i].toString() ));
		}
	
		return generosSI;
	}
	
	public String descreverLivro(){
		this.livro = (Livro) datatable.getRowData();
		return "descricao"; 
	}	
	
	public List<Livro> getBuscaLivros(){
		try {
			return this.livroFacade.buscaLivro(this.busca);
		} catch (LivroException e) {
			setMessagem(e.getMessage());
			return null;
		}
	}	
	
	public String paginaBusca(){
		return "buscar";
	}	
	
	private void setMessagem(String msg){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(msg));
	}
	
	
}
