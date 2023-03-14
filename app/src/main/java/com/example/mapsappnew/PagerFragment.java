package com.example.mapsappnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mapsappnew.databinding.FragmentPagerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class PagerFragment extends Fragment {

    private FragmentPagerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager, container, false);
        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a list of fragments to display in the ViewPager
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SwipeFragment.newInstance("Fragment 1"));
        fragmentList.add(SwipeFragment.newInstance("Fragment 2"));
        fragmentList.add(SwipeFragment.newInstance("Fragment 3"));
        fragmentList.add(SwipeFragment.newInstance("Fragment 4"));

        // Create a PagerAdapter and set it on the ViewPager
        PagerAdapter adapter = new PagerAdapter(requireActivity(), fragmentList);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText("")
        ).attach();
    }
}