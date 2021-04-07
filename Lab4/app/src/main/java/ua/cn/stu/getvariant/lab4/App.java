package ua.cn.stu.getvariant.lab4;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.cn.stu.getvariant.lab4.Main.db.AppDatabase;
import ua.cn.stu.getvariant.lab4.Main.db.BookDAO;
import ua.cn.stu.getvariant.lab4.logger.AndroidLogger;
import ua.cn.stu.getvariant.lab4.logger.Logger;
import ua.cn.stu.getvariant.lab4.model.BookService;
import ua.cn.stu.getvariant.lab4.model.network.BookApi;

public class App extends Application {

    private ViewModelProvider.Factory viewModelFactory;
    private static final String BASE_URL = "http://openlibrary.org/";

    @Override
    public void onCreate() {
        super.onCreate();
        Logger logger = new AndroidLogger();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookApi bookApi = retrofit.create(BookApi.class);

        ExecutorService executorService = Executors.newCachedThreadPool();

        AppDatabase appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database.db").build();
        BookDAO bookDAO = appDatabase.getBookDAO();
        BookService bookService = new BookService(bookApi,bookDAO,executorService,logger);
        viewModelFactory = new ViewModelFactory(bookService);
    }

    public ViewModelProvider.Factory getViewModelFactory(){
        return viewModelFactory;
    }
}
