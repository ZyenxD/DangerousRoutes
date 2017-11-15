package com.personal.neycasilla.dangerousroutes.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.personal.neycasilla.dangerousroutes.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText mail;
    private EditText password;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private OnFragmentRegisterListner fragmentRegisterListner;
    private LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment
        linearLayout = view.findViewById(R.id.waiting_screem);
        firebaseAuth = FirebaseAuth.getInstance();
        mail = view.findViewById(R.id.edit_text_mail);
        password = view.findViewById(R.id.edit_text_password);
        register = view.findViewById(R.id.button_register_register);
        linearLayout.setVisibility(View.GONE);
        register.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        String userMail = this.mail.getText().toString();
        String userPassword = password.getText().toString();
        linearLayout.setVisibility(View.VISIBLE);
        register.setFocusable(false);
        if(userMail.equals("")){
            Toast.makeText(getActivity(),"El campo de mail es requerido",
                    Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
            register.setFocusable(true);
        }
        if(userPassword.equals("")){
            Toast.makeText(getActivity(),"Ingrese una contraseña",
                    Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
            register.setFocusable(true);
        }
        if(!userMail.equals("") && !userPassword.equals("")){
            if(userMail.contains("@") && userMail.contains(".com")){
                if(userPassword.length()>=6){
                    firebaseAuth.createUserWithEmailAndPassword(userMail,userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Registration Error",Toast.LENGTH_SHORT).show();
                                linearLayout.setVisibility(View.GONE);
                                register.setFocusable(true);
                            }else {
                                Toast.makeText(getActivity(),"Successful Registration",Toast.LENGTH_SHORT).show();
                                linearLayout.setVisibility(View.GONE);
                                register.setFocusable(true);
                                fragmentRegisterListner.createAccount();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"La contraseña debe de tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                    register.setFocusable(true);
                    linearLayout.setVisibility(View.GONE);
                }
            }else {
                Toast.makeText(getActivity(),"Formato de mail incorecto",Toast.LENGTH_SHORT).show();
                register.setFocusable(true);
                linearLayout.setVisibility(View.GONE);
            }
        }
    }

    public interface OnFragmentRegisterListner {
        void createAccount();
    }


    public void setFragmentRegisterListner(OnFragmentRegisterListner fragmentRegisterListner) {
        this.fragmentRegisterListner = fragmentRegisterListner;
    }

}
