package br.com.bookstore.service;

public class JBossNameResolver implements NameResolver {

	@Override
	public String ResolverNameForLocalEJB(Class<?> ejbInterface) {
		StringBuffer result = new StringBuffer();
		String beanName = ejbInterface.getSimpleName();
		result.append("BookStoreEAR/").append(beanName);
		result.append("Bean/local");
		return result.toString();
	}

	@Override
	public String ResolverNameForRemoteEJB(Class<?> ejbInterface) {
		StringBuffer result = new StringBuffer();
		String beanName = ejbInterface.getSimpleName();
		result.append("BookStoreEAR/").append(beanName);
		result.append("Bean/remote");
		return result.toString();
	}

}
