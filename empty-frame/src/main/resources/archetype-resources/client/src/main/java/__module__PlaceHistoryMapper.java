package ${package};

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import ${package}.greeting.GreetingPlace;
import ${package}.home.HomePlace;
import ${package}.serverstatus.ServerStatusPlace;

@WithTokenizers({
	GreetingPlace.Tokenizer.class,

	HomePlace.class,
	ServerStatusPlace.class
	})
public interface ${module}PlaceHistoryMapper extends PlaceHistoryMapper {
}
