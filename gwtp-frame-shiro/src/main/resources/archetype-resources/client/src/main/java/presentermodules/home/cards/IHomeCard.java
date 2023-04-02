package ${package}.presentermodules.home.cards;

public interface IHomeCard extends Comparable<IHomeCard> {
	// return false when it is hidden.
	boolean update();
	Integer getOrder();
}
