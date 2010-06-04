package br.com.bookstore.managedbean;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.naming.NamingException;

import org.richfaces.component.html.HtmlDataTable;

import br.com.bookstore.compras.GerenciadorVenda;
import br.com.bookstore.model.livro.Livro;
import br.com.bookstore.service.ServiceBookStoreCompra;
import br.com.bookstore.service.ServiceLocator;

public class CarrinhoBeanWeb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GerenciadorVenda carrinho;
	private ServiceBookStoreCompra carrinhoFacede;
	private transient HtmlDataTable datatable;
	private Livro livro;
	private Integer quantidade;
	
	public CarrinhoBeanWeb(){
		
		try {
			/*Context initialContext = new InitialContext();
			this.livroFacade = (LivroBeanModel) initialContext.lookup("BookStoreEAR/LivroBeanGen/local");*/
			
			this.carrinhoFacede = (ServiceBookStoreCompra) ServiceLocator.getInstance().getLocalEJB(ServiceBookStoreCompra.class);
			
			} catch (NamingException e) {
			/* Se alguma coisa acontecer aqui, é porque não foi possível fazer lookup,
			 * ou não foi possível criar o initial context.
			 * */
			e.printStackTrace();
		}				
		
	    this.livro = new Livro();
		this.carrinho = new GerenciadorVenda();
	}
		
    public void CalcularFrete(){
    	this.carrinho.CalcularFrete();
    	this.carrinho.CalcularValorCompra();
    }
    
    public Double getValorTotal(){
    	this.carrinho.CalcularValorCompra();    
    	return this.carrinho.getVenda().getValorVendaTotal();
    }

	public GerenciadorVenda getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(GerenciadorVenda carrinho) {
		this.carrinho = carrinho;
	}

	public HtmlDataTable getDatatable() {
		return datatable;
	}

	public void setDatatable(HtmlDataTable datatable) {
		this.datatable = datatable;
	}
    
    public Livro getActionAttribute(ActionEvent event, String name) {
        return (Livro) event.getComponent().getAttributes().get(name);
    }

    
    public String addItemAoCarrinho(){
    	System.out.println("Teste do param: " + livro.toString());
    	//this.carrinhoFacede.addItens(livro, quantidade);    	
    	return "carrinho";
    }

    public String removeItemAoCarrinho(){
    	return "ficaNaMesmaPagian";
    }
    
	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		if( livro == null){
			System.out.println("Livro: null" );	
		}
		
		this.livro = livro;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
       
    
}
