package ua.cn.stu.getvariant.lab4.Main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import ua.cn.stu.getvariant.lab4.BaseViewModel;
import ua.cn.stu.getvariant.lab4.model.Book;
import ua.cn.stu.getvariant.lab4.model.BookService;
import ua.cn.stu.getvariant.lab4.model.CallBack;
import ua.cn.stu.getvariant.lab4.model.Cancellabe;
import ua.cn.stu.getvariant.lab4.model.Result;

public class MainViewModel extends BaseViewModel {

    private Result<List<Book>> bookResult = Result.empty();
    private MutableLiveData<ViewState> stateLiveData = new MutableLiveData<>();
    private Cancellabe cancellable;

    @Override
    protected void onCleared() {
        super.onCleared();
        if(cancellable != null){
            cancellable.canle();
        }
    }

    {
        updateViewState(Result.empty());
        //updateViewState(Result.loading());

       /* updateViewState(Result.success(Arrays.asList(
                new Book( 1, "bookk1", "desk1"),
                new Book( 2, "bookk2", "desk1"),
                new Book( 3, "bookk3", "desk1")

        )));*/
    }

    public MainViewModel(BookService bookService) {
        super(bookService);
    }

    public LiveData<ViewState> getViewState(){
        return stateLiveData;
    }

    public void updateViewState(Result<List<Book>> bookResult){
        this.bookResult=bookResult;
        ViewState state = new ViewState();
        state.enableSearchButton = bookResult.getStatus() != Result.Status.LOADING;
        state.showList = bookResult.getStatus() == Result.Status.SUCCESS;
        state.showEmptyHint = bookResult.getStatus() ==Result.Status.EMPTY;
        state.showError = bookResult.getStatus() == Result.Status.ERROR;
        state.showProgress = bookResult.getStatus() == Result.Status.LOADING;
        state.books = bookResult.getData();
        stateLiveData.postValue(state);
    }

    public void getBook(String book){
        updateViewState(Result.loading());
        cancellable = getBookService().getBook(book, new CallBack<List<Book>>() {
            @Override
            public void onError(Throwable error) {
                if(bookResult.getStatus() != Result.Status.SUCCESS){
                    updateViewState(Result.error(error));
                }

            }

            @Override
            public void onResults(List<Book> data) {
                updateViewState(Result.success(data));
            }

        });
    }

    static class ViewState{
        private boolean enableSearchButton;
        private boolean showList;
        private boolean showEmptyHint;
        private boolean showError;
        private boolean showProgress;
        private List<Book> books;

        public boolean isEnableSearchButton() {
            return enableSearchButton;
        }

        public boolean isShowList() {
            return showList;
        }

        public boolean isShowEmptyHint() {
            return showEmptyHint;
        }

        public boolean isShowError() {
            return showError;
        }

        public boolean isShowProgress() {
            return showProgress;
        }

        public List<Book> getBooks() {
            return books;
        }
    }
}
