package ua.cn.stu.getvariant.lab4.model;

public interface CallBack<T> {
    void onError(Throwable error);
    void onResults(T data);
}
