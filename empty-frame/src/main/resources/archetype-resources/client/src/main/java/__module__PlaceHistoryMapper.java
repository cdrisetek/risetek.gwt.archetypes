package ${package};

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import ${package}.greeting.GreetingPlace;

@WithTokenizers(GreetingPlace.Tokenizer.class)
public interface ${module}PlaceHistoryMapper extends PlaceHistoryMapper {
}
