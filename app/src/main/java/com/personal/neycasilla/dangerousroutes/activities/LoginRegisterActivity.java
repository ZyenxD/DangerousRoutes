package com.personal.neycasilla.dangerousroutes.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.personal.neycasilla.dangerousroutes.R;
import com.personal.neycasilla.dangerousroutes.fragments.DangerMapFragment;
import com.personal.neycasilla.dangerousroutes.fragments.LoginFragment;
import com.personal.neycasilla.dangerousroutes.fragments.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity implements
        RegisterFragment.OnFragmentRegisterListner,
        LoginFragment.OnLoginFragmentListener{

    private RegisterFragment registerFragment;
    private LoginFragment loginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        createAccount();
    }

    @Override
    public void createAccount() {
        loginFragment = new LoginFragment();
        loginFragment.setLoginFragmentListener(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_login_register_container,loginFragment).commit();
    }

    @Override
    public void sigIn() {
        Intent intent = new Intent(LoginRegisterActivity.this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void userRegister() {

        registerFragment = new RegisterFragment();
        registerFragment.setFragmentRegisterListner(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_login_register_container,registerFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(registerFragment!=null&&loginFragment!=null){
            registerFragment.setFragmentRegisterListner(this);
            loginFragment.setLoginFragmentListener(this);
        }
    }
}
