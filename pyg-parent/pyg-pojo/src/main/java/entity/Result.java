package entity;

import java.io.Serializable;

/**
 * @program: pyg-parent
 * @description: 新增品牌是否成功
 * @author: zzy
 * @create: 2018-11-29 21:19
 **/
public class Result implements Serializable {

    private boolean success;
    private String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
