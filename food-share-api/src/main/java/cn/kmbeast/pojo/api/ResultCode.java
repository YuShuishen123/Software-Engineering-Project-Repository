package cn.kmbeast.pojo.api;

/**
 * 请求响应码
 */
public enum ResultCode {
    /**
     * 请求成功码
     */
    REQUEST_SUCCESS(200),
    /**
     * 请求失败码
     */
    REQUEST_ERROR(400),
    /**
     * 请求失败（业务）
     */
    REQUEST_FAIL(500);

    private Integer code;

    ResultCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                '}';
    }
}
