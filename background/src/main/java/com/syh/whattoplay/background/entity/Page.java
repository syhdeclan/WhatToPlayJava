package com.syh.whattoplay.background.entity;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    //结果集
    private List<T> data;
    //查询记录数
    private int rowCount;
    //每页多少条数据
    private int pageSize = 20;
    //第几页
    private int pageNumber = 1;
    //跳过几条数
    private int skip = 0;

    /**
     * 总页数
     *
     * @return
     */
    public int getTotalPages() {
        return (rowCount + pageSize - 1) / pageSize;
    }

}