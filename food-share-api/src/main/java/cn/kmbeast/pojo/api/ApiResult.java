package cn.kmbeast.pojo.api;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用响应
 *
 * @param <T> 泛型
 */
@Setter
@Getter
public class ApiResult<T> extends Result<T> {
    private static final long serialVersionUID = 2L;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 数据总页，分页使用
     */
    private Integer total;

    /**
     * 无参构造函数
     */
    public ApiResult() {}

    /**
     * 有参构造函数
     *
     * @param code 状态码
     */
    public ApiResult(Integer code) {
        super(code, "操作成功");
    }

    /**
     * 有参构造函数
     *
     * @param code 状态码
     * @param msg  消息
     */
    public ApiResult(Integer code, String msg) {
        super(code, msg);
    }

    /**
     * 有参构造函数
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     */
    public ApiResult(Integer code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    /**
     * 有参构造函数
     *
     * @param data  数据
     * @param total 总数
     */
    public ApiResult(T data, Integer total) {
        this.data = data;
        this.total = total;
    }

    /**
     * 有参构造函数
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @param total 总数
     */
    public ApiResult(Integer code, String msg, T data, Integer total) {
        super(code, msg);
        this.data = data;
        this.total = total;
    }

    /**
     * 操作成功
     *
     * @param <T> 泛型
     * @return 响应对象
     */
    public static <T> Result<T> success() {
        ApiResult<T> result = new ApiResult<>(ResultCode.REQUEST_SUCCESS.getCode());
        result.setData(null);
        return result;
    }

    /**
     * 操作成功
     *
     * @param data 数据
     * @param <T>  泛型
     * @return 响应对象
     */
    public static <T> Result<T> success(T data) {
        ApiResult<T> result = new ApiResult<>(ResultCode.REQUEST_SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    /**
     * 分页成功返回
     *
     * @param data  数据
     * @param total 总数
     * @param <T>   泛型
     * @return 响应对象
     */
    public static <T> Result<T> success(T data, Integer total) {
        ApiResult<T> result = new ApiResult<>(ResultCode.REQUEST_SUCCESS.getCode());
        result.setData(data);
        result.setTotal(total);
        return result;
    }

    /**
     * 操作成功
     *
     * @param msg 消息
     * @param <T> 泛型
     * @return 响应对象
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultCode.REQUEST_SUCCESS.getCode(), msg);
    }

    /**
     * 操作成功
     *
     * @param msg  消息
     * @param data 数据
     * @param <T>  泛型
     * @return 响应对象
     */
    public static <T> Result<T> success(String msg, T data) {
        return new ApiResult<>(ResultCode.REQUEST_SUCCESS.getCode(), msg, data);
    }

    /**
     * 操作失败
     *
     * @param msg 消息
     * @param <T> 泛型
     * @return 响应对象
     */
    public static <T> Result<T> error(String msg) {
        return new Result<T>(ResultCode.REQUEST_ERROR.getCode(), msg);
    }

    /**
     * 操作失败
     *
     * @param message 消息
     * @param <T>     泛型
     * @return 响应对象
     */
    public static <T> Result<T> fail(String message) {
        return new ApiResult<>(ResultCode.REQUEST_FAIL.getCode(), message);
    }

    /**
     * 操作失败
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     泛型
     * @return 响应对象
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new ApiResult<>(code, message);
    }

//    //快速返回操作成功响应结果(带响应数据)
//    public static <E> Result<E> success(E data) {
//
//        return new Result<>(0, "操作成功", data);
//    }
}
