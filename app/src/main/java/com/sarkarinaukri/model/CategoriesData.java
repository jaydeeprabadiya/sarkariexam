package com.sarkarinaukri.model;

import java.io.Serializable;

/**
 * Created by AppsMediaz Technologies on 04/08/2017.
 */
public class CategoriesData implements Serializable {
    private String categoryId;
    private String categoryName;
    private String selected;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
