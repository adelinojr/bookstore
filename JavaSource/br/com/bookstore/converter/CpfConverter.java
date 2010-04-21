package br.com.bookstore.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.bookstore.cliente.CpfCliente;

public class CpfConverter implements Converter {


	private CpfCliente cpf;
	
	public CpfConverter() {
		this.cpf = new CpfCliente();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if( arg2.length() == 11 && isDigitos(arg2) )	{
			this.cpf.setNumeroCpf( arg2.substring(0, 10));
			this.cpf.setDivitoVerificador(arg2.substring(10));			
		}else{
			throw new ConverterException(getMessage());
		}
		return this.cpf;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return arg2.toString();
	}
	
	private boolean isDigitos(String str){
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(str);
		return m.matches(); 		
	}

	private FacesMessage getMessage(){
		FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Formato ou tamanha Invalido ([0-9])");	
		return message;
	}


}
