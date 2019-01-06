package ${package}.serverstatus;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

@Prefix("SStatus")
public class ServerStatusPlace extends Place implements PlaceTokenizer<ServerStatusPlace> {
    @Override
    public String getToken(ServerStatusPlace place) {
        return "";
    }

	@Override
	public ServerStatusPlace getPlace(String token) {
		return new ServerStatusPlace();
	}
}
