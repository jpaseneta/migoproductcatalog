package com.example.v15.migoproductcatalog.Fragments;

import android.support.v4.app.Fragment;

/**
 * Created by V15 on 18/03/2016.
 */
public class BaseFragment extends Fragment {
    public interface CommunicateToActivity {
        //public void getAdapterInstance(RadarItemAdapter adapter);
        void initializeData();
    }
}