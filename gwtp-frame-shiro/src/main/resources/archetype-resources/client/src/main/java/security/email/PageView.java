package ${package}.security.email;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	interface MyUiBinder extends UiBinder<HTMLPanel, PageView> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private HTMLPanel root;
	
	@UiField Panel backButton;
	

	@Inject
	public PageView() {
		root = uiBinder.createAndBindUi(this);
		backButton.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().goBack();
			}}, ClickEvent.getType());

		initWidget(root);
	}
	
}
