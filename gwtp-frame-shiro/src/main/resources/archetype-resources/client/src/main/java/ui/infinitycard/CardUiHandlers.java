package ${package}.ui.infinitycard;

import java.util.Set;

import com.google.gwt.view.client.Range;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CardUiHandlers<E> extends UiHandlers {
	public void setSearchKeyHandler(HasSearch keyhandler);

	public void onRefresh();
	public void onLoadRange(Range range);

	public void onCreateEntities(final Set<E> entities);
	public void onDeleteCards(final Set<IsCardWidget<E>> cards);
	public void onUpdateCards(final Set<IsCardWidget<E>> cards);

	public void openCardDialog(final IsCardWidget<E> card);
	public String getDialogTitle(IsCardWidget<E> card);
	public void gotoPlace(final E entity);
}
