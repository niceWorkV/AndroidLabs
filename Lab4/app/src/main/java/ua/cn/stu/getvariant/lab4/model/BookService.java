package ua.cn.stu.getvariant.lab4.model;


import androidx.lifecycle.LiveData;

import com.annimon.stream.Stream;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.cn.stu.getvariant.lab4.Main.db.BookDAO;
import ua.cn.stu.getvariant.lab4.Main.db.BookDbEntity;
import ua.cn.stu.getvariant.lab4.logger.Logger;
import ua.cn.stu.getvariant.lab4.model.network.BookApi;
import ua.cn.stu.getvariant.lab4.model.network.BookNetworkEntity;

public class BookService {
    private ExecutorService executorService;
    private BookDAO bookDAO;
    private BookApi bookApi;
    private Logger logger;
    private List<BookDocs> bDocs;

    public BookService(BookApi bookApi,BookDAO bookDAO,ExecutorService executorService ,Logger logger) {
        this.logger = logger;
        this.bookDAO = bookDAO;
        this.bookApi = bookApi;
        this.executorService = executorService;
    }

    public Cancellabe getBook(String author, CallBack<List<Book>> callBack){
        Future<?> future = executorService.submit(()->{

            try {
            List<BookDbEntity> entities = bookDAO.getBooks(author);
            List<Book> books = convertToBookEntities(entities);
            callBack.onResults(books);

            Response<BookNetworkEntity> response = bookApi.getBooks(author).execute();
            if(response.isSuccessful()){
                BookNetworkEntity bne = response.body();
                List<BookDbEntity> newBooks = networkToDbEntities(author,bne.getDocs());
                bookDAO.updateBooksForAuthor(author,newBooks);
                callBack.onResults(convertToBookEntities(newBooks));
            } else{
                if(!books.isEmpty()){
                    RuntimeException exception = new RuntimeException("Something happened");
                    logger.e(exception);
                    callBack.onError(exception);
                }
            }

            } catch (IOException e) {
                logger.e(e);
            }
        });

        return new FutureCancallable(future);
/*
        Call<BookNetworkEntity> call = bookApi.getBooks(author);
        call.enqueue(new Callback<BookNetworkEntity>() {
            @Override
            public void onResponse(Call<BookNetworkEntity> call, Response<BookNetworkEntity> response) {
                if(response.isSuccessful()){
                    BookNetworkEntity entity = response.body();
                    callBack.onResults(convertToBookEntities(entity.getDocs()));
                } else {
                    callBack.onError(new RuntimeException("Error"));

                }
            }

            @Override
            public void onFailure(Call<BookNetworkEntity> call, Throwable t) {
                logger.e(t);
                callBack.onError(new RuntimeException("Network error"));
            }
        });
        return ()->{};
        */

    }

    public Cancellabe getBookById(int id, CallBack<Book> callBack)
    {
        Future<?> future = executorService.submit(()->{
            BookDbEntity dbEntity = bookDAO.getById(id);
            Book book = new Book(dbEntity);
            callBack.onResults(book);
        });
        return new FutureCancallable(future);
    }


    public List<BookDocs> getbDocs(){
        return bDocs;
    }

    private List<BookDbEntity> networkToDbEntities(String author,List<BookDocs> bookDocs){
        return Stream.of(bookDocs)
                .map(networkEntities -> new BookDbEntity(author, networkEntities))
                .toList();
    }


    private List<Book> convertToBookEntities(List<BookDbEntity> bookDocs) {
        return Stream.of(bookDocs)
                .map(Book::new)
                .toList();
    }

    static class FutureCancallable implements Cancellabe{
    private  Future<?> future;
    public FutureCancallable(Future<?> future){
        this.future= future;
    }
    @Override
    public void canle() {
        future.cancel(true);
        }
    }
}
