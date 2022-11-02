package ${package}.server.database.hsqldb;

import javax.inject.Provider;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ${package}.server.devops.ServicesManagement;

public class HibernateSessionFactoryProvider implements Provider<SessionFactory> {
	private static SessionFactory sessionFactory;
    public static void shutdown() {
        // Close caches and connection pools
    	if(null != sessionFactory) {
    		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX SHUTDOWN");
    		sessionFactory.close();
    	}
    }

	@Override
	public SessionFactory get() {
		StandardServiceRegistry standardRegistry;
		if(null != System.getProperty("hsqlmem")) {
			System.out.println("--- development mode, load hibernate.hsqldbmem.cfg.xml");
	        standardRegistry = new StandardServiceRegistryBuilder()
	                .configure("hibernate.hsqldbmem.cfg.xml")
	                .build();
		} else {
	        standardRegistry = new StandardServiceRegistryBuilder()
	                .configure()
	                .build();
		}
	        
	        Metadata metadata = new MetadataSources( standardRegistry )
	/*        		
	            .addAnnotatedClass( MyEntity.class )
	            .addAnnotatedClassName( "org.hibernate.example.Customer" )
	            .addResource( "org/hibernate/example/Order.hbm.xml" )
	            .addResource( "org/hibernate/example/Product.orm.xml" )
	*/
	            .getMetadataBuilder()
	            .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
	            .build();

	        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();
	/*
	        // Supply a SessionFactory-level Interceptor
	        sessionFactoryBuilder.applyInterceptor( new CustomSessionFactoryInterceptor() );

	        // Add a custom observer
	        sessionFactoryBuilder.addSessionFactoryObservers( new CustomSessionFactoryObserver() );

	        // Apply a CDI BeanManager ( for JPA event listeners )
	        sessionFactoryBuilder.applyBeanManager( getBeanManager() );
	*/
	        
			ServicesManagement.addCleanerHandler(consumer -> shutdown());

	        
	        return (sessionFactory = sessionFactoryBuilder.build());    
	}
}
