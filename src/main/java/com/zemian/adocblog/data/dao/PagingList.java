package com.zemian.adocblog.data.dao;

import java.util.ArrayList;
import java.util.List;

public class PagingList<T> {
    private List<T> list;
    private boolean isMore;
    private Paging paging; // Store input paging if there is any

    public PagingList() {
        this.list = new ArrayList<>();
        this.isMore = false;
        this.paging = new Paging(0, 0);
    }

    public PagingList(List<T> list, boolean isMore, Paging paging) {
        this.list = list;
        this.isMore = isMore;
        this.paging = paging;
    }

    public int getPrevPageOffset() {
        int offsetVal = paging.getOffset() - paging.getSize();
        if (offsetVal < 0) {
            offsetVal = 0;
        }
        return offsetVal;
    }

    public int getNextPageOffset() {
        int offsetVal = paging.getOffset() + paging.getSize();
        return offsetVal;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        this.isMore = more;
    }

    @Override
    public String toString() {
        return "PagingResult{" +
                "list.size=" + list.size() +
                ", isMore=" + isMore +
                '}';
    }
}
