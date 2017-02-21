package com.boostcamp.hyeon.wallpaper.base.domain;

import java.util.List;

/**
 * Created by hyeon on 2017. 2. 21..
 */

public class ResultNaver {
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<ImageNaver> items;

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public List<ImageNaver> getItems() {
        return items;
    }

    public void setItems(List<ImageNaver> items) {
        this.items = items;
    }
}
