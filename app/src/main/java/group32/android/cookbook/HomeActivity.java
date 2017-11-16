package group32.android.cookbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import group32.android.cookbook.models.Post;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomListItemRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Post> listData = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        recyclerView = ((RecyclerView)findViewById(R.id.recyclerView));


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomListItemRecyclerAdapter(this, listData);
        recyclerView.setAdapter(adapter);


        listData.add(new Post("Author : HB 1", "Test 1", "Éo có Reicpe"));
        listData.add(new Post("Author : HB 2", "Test 2", "Éo có Reicpe"));
        listData.add(new Post("Author : HB 3", "Test 3", "Éo có Reicpe"));
        listData.add(new Post("Author : HB 4", "Test 4", "Éo có Reicpe"));

        adapter.notifyDataSetChanged();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.cookbook_menu, menu);
        return true;
    }   */
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.cookbook_menu, popup.getMenu());
        popup.show() ;
    }
}
