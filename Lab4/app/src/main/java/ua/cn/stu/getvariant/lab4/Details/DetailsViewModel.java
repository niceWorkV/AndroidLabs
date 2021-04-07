package ua.cn.stu.getvariant.lab4.Details;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ua.cn.stu.getvariant.lab4.BaseViewModel;
import ua.cn.stu.getvariant.lab4.model.Book;
import ua.cn.stu.getvariant.lab4.model.BookService;
import ua.cn.stu.getvariant.lab4.model.CallBack;
import ua.cn.stu.getvariant.lab4.model.Cancellabe;
import ua.cn.stu.getvariant.lab4.model.Result;

public class DetailsViewModel extends BaseViewModel {

    private MutableLiveData<Result<Book>> bookLiveData = new MutableLiveData<>();
    private Cancellabe cancellabe;

    {
        bookLiveData.postValue(Result.empty());
    }

    public DetailsViewModel(BookService bookService) {
        super(bookService);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(cancellabe != null){
            cancellabe.canle();
        }
    }

    public void loadDetails(int id){
        bookLiveData.postValue(Result.loading());
        cancellabe = getBookService().getBookById(id, new CallBack<Book>()
        {
            @Override
            public void onError(Throwable error) {
                bookLiveData.postValue(Result.error(error));
            }

            @Override
            public void onResults(Book data) {
                bookLiveData.postValue(Result.success(data));
            }

        });

    }

    public LiveData<Result<Book>> getResult(){
        return bookLiveData;
    }
}
