package com.zty.common.core.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类
 * @author Administration
 */
@Data
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int total;

    private List<T> list;

    public PageData(List<T> list,long total) {
        this.list = list;
        this.total = (int)total;
    }
}
