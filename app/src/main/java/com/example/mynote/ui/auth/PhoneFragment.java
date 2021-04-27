package com.example.mynote.ui.auth;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mynote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {

    private EditText editPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CountDownTimer countDownTimer;


    private ConstraintLayout constraintSend;
    private ConstraintLayout constraintConfirm;
    private TextView txtCounterTime;
    private TextView txtAlertSend;
    private String verificationCode;
    private EditText editConfirm;
    private Button btnConfirm;
    private final String messageSendCodeAgain = "Отправить код ещё раз: ";
    private final String editErrorEnterNumber = "Enter phone number";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.phoneNum);

        constraintSend = view.findViewById(R.id.constSend);
        constraintConfirm = view.findViewById(R.id.constConfirm);
        txtCounterTime = view.findViewById(R.id.timeCounterView);
        txtAlertSend = view.findViewById(R.id.alertSend);
        editConfirm = view.findViewById(R.id.editEnterCode);
        btnConfirm = view.findViewById(R.id.btnAccept);

        countDownTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCounterTime.setText(messageSendCodeAgain + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                constraintConfirm.setVisibility(View.GONE);
                constraintSend.setVisibility(View.VISIBLE);
                txtAlertSend.setVisibility(View.VISIBLE);
            }
        }.start();


        view.findViewById(R.id.btnGetMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editPhone.getText().toString().trim();
                if (code.isEmpty()) {
                    editPhone.setError(editErrorEnterNumber);
                    editPhone.requestFocus();
                    return;
                } else {
                    constraintSend.setVisibility(View.GONE);
                    constraintConfirm.setVisibility(View.VISIBLE);
                }
                requestSms();
            }
        });
        setCallBacks();

        view.findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editConfirm.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    requireActivity().finish();
                }
            }
        });
    }

    private void setCallBacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG", "createUserWithEmail:success");
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("TAG", "createUserWithEmail:failure");
                Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.action_phoneFragment_to_navigation_note);
                } else {
                    Toast.makeText(requireContext(), "Enter another code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void requestSms() {
        String phoneNum = editPhone.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNum)   // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                        .setActivity(requireActivity())    // Activity (for callback binding)
                        .setCallbacks(mCallbacks)    // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}
