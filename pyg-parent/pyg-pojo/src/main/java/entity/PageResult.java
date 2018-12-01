package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @program: pyg-parent
 * @description: 分页
 * @author: zzy
 * @create: 2018-11-29 19:47
 **/
public class PageResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private long total;//总记录数
    private List<?> rows;//当前页结果

    public PageResult() {
    }

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
