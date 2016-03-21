package com.example.v15.migoproductcatalog.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.v15.migoproductcatalog.Adapter.CollectionAdapter;
import com.example.v15.migoproductcatalog.Database.DatabaseHelper;
import com.example.v15.migoproductcatalog.Fragments.BaseFragment;
import com.example.v15.migoproductcatalog.Fragments.CollectionViewFragment;
import com.example.v15.migoproductcatalog.Fragments.FragmentDrawer;
import com.example.v15.migoproductcatalog.Model.Administrator;
import com.example.v15.migoproductcatalog.Model.Products;
import com.example.v15.migoproductcatalog.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, BaseFragment.CommunicateToActivity {
    private static final int URL_LOADER = 0;
    private boolean isAdmin=false;
    private FragmentDrawer drawerFragment;
    private CollectionViewFragment collectionViewFragment;
    private CollectionAdapter adapter;
    private Toolbar toolbar;
    private DatabaseHelper db;
    private ArrayList<Products> Products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        if(i!=null){
            Administrator.getInstance().flag=i.getBooleanExtra("admin",false);
            if( Administrator.getInstance().flag) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);


        db = new DatabaseHelper(this);


        FragmentManager fragmentManager = getSupportFragmentManager();
        //Fragment currFragment = fragmentManager.findFragmentById(R.id.container_body);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        collectionViewFragment = new CollectionViewFragment();
        fragmentTransaction.add(R.id.container_body, collectionViewFragment, "collectionViewFragment");
        fragmentTransaction.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(collectionViewFragment.materialSheetFab.isSheetVisible()){
            collectionViewFragment.materialSheetFab.hideSheet();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gridview) {
            collectionViewFragment.changeView(1);
            return true;
        }

        if (id == R.id.action_listview) {
            collectionViewFragment.changeView(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeDataSet(int position){
        ArrayList<Products> items = new ArrayList<>();

        switch (position){
            case 0: //All items
                collectionViewFragment.getAllProducts();
                break;
            case 1: //Music
                collectionViewFragment.getMusic();
                getSupportActionBar().setTitle("Migo "+ getResources().getString(R.string.nav_item_music));
                break;
            case 2: //Books
                //int index=db.getMediaType("Books");
                collectionViewFragment.getBooks();
                getSupportActionBar().setTitle("Migo " + getResources().getString(R.string.nav_item_books));
                break;
            case 3:
                collectionViewFragment.getMovies();
                getSupportActionBar().setTitle("Migo " + getResources().getString(R.string.nav_item_movies));
                break;
        }


    }



    @Override
    public void onDrawerItemSelected(View view, int position) {
        changeDataSet(position);
    }


    @Override
    public void initializeData() {
        changeDataSet(0);
    }
}
