package com.personal.neycasilla.dangerousroutes.fragments;


import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.personal.neycasilla.dangerousroutes.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "user";
    private FirebaseAuth firebaseAuthentication;
    private FirebaseAuth.AuthStateListener authStateListener;
    private OnLoginFragmentListener loginFragmentListener;
    private EditText mailText;
    private EditText passwordText;
    private Button registerButton;
    private Button logingButon;
    private LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        linearLayout = view.findViewById(R.id.waiting_screem);
        firebaseAuthentication = FirebaseAuth.getInstance();
        mailText = view.findViewById(R.id.edit_login);
        passwordText = view.findViewById(R.id.edit_password);
        logingButon = view.findViewById(R.id.button_login);
        registerButton = view.findViewById(R.id.button_register);
        linearLayout.setVisibility(View.GONE);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"singedIn "+ user.getUid());
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        logingButon.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuthentication.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuthentication.removeAuthStateListener(authStateListener);
        linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAuthentication.addAuthStateListener(authStateListener);
    }

    public void setLoginFragmentListener(OnLoginFragmentListener loginFragmentListener) {
        this.loginFragmentListener = loginFragmentListener;
    }

    private void signIn(String email,String password){
        linearLayout.setVisibility(View.VISIBLE);
        logingButon.setClickable(false);
        registerButton.setClickable(false);
        mailText.setFocusable(false);
        passwordText.setFocusable(false);
        firebaseAuthentication.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Contraseña o Email incorrecto",
                            Toast.LENGTH_SHORT).show();
                    linearLayout.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getActivity(),"Sign in Succesfull!",
                            Toast.LENGTH_SHORT).show();
                    loginFragmentListener.sigIn();
                }
                logingButon.setClickable(true);
                registerButton.setClickable(true);
                mailText.setFocusable(true);
                passwordText.setFocusable(true);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                signIn(mailText.getText().toString(),
                        passwordText.getText().toString());
                break;
            case  R.id.button_register:
                loginFragmentListener.userRegister();
                break;
            default:
                break;
        }
    }

    public interface OnLoginFragmentListener{
        void sigIn();
        void userRegister();
    }

}
