package edu.avans.hartigehap.domain;

public class MenuItemIterator implements Iterator {
	MenuItem[] menuItems;
	int position = 0;
	
	public MenuItemIterator(MenuItem[] menuItems) {
		this.menuItems = menuItems;
	}
	
	public MenuItem next() {
		MenuItem menuItem = menuItems[position];
		position++;
		return menuItem;
	}
	
	public boolean hasNext() {
		if(position >= menuItems.length || menuItems[position] == null) {
			return false;
		} else {
			return true;
		}
	}
}
