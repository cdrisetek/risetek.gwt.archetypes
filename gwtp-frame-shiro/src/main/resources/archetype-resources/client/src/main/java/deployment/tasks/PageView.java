package ${package}.deployment.tasks;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<Widget, PageView> {}

	@UiField Panel slot;

	@Inject
	public PageView(final EventBus eventBus,
			        final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}
	
	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		getUiHandlers().onGoBackPlace();
	}

	@Override
	public void showTask(String title, List<String> messages, String type, String state) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		for(String m : messages)
			sb.append(templateMessage.message(m));

		slot.add(new HTMLPanel(templates.task(title, sb.toSafeHtml(), state)));
	}

	@Override
	public void clear() {
		slot.clear();
	}

	interface TemplateTask extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div class=\"task-card\">"
				+ "<div><div class=\"title\">{0}</div><div class=\"state\">{2}</div></div>"
				+ "<div class=\"messages\">{1}</div>"
				+ "</div>")
		SafeHtml task(String name, SafeHtml messages, String state);
	}

	private TemplateTask templates = GWT.create(TemplateTask.class);

	interface TemplateMessage extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div class=\"message\">{0}</div>")
		SafeHtml message(String message);
	}

	private TemplateMessage templateMessage = GWT.create(TemplateMessage.class);
}
