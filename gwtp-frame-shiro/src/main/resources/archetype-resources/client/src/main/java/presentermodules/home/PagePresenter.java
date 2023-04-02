package ${package}.presentermodules.home;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.NameTokens;
import ${package}.bindery.PlainMenu;
import ${package}.entry.SubjectChangeEvent;
import ${package}.entry.SubjectChangeEvent.SubjectChangeHandler;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.home.cards.AbstractHomeCardPresenter;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;
import ${package}.utils.Icons;

@PlainMenu(order = 0, title = "\u9996\u9875", token = NameTokens.home, iconClass = Icons.Home.class)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		                   implements MyUiHandlers, SubjectChangeHandler {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void layoutHomeCards(List<AbstractHomeCardPresenter<?,?>> sortedList);
	}

	@ProxyStandard
	@NameToken(NameTokens.home)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		// When subject changed, home-card may be change too.
		eventBus.addHandler(SubjectChangeEvent.getType(), this);
		// Reveal all HomeCard with ProxyEvent
		// every homeCard instance call back with it Presenter and order,
		// so we have chance to collection those cards for update view later.
		fireRevealHomeCardEvent();
		
		// update cards and set to view container with order.
		onSubjectChange();
	}

	List<AbstractHomeCardPresenter<?, ?>> cards = new Vector<>();

	private void fireRevealHomeCardEvent() {
		fireEvent(new RevealHomeCardEvent((p) -> cards.add(p)));
	}
	
	@Override
	public void layoutCards() {
		// Scheduler a work when fired event all completes.
		Scheduler.get().scheduleDeferred(()-> {
			List<AbstractHomeCardPresenter<?,?>> list = cards.stream().filter(c -> c.update()).collect(Collectors.toList());
			Collections.sort(list);
			getView().layoutHomeCards(list);
		});
	}
	
	@Override
	public void onSubjectChange() {
		Scheduler.get().scheduleDeferred(() -> layoutCards());
	}
}
