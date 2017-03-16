package com.example.administrator.tests.Fragmnt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.tests.R;

public class GitHubFragment extends Fragment {


    public GitHubFragment() {
    }

    public static GitHubFragment newInstance() {
        GitHubFragment fragment = new GitHubFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_github, container, false);
        return view;
    }
}
