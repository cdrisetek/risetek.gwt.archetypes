package ${package};

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import ${package}.greeting.GreetingPlace;
import ${package}.home.HomeActivity.HomePlace;
import ${package}.serverstatus.ServerStatusActivity.ServerStatusPlace;
import ${package}.login.LoginActivity.LoginPlace;

@WithTokenizers({
	GreetingPlace.Tokenizer.class,

	HomePlace.class,
	ServerStatusPlace.class,
	LoginPlace.class
	})
public interface ${module}PlaceHistoryMapper extends PlaceHistoryMapper {
}
