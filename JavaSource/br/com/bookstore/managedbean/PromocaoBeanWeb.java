package br.com.bookstore.managedbean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.richfaces.component.html.HtmlDataTable;

import br.com.bookstore.compras.PromocaoVendas;
import br.com.bookstore.service.ServiceBookStore;
import br.com.bookstore.service.ServiceLocator;
import br.com.bookstoreExecption.PromocaoException;

public class PromocaoBeanWeb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PromocaoVendas promocao ;
	private transient HtmlDataTable datatable;
	private ServiceBookStore promocaoFacede;
	
	
	
	public PromocaoBeanWeb (){

		try {
			/*Context initialContext = new InitialContext();
			this.livroFacade = (LivroBeanModel) initialContext.lookup("BookStoreEAR/LivroBeanGen/local");*/
			
			this.promocaoFacede = (ServiceBookStore) ServiceLocator.getInstance().getLocalEJB(ServiceBookStore.class);
			
			} catch (NamingException e) {
				e.printStackTrace();
		}		
	   this.promocao = new  PromocaoVendas();	
	}
	
	public PromocaoVendas getPromocao() {
		return promocao;
	}
	public void setPromocao(PromocaoVendas promocao) {
		this.promocao = promocao;
	}
	public HtmlDataTable getDatatable() {
		return datatable;
	}
	public void setDatatable(HtmlDataTable datatable) {
		this.datatable = datatable;
	}
	
	public String cadastra(){
		try {
			this.promocaoFacede.cadastraPromocao(this.promocao);
		} catch (PromocaoException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "sucesso";
	}	
	
	public String excluirPromocao(){
		this.promocao = (PromocaoVendas) datatable.getRowData();
		try {
			this.promocaoFacede.removePromocao(this.promocao);
		} catch (PromocaoException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "ficarNaMesmaPagina";
	}	

	public String editarPromocao(){
		this.promocao = (PromocaoVendas) datatable.getRowData();
		return "editar"; 
	}	
	
	public String salvarModificacoes(){
		try {	
			this.promocaoFacede.editaPromocao(this.promocao);
		} catch (PromocaoException e) {
			setMessagem(e.getMessage());
			return "falha";
		}
		return "salvo";
	}	
	
	public ArrayList<PromocaoVendas> getPromocoes(){
		return this.promocaoFacede.buscaTodasPromocao();
	}	
	
	
	private void setMessagem(String msg){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(msg));
	}
}
