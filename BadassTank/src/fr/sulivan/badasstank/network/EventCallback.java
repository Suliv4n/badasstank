package fr.sulivan.badasstank.network;

@FunctionalInterface
public interface EventCallback {
	public void call(Event event);
}
