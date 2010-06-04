package br.com.bookstore.service;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ServiceLocator {

	private Context initialContext;
	private static ServiceLocator instance;
	private Map<String, Object> cache;
	private NameResolver nomeResolver;
	
	private ServiceLocator(){
		try {
			initialContext = new InitialContext();
			cache= new HashMap<String, Object>();
			nomeResolver =  new JBossNameResolver();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public Object getLocalEJB(Class<?> ejbInterface) throws NamingException{
		String jndiNAme = nomeResolver.ResolverNameForLocalEJB(ejbInterface);
		Object result = cache.get(jndiNAme);
		if (result == null) {
			result = initialContext.lookup(jndiNAme);
			cache.put(jndiNAme, result);
		}
		return result;
	}
	
	public Object getRemoteEJB(Class<?> ejbInterface) throws NamingException{
		
		String jndiNAme = nomeResolver.ResolverNameForRemoteEJB(ejbInterface);
		Object result = cache.get(jndiNAme);
		if (result == null) {
			result = initialContext.lookup(jndiNAme);
			cache.put(jndiNAme, result);
		}
		return result;
	}
	
	public static ServiceLocator getInstance(){
		if(instance == null){
			instance = new ServiceLocator();
		}
		return instance;
	}
}
