package ${package}.root;

import javax.inject.Inject;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.root.StyleBundle.Style;

public class ViewImpl extends ViewWithUiHandlers<MyUiHandlers> implements RootPresenter.MyView {

	private final Style style = StyleBundle.resources.style();
    private final SimplePanel mainContainer = new SimplePanel();
    private final SimplePanel topContainer = new SimplePanel();
    
    @Inject
    public ViewImpl() {
		style.ensureInjected();

		// main Layout
	    FlowPanel mainLayout = new FlowPanel();
		mainLayout.getElement().setAttribute("layout", "column");
		mainLayout.setStyleName(style.p6nVulcanLayoutMain(), true);
		mainLayout.setStyleName(style.layoutColumn(), true);
		mainLayout.setStyleName(style.flex(), true);

        initWidget(mainLayout);

        // top Container, for Menu Context
        topContainer.setStyleName(style.pan_shell_top_container());
        topContainer.setStyleName(style.layoutColumn(), true);
        topContainer.setStyleName(style.flex_none(), true);
        mainLayout.add(topContainer);
        
        mainContainer.getElement().setId("mainContainer");
        mainContainer.getElement().setAttribute("layout", "row");
        mainContainer.setStyleName(style.panShellMainContainer());
        mainContainer.setStyleName(style.layoutRow(), true);
        mainContainer.setStyleName(style.flex(), true);
        mainLayout.add(mainContainer);
    }

    /**
     * pan-shell -> mainLayout  -> topContainer   -> barContainer
     *                          -> mainContainer
    **/
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
