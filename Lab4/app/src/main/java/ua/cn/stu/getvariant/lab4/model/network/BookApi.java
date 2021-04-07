package ua.cn.stu.getvariant.lab4.model.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {
    @GET("/search.json/")
    Call<BookNetworkEntity> getBooks(@Query("author") String book);

}
