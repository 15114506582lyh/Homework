package com.example.homework.Domain.vo;

public interface PaginationResultSupport {

    /**
     * 总记录数
     */
    public void setTotal(long total);

    public long getTotal();

    /**
     * 总页数
     */
    public void setTotalPages(int totalPages);

    public int getTotalPages();
}
