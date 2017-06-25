package com.drowsyatmidnight.nytarticle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.drowsyatmidnight.adapter.ArticleAdapter;
import com.drowsyatmidnight.adapter.SortOrderAdapter;
import com.drowsyatmidnight.model.SearchResult;
import com.drowsyatmidnight.utils.ArticleAPI;
import com.drowsyatmidnight.utils.RetrofitUtils;
import com.mingle.widget.LoadingView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainArticle extends AppCompatActivity {

    private SearchRequest searchRequest;
    private ArticleAPI articleApi;
    private ArticleAdapter articleAdapter;
    private SearchView searchView;
    private Calendar myCalendar;
    private String beginDate;
    @BindView(R.id.lvArticle)
    RecyclerView lvArticle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressMain)
    LoadingView progressMain;
    @BindView(R.id.progressLoadMore)
    LoadingView progressLoadMore;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getActiveNetworkInfo()!=null){
                Toast.makeText(MainArticle.this, "Network is Connected, swipe to refresh", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainArticle.this, "No network is Connected", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private interface Listerner{
        void onResult(SearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_article);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        progressMain.setVisibility(View.VISIBLE);
        setUpView();
        setUpApi();
        search();
        addEvents();
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(v -> searchView.setIconified(true));
        articleAdapter.setListener(this::searchMore);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressMain.setVisibility(View.VISIBLE);
            searchRequest.resetPage();
            setUpView();
            search();
            articleAdapter.setListener(this::searchMore);
        });
    }

    private void searchMore() {
        searchRequest.nextPage();
        progressLoadMore.setVisibility(View.VISIBLE);
        fetchArticle(searchResult -> {
            articleAdapter.appendData(searchResult.getArticles());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnSearchClickListener(v -> {
            menu.findItem(R.id.action_filter).setVisible(false);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        });
        searchView.setOnCloseListener(() -> {
            menu.findItem(R.id.action_filter).setVisible(true);
            toolbar.setNavigationIcon(null);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchRequest.setQuery(query);
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_filter:
                filterClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpView() {
        articleAdapter = new ArticleAdapter(MainArticle.this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        lvArticle.setLayoutManager(layoutManager);
        lvArticle.setAdapter(articleAdapter);
    }

    private void setUpApi() {
        searchRequest = new SearchRequest();
        articleApi = RetrofitUtils.GET().create(ArticleAPI.class);
    }

    private void search(){
        searchRequest.resetPage();
        fetchArticle(searchResult -> {
            articleAdapter.setData(searchResult.getArticles());
        });
    }

    private void fetchArticle(final Listerner listener){
        articleApi.search(searchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    listener.onResult(response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressMain.setVisibility(View.GONE);
                    progressLoadMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d("FailCallback", t.toString());
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(broadcastReceiver,intentFilter);
            }
        });
    }

    private void filterClick(){
        View view = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        EditText edDate = (EditText) view.findViewById(R.id.edDate);
        Spinner spSort = (Spinner) view.findViewById(R.id.spSort);
        CheckBox ckArts = (CheckBox) view.findViewById(R.id.ckArts);
        CheckBox ckFashionStyle =(CheckBox) view.findViewById(R.id.ckFashionStyle);
        CheckBox ckSport = (CheckBox) view.findViewById(R.id.ckSport);
        Button btnOK = (Button) view.findViewById(R.id.btnOK);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Dialog filterDialog = new Dialog(MainArticle.this, R.style.MaterialDialogSheet);
        filterDialog.setContentView(view);
        filterDialog.setCancelable(true);
        filterDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        filterDialog.getWindow().setGravity(Gravity.BOTTOM);
        filterDialog.show();
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(edDate);
        };
        edDate.setInputType(InputType.TYPE_NULL);
        edDate.setOnClickListener(v -> {
            new DatePickerDialog(MainArticle.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        SortOrderAdapter sortOrderAdapter = new SortOrderAdapter(MainArticle.this, R.layout.sort_order, orders());
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(sortOrderAdapter);
        btnOK.setOnClickListener(v -> {
            if(edDate.getText().toString().isEmpty()){
                //nothing
            }else {
                searchRequest.setBeginDate(beginDate.replaceAll("/",""));
            }
            searchRequest.setOrder(spSort.getSelectedItem().toString());
            Boolean hasArts, hasFashionStyle, hasSport;
            if(ckArts.isChecked()){
                hasArts = true;
            } else hasArts = false;
            if(ckFashionStyle.isChecked()){
                hasFashionStyle = true;
            } else hasFashionStyle = false;
            if(ckSport.isChecked()){
                hasSport = true;
            } else hasSport = false;
            searchRequest.setFq(searchRequest.getNewDesk(hasArts, hasFashionStyle, hasSport));
            filterDialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> filterDialog.dismiss());

    }

    private void updateLabel(EditText edDate) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edDate.setText(sdf.format(myCalendar.getTime()));
        myFormat = "yyyy/MM/dd";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        beginDate = sdf.format(myCalendar.getTime());
    }

    private List<String>orders(){
        List<String> listOrder = new ArrayList<>();
        listOrder.add("Newest");
        listOrder.add("Oldest");
        return listOrder;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (broadcastReceiver != null) {
                this.unregisterReceiver(broadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            broadcastReceiver = null;
        }
    }
}
