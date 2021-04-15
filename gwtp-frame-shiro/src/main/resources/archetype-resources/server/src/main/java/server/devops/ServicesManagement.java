package ${package}.server.devops;

import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.inject.Singleton;

import ${package}.share.container.StateEntity;

@Singleton
public class ServicesManagement {
	static Logger logger = Logger.getLogger(ServicesManagement.class.getName());

	static {
		System.out.println("ServicesManagement add shutdown hooks for JVM");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			cleanServices();
		}));
	}
	
	static private List<Supplier<StateEntity>> resourcesProvider = new Vector<>();
	static private final List<Consumer<Boolean>> resourcesCleaner = new Vector<>();

	static public void provideState(Supplier<StateEntity> provider) {
		resourcesProvider.add(provider);
	}

	static public void addCleanerHandler(Consumer<Boolean> consumer) {
		resourcesCleaner.add(consumer);
	}
	
	static public void cleanServices() {
		for(Consumer<Boolean> clean:resourcesCleaner)
			clean.accept(true);
	}
	
	static public void consumerState(Consumer<StateEntity> consumer) {
		for(Supplier<StateEntity> supplier:resourcesProvider)
			consumer.accept(supplier.get());
	}
	
	public ServicesManagement() {
		logger.severe("Launch ServicesManagement");
	}
	
}
