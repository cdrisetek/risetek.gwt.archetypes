package ${package}.home;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

@Prefix("Home")
public class HomePlace extends Place implements PlaceTokenizer<HomePlace> {
    @Override
    public String getToken(HomePlace place) {
        return "";
    }

	@Override
	public HomePlace getPlace(String token) {
		return new HomePlace();
	}
}

