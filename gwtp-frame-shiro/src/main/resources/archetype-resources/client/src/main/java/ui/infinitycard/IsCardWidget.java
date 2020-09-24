package ${package}.ui.infinitycard;

import com.google.gwt.user.client.ui.IsWidget;

public interface IsCardWidget<E> extends IsWidget {
	public E getEntity();
	public void updateCard(E new_entity);
	public void render();
	public void deleteCard();
}
