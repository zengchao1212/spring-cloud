package com.xiangyi.cloud.boot.controller;

/**
 * Created by bobo on 2017/5/26.
 */
public class Response<T> {
    private String code;
    private String errorMsg;
    private T result;

    /**
     * 反序列号需要用到此构造方法
     */
    public Response(){
    }
    public Response(T t){
        this.result=t;
        this.code="SUCCESS";
    }
    public Response(String code,String errorMsg){
        this.code=code;
        this.errorMsg=errorMsg;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
