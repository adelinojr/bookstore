package br.com.bookstore.service;

public interface NameResolver {

	public String ResolverNameForLocalEJB(Class<?> ejbInterface);
	
	public String ResolverNameForRemoteEJB(Class<?> ejbInterface);
}
