package br.com.bookstore.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.richfaces.component.html.HtmlDataTable;

import br.com.bookstore.bean.ClienteBeanModel;
import br.com.bookstore.cliente.Perfil;
import br.com.bookstore.cliente.Sexo;
import br.com.bookstore.cliente.Usuario;
import br.com.bookstore.exceptions.ClienteException;
import br.com.bookstore.service.ServiceBookStore;
import br.com.bookstore.service.ServiceLocator;
import br.com.bookstore.cliente.Endereco;

public class ClienteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Usuario cliente;
	
	/**
	 * Este combo está associado ao xhtml através de um binding. Isso significa que 
	 * toda alteração que fizermos aqui, programaticamente, será refletida no combo
	 * ao ser apresentado ao usuário.
	 */
	private transient UISelectItems comboCidades;
	
	private transient HtmlDataTable datatable;
	
	//private ClienteBeanModel facade;
	private ServiceBookStore facade;
	
	public ClienteBean(){
		
		try {
			/*Context initialContext = new InitialContext();
			this.facade = (ClienteBeanModel) initialContext.lookup("BookStoreEAR/ClienteBeanGen/local");*/
			
			this.facade = (ServiceBookStore) ServiceLocator.getInstance().getLocalEJB(ServiceBookStore.class);
			
			} catch (NamingException e) {
			/* Se alguma coisa acontecer aqui, é porque não foi possível fazer lookup,
			 * ou não foi possível criar o initial context.
			 * */
			e.printStackTrace();
		}		
			
			this.cliente = new Usuario();
			this.cliente.setEndereco(new Endereco());
			iniciarCidades();				
	}
	
	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}
	
	public UISelectItems getComboCidades() {
		if(this.cliente.getEndereco().getEstado() != null){
			iniciarCidades();
		}
		return this.comboCidades;
	}
	
	public void setComboCidades(UISelectItems comboCidades) {
		
		this.comboCidades = comboCidades;
	}
	
	public HtmlDataTable getDatatable() {
		return datatable;
	}

	public void setDatatable(HtmlDataTable datatable) {
		this.datatable = datatable;
	}

	public String cadastrar(){
		//TODO: verificar se as duas senhas digitadas batem...
		//TODO: o mesmo para o e-mail;
		
		try {
			this.cliente.setPerfil(Perfil.Cliente);
			this.facade.cadastrarCliente(this.cliente);
		} catch (ClienteException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(e.getMessage()));
			return "naoCadastrado"; //não colocamos nenhum mapeamento, para que fique na mesma página
		}
		
		return "cadastrado";
	}
	
	public String excluirCliente(){
		this.cliente = (Usuario) datatable.getRowData();
		try {
			this.facade.removerCliente(this.cliente);
		} catch (ClienteException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		return "ficarNaMesmaPagina";
	}
	
	public String editarCliente(){
		this.cliente = (Usuario) datatable.getRowData();
		
		if(this.cliente.getPerfil().equals(Perfil.Administrador)){
			FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Cliente com Perfil invalido"));			
			return "ficarNaMesmaPagina";
		}
		return "editar";
	}
	
	public String salvarModificacoes() throws ClienteException{
		try {
			this.facade.editarCliente(this.cliente);
		} catch (ClienteException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(e.getMessage()));
			return "ficarNaMesmaPagina";
		}
		return "salvo";
	}
	
	public List<Usuario> getClientes(){
		return this.facade.listarClientes();
	}
	
	/**
	 * Método usado para tratar a troca de um estado a partir do combobox. De acordo com o novo estado
	 * selecionado, iremos alterar as cidades correspondentes. Para isso, precisamos usar o binding pelo
	 * menos para o combo de cidades. Por enquanto, estamos tratando tudo neste managed bean. Poderíamos
	 * ter um outro managed bean para realizar esta lógica de apresentação, mas isso poderia complicar
	 * o entendimento de quem está começando a enteder esta tecnologia.
	 * 
	 * @param evento ValueChangeEvent demonstrando os valores novo e antigo.
	 */
	public void alterarEstado( ValueChangeEvent evento ){
		
		String novoEstado = (String) evento.getNewValue();
		
		List<String> cidadesStr = this.facade.obterCidades(novoEstado);
		List<SelectItem> cidadesSI = this.converteParaSelectItems(cidadesStr);
		
		this.comboCidades.setValue(cidadesSI);
		
		FacesContext.getCurrentInstance().renderResponse();
	}
	
	public List<SelectItem> getEstados(){
		List<String> estados = this.facade.obterTodosOsEstados();
		return converteParaSelectItems(estados);
	}

	public List<SelectItem> getSexos(){
		Sexo[] sexosArray = Sexo.values();
		List<SelectItem> sexosSI = new ArrayList<SelectItem>();
		sexosSI.add(new SelectItem(""));
		
		//TODO: extrair para ficar genérico para qualquer enum
		for ( int i = 0; i < sexosArray.length; i++ ){
			sexosSI.add(new SelectItem( sexosArray[i].toString() ));
		}
	
		return sexosSI;
	}
	
	/**
	 * Realiza a conversão de uma lista de Strings para uma lista de SelectItems.
	 * Isso é uma exigência do combo box, quando trabalhamos com a implementação
	 * padrão do JSF 1.2. Se usarmos um combo box de uma biblioteca de componentes, 
	 * como por exemplo, o Tomahawk, não precisaríamos realizar esta conversão.
	 */
	private List<SelectItem> converteParaSelectItems(List<String> estados) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("")); //adicionando um item vazio, para que não se tenha nada inicialmente.
		for (String string : estados) {
			items.add(new SelectItem(string));
		}
		return items;
	}
		
	private void iniciarCidades(){
		List<String> cidadesStr = this.facade.obterCidades(this.cliente.getEndereco().getEstado());
		List<SelectItem> cidadesSI = this.converteParaSelectItems(cidadesStr);
		this.comboCidades = new UISelectItems();
		this.comboCidades.setValue(cidadesSI);							
	} 
	
}
