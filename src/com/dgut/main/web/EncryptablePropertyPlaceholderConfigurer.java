package com.dgut.main.web;

import java.util.Properties;

import com.dgut.common.pck.Encrypt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


import com.dgut.main.Constants;

public class EncryptablePropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {

		String driverClassName = props.getProperty("jdbc.driverClassName");
		if (driverClassName != null) {
			props.setProperty("jdbc.driverClassName", Encrypt.decrypt3DES(
					driverClassName, Constants.ENCRYPTION_KEY));
		}
		
		String jdbcUrl=props.getProperty("jdbc.url");
		if(jdbcUrl!=null){
			props.setProperty("jdbc.url", Encrypt.decrypt3DES(jdbcUrl, Constants.ENCRYPTION_KEY));
		}
		
		String username = props.getProperty("jdbc.username");
		if (username != null) {
			props.setProperty("jdbc.username", Encrypt.decrypt3DES(username, Constants.ENCRYPTION_KEY));
		}
		
		String password = props.getProperty("jdbc.password");
		if(password!=null){
			props.setProperty("jdbc.password",Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY) );
		}
		
//		String checkoutTimeout = props.getProperty("cpool.checkoutTimeout");
//		if(checkoutTimeout!=null){
//			props.setProperty("cpool.checkoutTimeout",Encrypt.decrypt3DES(checkoutTimeout, Constants.ENCRYPTION_KEY) );
//		}
//
//		String minPoolSize = props.getProperty("cpool.minPoolSize");
//		if(minPoolSize!=null){
//			props.setProperty("cpool.minPoolSize",Encrypt.decrypt3DES(minPoolSize, Constants.ENCRYPTION_KEY) );
//
//		}
//
//		String maxPoolSize = props.getProperty("cpool.maxPoolSize");
//		if(maxPoolSize!=null){
//			props.setProperty("cpool.maxPoolSize",Encrypt.decrypt3DES(maxPoolSize, Constants.ENCRYPTION_KEY) );
//		}
//
//		String maxIdleTime = props.getProperty("cpool.maxIdleTime");
//		if(maxIdleTime!=null){
//			props.setProperty("cpool.maxIdleTime",Encrypt.decrypt3DES(maxIdleTime, Constants.ENCRYPTION_KEY) );
//		}
//
//		String acquireIncrement = props.getProperty("cpool.acquireIncrement");
//		if(acquireIncrement!=null){
//			props.setProperty("cpool.acquireIncrement",Encrypt.decrypt3DES(acquireIncrement, Constants.ENCRYPTION_KEY) );
//		}
//
//		String maxIdleTimeExcessConnections = props.getProperty("cpool.maxIdleTimeExcessConnections");
//		if(maxIdleTimeExcessConnections!=null){
//			props.setProperty("cpool.maxIdleTimeExcessConnections",Encrypt.decrypt3DES(maxIdleTimeExcessConnections, Constants.ENCRYPTION_KEY) );
//		}

		System.out.println("+++"+props);
		
		super.processProperties(beanFactoryToProcess,props);
		
		
	}
}
