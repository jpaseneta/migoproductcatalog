package com.example.v15.migoproductcatalog.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.v15.migoproductcatalog.Adapter.CollectionAdapter;
import com.example.v15.migoproductcatalog.Adapter.NavigationDrawerAdapter;
import com.example.v15.migoproductcatalog.Animations.CustomFab;
import com.example.v15.migoproductcatalog.Database.DatabaseHelper;
import com.example.v15.migoproductcatalog.Model.Administrator;
import com.example.v15.migoproductcatalog.Model.Books;
import com.example.v15.migoproductcatalog.Model.Movies;
import com.example.v15.migoproductcatalog.Model.Music;
import com.example.v15.migoproductcatalog.Model.Products;
import com.example.v15.migoproductcatalog.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionViewFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int URL_LOADER = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CommunicateToActivity mcallback;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CollectionAdapter adapter;
    private ArrayList<Products> Products = new ArrayList<>();
    private DatabaseHelper db;
    private boolean isList = true;
    private CheckBox checkboxFilter;
    private LinearLayout filterLayout;
    private SearchView searchView;
    private Boolean booksView=false;
    private Boolean musicView=false;
    private Boolean moviesView=false;
    public MaterialSheetFab<CustomFab> materialSheetFab;
    private Spinner mediaTypeSpinner;
    private Spinner genreSpinner;
    private String selectedGenre;
    private String selectedMediaType;
    private EditText newProductTitle;
    private EditText productOtherInfo;
    private Button uploadImage;
    private Button addProduct;
    private TextView fileLabel;

    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    public CollectionViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionViewFragment newInstance(String param1, String param2) {
        CollectionViewFragment fragment = new CollectionViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search by title..");
        searchView.setOnQueryTextListener(this);
        Log.d("CollectionViewFragment", "searchview was set");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        final View layout = inflater.inflate(R.layout.fragment_collection_view, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.collection_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        filterLayout = (LinearLayout) layout.findViewById(R.id.filterlayout);
        checkboxFilter = (CheckBox) layout.findViewById(R.id.checkboxfilter);
        checkboxFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxFilter.isChecked()){
                    searchView.setQueryHint("Search by "+ checkboxFilter.getText()+"...");
                }else{
                    searchView.setQueryHint("Search by Title...");
                }
            }
        });

        CustomFab fab = (CustomFab) layout.findViewById(R.id.fab);
        View sheetView = layout.findViewById(R.id.fab_sheet);
        View overlay = layout.findViewById(R.id.dim_overlay);
        int sheetColor = getResources().getColor(R.color.windowBackground);
        int fabColor = getResources().getColor(R.color.colorAccent);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);

        if(Administrator.getInstance().flag){
            fab.setVisibility(View.VISIBLE);
        }else {
            fab.setVisibility(View.GONE);
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(new FloatingActionButton.Behavior());
            fab.setLayoutParams(p);
        }

        mediaTypeSpinner = (Spinner) layout.findViewById(R.id.mediatypeSpinner);

        genreSpinner = (Spinner) layout.findViewById(R.id.genreSpinner);
        newProductTitle = (EditText) layout.findViewById(R.id.producttitle);
        productOtherInfo = (EditText) layout.findViewById(R.id.productotherinfo);
        uploadImage = (Button) layout.findViewById(R.id.uploadimage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        fileLabel = (TextView) layout.findViewById(R.id.fileLabel);
        fileLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uploadImage.setError(null);
            }
        });
        addProduct = (Button) layout.findViewById(R.id.addproduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    MaterialStyledDialog dialog = new MaterialStyledDialog(getActivity())
                            .setTitle("Add new product!")
                            .setDescription("Are you sure you want to add this product?")
                            .withDialogAnimation(true, Duration.NORMAL)
                            .setPositive(getResources().getString(R.string.proceed), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    com.example.v15.migoproductcatalog.Model.Products newProduct = new Products();
                                    newProduct.setTitle(newProductTitle.getText().toString());
                                    newProduct.setFileName(imgDecodableString);
                                    int mediaType = db.getMediaTypeId(mediaTypeSpinner.getSelectedItem().toString());

                                    newProduct.setGenreID(db.getGenreIDFromGenreName(selectedGenre));
                                    newProduct.setGenreName(selectedGenre);
                                    newProduct.setExternalFlag(1);
                                    newProduct.setOtherInfo(productOtherInfo.getText().toString());
                                    Products newItem = db.addNewProduct(newProduct, mediaType);
                                    if (newItem != null) {
                                        Products.add(0, newItem);
                                        adapter.addItem(0, newItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Snackbar snackbar = Snackbar
                                            .make(layout, newItem.getTitle()+ " was added", Snackbar.LENGTH_LONG);

                                    snackbar.show();
                                    materialSheetFab.hideSheet();
                                }
                            })
                            .setNegative(getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .build();
                    dialog.show();

                }


            }
        });

        return layout;
    }

    private boolean validateFields(){
        // Reset errors.
        newProductTitle.setError(null);
        productOtherInfo.setError(null);
        uploadImage.setError(null);


        // Store values at the time of the login attempt.
        String productTitle = newProductTitle.getText().toString();
        String otherInfo = productOtherInfo.getText().toString();
        String uploadedImage = fileLabel.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(productTitle)) {
            newProductTitle.setError(getString(R.string.error_field_required));
            focusView = newProductTitle;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(otherInfo)) {
            productOtherInfo.setError(getString(R.string.error_field_required));
            focusView = productOtherInfo;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(uploadedImage)) {
            uploadImage.setError(getString(R.string.error_image_required));
            focusView = uploadImage;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        mcallback.initializeData();

        loadMediaSpinner();
        mediaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMediaType = parent.getItemAtPosition(position).toString();
                loadGenreSpinner(selectedMediaType);
                switch (selectedMediaType) {
                    case "Music":
                        productOtherInfo.setHint("Artist");
                        productOtherInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case "Books":
                        productOtherInfo.setHint("Author");
                        productOtherInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case "Movies":
                        productOtherInfo.setHint("Year");
                        productOtherInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenre = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getBooks(){
        checkboxFilter.setText("Author");
        checkboxFilter.setChecked(false);
        filterLayout.setVisibility(View.VISIBLE);
        Products.clear();
        Products.addAll(db.getAllBooks());
        adapter = new CollectionAdapter(getActivity(),Products,isList);
        recyclerView.setAdapter(adapter);
        if(!isList)
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
        Log.d("PRODUCTCOUNTBOOKS", "" + adapter.getItemCount());
        booksView=true;
        moviesView=false;
        musicView=false;

    }

    public void getMovies(){
        checkboxFilter.setText("Year");
        checkboxFilter.setChecked(false);
        filterLayout.setVisibility(View.VISIBLE);
        Products.clear();
        Products.addAll(db.getAllMovies());
        adapter = new CollectionAdapter(getActivity(),Products,isList);
        recyclerView.setAdapter(adapter);
        if(!isList)
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
        Log.d("PRODUCTCOUNTMOVIES", "" + adapter.getItemCount());
        booksView=false;
        moviesView=true;
        musicView=false;
    }

    public void getMusic(){
        checkboxFilter.setText("Artist");
        checkboxFilter.setChecked(false);
        filterLayout.setVisibility(View.VISIBLE);
        Products.clear();
        Products.addAll(db.getAllMusic());
        adapter = new CollectionAdapter(getActivity(),Products,isList);
        recyclerView.setAdapter(adapter);
        if(!isList)
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
        Log.d("PRODUCTCOUNTMUSIC", "" + adapter.getItemCount());
        booksView=false;
        moviesView=false;
        musicView=true;
    }

    public void getAllProducts(){
        filterLayout.setVisibility(View.GONE);
        Products.clear();
        Products.addAll(db.getAllProducts());
        adapter = new CollectionAdapter(getActivity(),Products,isList);
        recyclerView.setAdapter(adapter);
        if(!isList)
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
        Log.d("PRODUCTCOUNTALL", "" + adapter.getItemCount());
    }


    public void loadMediaSpinner(){
        List<String> mediaTypes = db.getMediaTypes();

        // Creating adapter for spinner
        ArrayAdapter<String> mediaSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, mediaTypes);
        mediaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mediaTypeSpinner.setAdapter(mediaSpinnerAdapter);
    }

    public void loadGenreSpinner(String medianame) {
        List<String> genre = db.getGenreNames(db.getMediaTypeId(medianame));

        // Creating adapter for spinner
        ArrayAdapter<String> genreSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item,genre);
        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreSpinnerAdapter);
    }

    public void changeView(int type){
        switch (type){
            case 1:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                isList=false;
                adapter = new CollectionAdapter(getActivity(),Products,isList);
                recyclerView.setAdapter(adapter);
                break;
            case 2:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                isList=true;
                adapter = new CollectionAdapter(getActivity(),Products,isList);
                recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                fileLabel.setText(FilenameUtils.getName(imgDecodableString));
                cursor.close();
//                ImageView imgView = (ImageView) findViewById(R.id.imgView);
//                // Set the Image in ImageView after decoding the String
//                imgView.setImageBitmap(BitmapFactory
//                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mcallback = (CommunicateToActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement CommunicateToActivity");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final ArrayList<Products> filteredProductList = filter(Products, query);
        adapter.animateTo(filteredProductList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private ArrayList<Products> filter(ArrayList<Products> products, String query) {
        query = query.toLowerCase();
        String text="";
        final ArrayList<Products> filteredProductList = new ArrayList<>();
        if(booksView && checkboxFilter.isChecked()) {
            for (Products product : products) {
                text = ((Books) product).getAuthor().toLowerCase();
                if (text.contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }else if (musicView && checkboxFilter.isChecked()){
            for (Products product : products) {
                text = ((Music) product).getArtist().toLowerCase();
                if (text.contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }else if (moviesView && checkboxFilter.isChecked()){
            for (Products product : products) {
                text = Integer.toString(((Movies) product).getYear());
                if (text.contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }else {
            for (Products product : products) {
                text =  product.getTitle().toLowerCase();
                if (text.contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }
        return filteredProductList;
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
