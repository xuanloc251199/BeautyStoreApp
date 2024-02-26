package com.example.beautystoreapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    View mView;

    Button btnLogOut;
    RelativeLayout rvInfoAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setting, container, false);

        init();

        setOnclickListener();

        return mView;
    }

    private void init(){
        btnLogOut = mView.findViewById(R.id.btnLogOut);
        rvInfoAccount = mView.findViewById(R.id.rvInfoAccount);
    }

    private void setOnclickListener() {

        btnLogOut.setOnClickListener(v -> {
            logoutUser();
        });

        rvInfoAccount.setOnClickListener(v -> redirectToInfoAccountActivity());

    }

    private void logoutUser() {
        // Lấy SharedPreferences từ Context của Activity chứa Fragment
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        // Xóa thêm các thông tin khác của người dùng nếu có
        editor.apply();

        // Chuyển hướng người dùng về màn hình đăng nhập
        redirectToLoginActivity();
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish(); // Đảm bảo người dùng không thể quay trở lại Fragment trước bằng cách nhấn nút Back
    }
    private void redirectToInfoAccountActivity() {
        Intent intent = new Intent(getActivity(), InfoAccountActivity.class);
        startActivity(intent);
    }
}