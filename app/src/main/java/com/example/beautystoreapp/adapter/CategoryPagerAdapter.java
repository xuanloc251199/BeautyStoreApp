package com.example.beautystoreapp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.beautystoreapp.model.Category;
import com.example.beautystoreapp.view.CategoryDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryPagerAdapter extends FragmentPagerAdapter {

    private List<Category> categories;

    public CategoryPagerAdapter(FragmentManager fm, List<Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        // Tạo mới fragment tương ứng với mỗi danh mục
        return CategoryDetailFragment.newInstance(categories.get(position).getId());
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
    }
}

