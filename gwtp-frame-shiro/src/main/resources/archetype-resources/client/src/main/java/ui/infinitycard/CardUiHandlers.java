package ${package}.ui.infinitycard;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CardUiHandlers<E> extends UiHandlers {
	void onSelected(IsCardWidget<E> card);
	void onLoadRange(LoadRange loadRange);
	void onRefresh();
}
