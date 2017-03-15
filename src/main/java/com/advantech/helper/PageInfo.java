/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

/**
 *
 * @author Wei.Cheng
 */
public class PageInfo {

    private String _search;
    private String nd;
    private int page;
    private int rows;
    private String sidx;
    private String sord;

    private int maxNumOfRows;
    
    public PageInfo(){
        this.sidx = "1";
        this.sord = "asc";
    }

    public String getSearch() {
        return _search;
    }

    public PageInfo setSearch(String _search) {
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

    public int getMaxNumOfRows() {
        return maxNumOfRows;
    }

    public PageInfo setMaxNumOfRows(int maxNumOfRows) {
        this.maxNumOfRows = maxNumOfRows;
        return this;
    }

}
