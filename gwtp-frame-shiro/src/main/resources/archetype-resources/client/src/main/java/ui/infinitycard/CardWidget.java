package ${package}.ui.infinitycard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class CardWidget<E> implements IsCardWidget<E> {
	protected E entity;
	protected CardUiHandlers<E> uiHandlers;
	
	@Override
	public E getEntity() {
		return entity;
	}

	public static abstract class Builder<E> {
		protected E entity;
		private CardUiHandlers<E> uiHandlers;

		public Builder(CardUiHandlers<E> uiHandlers) {
			this.uiHandlers = uiHandlers;
		}

		public abstract CardWidget<E> build();
		protected CardWidget<E> build(CardWidget<E> c) {
			assert entity != null : "build without entity";
			c.entity = entity;
			c.uiHandlers = uiHandlers;
			c.render();
			return c;
		}

		public Builder<E> setData(E entity) {
			this.entity = entity;
			return this;
		}
	}

    @Override
    public Widget asWidget() {
        if (widget == null) {
            throw new NullPointerException("widget cannot be null, you should call ViewImpl.initWidget() before.");
        }

        return widget;
    }
	
	private Widget widget;
    protected void initWidget(IsWidget widget) {
        if (this.widget != null) {
            throw new IllegalStateException("ViewImpl.initWidget() may only be called once.");
        } else if (widget == null) {
            throw new NullPointerException("widget cannot be null");
        }

        this.widget = widget.asWidget();
    }
}
