package ${package}.root;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ViewImpl extends ViewWithUiHandlers<MyUiHandlers> implements RootPresenter.MyView {
    @UiTemplate("PageView.ui.xml")
	interface Binder extends UiBinder<HTMLPanel, ViewImpl> {}

    @UiField
	SimplePanel mainContainer, topContainer;

    @Inject
    public ViewImpl(Binder binder) {
    	initWidget(binder.createAndBindUi(this));
		asWidget().getElement().setAttribute("layout", "column");
        mainContainer.getElement().setAttribute("layout", "row");
    }

	@Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == RootPresenter.SLOT_MainContent) {
        	mainContainer.clear();
            mainContainer.add(content);
        } else if( slot == RootPresenter.SLOT_MenuContent ) {
        	topContainer.clear();
        	topContainer.add(content);
        } else {
            super.setInSlot(slot, content);
        }
    }
}
