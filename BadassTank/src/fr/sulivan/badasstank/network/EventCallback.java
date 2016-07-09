package fr.sulivan.badasstank.network;

@FunctionalInterface
public interface EventCallback {
	public boolean call(Event event);
}
