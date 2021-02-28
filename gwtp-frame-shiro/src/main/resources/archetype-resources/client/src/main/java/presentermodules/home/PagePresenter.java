package ${package}.presentermodules.home;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.NameTokens;
import ${package}.bindery.PlainMenu;
import ${package}.entry.SubjectChangeEvent;
import ${package}.entry.SubjectChangeEvent.SubjectChangeHandler;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;
import ${package}.utils.Icons;

@PlainMenu(order = 0, title = "\u9996\u9875", token = NameTokens.home, iconClass = Icons.Home.class)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		                   implements MyUiHandlers, SubjectChangeHandler {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void bindSlot(Slot<?> slot, int index);
		int getCloumnSize();
	}

	private final ArrayList<Slot<PresenterWidget<?>>> list = new ArrayList<>();

	@ProxyStandard
	@NameToken(NameTokens.home)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		eventBus.addHandler(SubjectChangeEvent.getType(), this);

		for(int i=0; i<getView().getCloumnSize(); i++) {
			Slot<PresenterWidget<?>> slot = new Slot<>();
			getView().bindSlot(slot, i);
			list.add(slot);
		}
		fireRevealHomeCardEvent();
	}

	private void fireRevealHomeCardEvent() {
		fireEvent(new RevealHomeCardEvent((p, o) -> addToSlot(list.get(o % 3), p)));
	}
	
	@Override
	public void onSubjectChange() {
		list.forEach(s -> clearSlot(s));
		fireRevealHomeCardEvent();
	}
}
