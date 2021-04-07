package ua.cn.stu.getvariant.lab4;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

import ua.cn.stu.getvariant.lab4.model.BookService;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewModelFactory implements ViewModelProvider.Factory {

    private BookService bookService;


    public ViewModelFactory(BookService bookService) {
        this.bookService = bookService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Constructor<T> constructor= null;
        try {
            constructor = modelClass.getConstructor(BookService.class);
            return constructor.newInstance(bookService);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            RuntimeException wrapper = new RuntimeException();
            wrapper.initCause(e);
            throw wrapper;
        }
    }
}
