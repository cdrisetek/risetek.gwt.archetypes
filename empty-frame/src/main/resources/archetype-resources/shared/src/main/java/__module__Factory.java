package ${package};

import com.google.web.bindery.requestfactory.shared.RequestFactory;
import ${package}.serverstatus.ServerStatusContext;

public interface ${module}Factory extends RequestFactory {
	GreetingContext greeting();
	ServerStatusContext serverstatus();
}