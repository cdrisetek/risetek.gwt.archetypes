package ${package}.presentermodules.security;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.presentermodules.security.MyUiHandlers.informationItem;
import ${package}.presentermodules.security.ui.InformationItemPanel;
import ${package}.presentermodules.security.ui.InformationPanel;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	interface Binder extends UiBinder<HTMLPanel, PageView> {}
	
	@UiField Panel listPanel;
	
	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Composite infoPanel(String infoTitle, List<informationItem> items) {
		InformationPanel i = new InformationPanel();
		i.setTitle(infoTitle);

		for(informationItem item:items) {
			InformationItemPanel a = new InformationItemPanel();
			a.setKey(item.key);
			a.setValue(item.value);
			if(null != item.link) {
				a.hasLink();
				a.addClickHandler(c->{getUiHandlers().update(item.link);});
			}

			i.add(a);
		}

		return i;
	}
	
	@Override
	public void onAttach() {
		showInformation();
	}
	
	@Override
	public void showInformation() {
		listPanel.clear();
		listPanel.add(infoPanel("个人资料", getUiHandlers().getSecurityInformation()));
		listPanel.add(infoPanel("联系信息", getUiHandlers().getContactInformation()));
	}
}
