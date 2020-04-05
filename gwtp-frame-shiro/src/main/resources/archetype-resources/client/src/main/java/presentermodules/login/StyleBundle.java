package ${package}.presentermodules.login;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

interface StyleBundle extends ClientBundle {
	@Source("user-icon.png")
	ImageResource user_png();

	@Source("password-icon.png")
	ImageResource password_png();
}
