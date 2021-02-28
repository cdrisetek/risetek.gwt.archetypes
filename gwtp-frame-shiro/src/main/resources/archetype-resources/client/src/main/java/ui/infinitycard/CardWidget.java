package ${package}.ui.infinitycard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class CardWidget<E> implements IsCardWidget<E> {
	protected E entity;
	
	@Override
	public E getEntity() {
		return entity;
	}

    @Override
    public Widget asWidget() {
        if (widget == null)
            throw new NullPointerException("widget cannot be null, you should call ViewImpl.initWidget() before.");

        return widget;
    }
	
	private Widget widget;
    protected void initWidget(IsWidget widget) {
        if (this.widget != null)
            throw new IllegalStateException("ViewImpl.initWidget() may only be called once.");
        else if (widget == null)
            throw new NullPointerException("widget cannot be null");

        this.widget = widget.asWidget();
    }
}
