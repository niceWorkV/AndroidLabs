package ua.cn.stu.getvariant.lab4.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.cn.stu.getvariant.lab4.App;
import ua.cn.stu.getvariant.lab4.Details.DetailsActivity;
import ua.cn.stu.getvariant.lab4.R;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private RecyclerView booksList;
    private EditText bookEditText;
    private ProgressBar progress;
    private TextView emptyTextView;
    private TextView errorTextView;

    private MainViewModel viewModel;

    private BookAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton =findViewById(R.id.searchButton);
        booksList = findViewById(R.id.bookList);
        bookEditText = findViewById(R.id.bookEditText);
        emptyTextView = findViewById(R.id.emptyTextView);
        errorTextView = findViewById(R.id.errorTextView);
        progress = findViewById(R.id.progress);


        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this,app.getViewModelFactory());
        viewModel = viewModelProvider.get(MainViewModel.class);

        viewModel.getViewState().observe(this,state ->{
            searchButton.setEnabled(state.isEnableSearchButton());
            booksList.setVisibility(toVisibility(state.isShowList()));
            progress.setVisibility(toVisibility(state.isShowProgress()));
            emptyTextView.setVisibility(toVisibility(state.isShowEmptyHint()));
            errorTextView.setVisibility(toVisibility(state.isShowError()));

            adapter.setBooks(state.getBooks());
        });

        searchButton.setOnClickListener(v->{
            String book = bookEditText.getText().toString();
            viewModel.getBook(book);
        });

        initBooksList();
    }

    private void initBooksList(){
        LayoutManager layoutManager = new LinearLayoutManager(this);
        booksList.setLayoutManager(layoutManager);
        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_BOOK_ID, book.getId());
            intent.putExtra(DetailsActivity.EXTRA_AUTHOR,bookEditText.getText().toString());
            startActivity(intent);
        });
        booksList.setAdapter(adapter);

    }

    static int toVisibility(boolean show){
        return show ? View.VISIBLE: View.GONE;
    }
}