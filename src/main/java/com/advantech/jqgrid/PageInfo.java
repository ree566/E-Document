/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.jqgrid;

/**
 *
 * @author Wei.Cheng
 */
public class PageInfo {

    private boolean _search;
    private String nd;
    private int page;
    private int rows;
    private String sidx;
    private String sord;

    private String searchField;
    private String searchString;
    private String searchOper;
    private String filters;

    private Integer maxNumOfRows;

    public PageInfo() {
        this.sidx = "";
        this.sord = "asc";
        this.page = 1;
        this.rows = 10;
        this.maxNumOfRows = 0;
    }

    public boolean get_Search() {
        return _search;
    }

    public PageInfo set_Search(boolean _search) {
        this._search = _search;
        return this;
    }

    public String getNd() {
        return nd;
    }

    public PageInfo setNd(String nd) {
        this.nd = nd;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PageInfo setPage(int page) {
        this.page = page;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public PageInfo setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public String getSidx() {
        return sidx;
    }

    public PageInfo setSidx(String sidx) {
        this.sidx = sidx;
        return this;
    }

    public String getSord() {
        return sord;
    }

    public PageInfo setSord(String sord) {
        this.sord = sord;
        return this;
    }

    public Integer getMaxNumOfRows() {
        return maxNumOfRows;
    }

    public PageInfo setMaxNumOfRows(Integer maxNumOfRows) {
        this.maxNumOfRows = maxNumOfRows;
        return this;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchOper() {
        return searchOper;
    }

    public void setSearchOper(String searchOper) {
        this.searchOper = searchOper;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

}
