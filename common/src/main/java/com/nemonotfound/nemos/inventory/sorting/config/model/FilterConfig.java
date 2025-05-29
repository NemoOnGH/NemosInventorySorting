package com.nemonotfound.nemos.inventory.sorting.config.model;

import com.nemonotfound.nemos.inventory.sorting.model.FilterMode;

public class FilterConfig {

    private boolean isFilterPersistent = false;
    private String filter = "";
    private FilterMode filterMode = FilterMode.CONTAINS_FILTER;

    public boolean isFilterPersistent() {
        return isFilterPersistent;
    }

    public void toggleFilterPersistency() {
        isFilterPersistent = !isFilterPersistent;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }
}
