package ${package}.entry;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public abstract class ActivityPlace<A extends AbstractActivity, P extends Place> extends Place
		implements PlaceTokenizer<P> {

	@Override
	public String getToken(P place) {
		return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public P getPlace(String token) {
		return (P) this;
	}
}
