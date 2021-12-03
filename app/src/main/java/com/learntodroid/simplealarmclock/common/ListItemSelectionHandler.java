package com.learntodroid.simplealarmclock.common;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class ListItemSelectionHandler<Item> extends Observable {
    private boolean inSelectableMode;
    private Set<Item> selectedItems = new HashSet<>();

    public void setSelectable(boolean selectable) {
        this.inSelectableMode = selectable;
        setChanged();
        notifyObservers(this.inSelectableMode);
    }

    public boolean isInSelectableMode() {
        return inSelectableMode;
    }

    public void select(Item item) {
        selectedItems.add(item);
    }

    public Set<Item> getSelectedItems() {
        return selectedItems;
    }

    public void clear() {
        selectedItems.clear();
    }

    public void deselect(Item alarm) {
        selectedItems.remove(alarm);
        if (selectedItems.size() == 0) {
            setSelectable(false);
        }
    }
}
