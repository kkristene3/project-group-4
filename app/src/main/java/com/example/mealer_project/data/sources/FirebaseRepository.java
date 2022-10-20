package com.example.mealer_project.data.sources;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.ui.LoginScreen;
import com.example.mealer_project.ui.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseRepository {

    final private FirebaseAuth mAuth;
    public AuthActions AUTH;

    public FirebaseRepository(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        this.AUTH = new AuthActions();
    }


    public class AuthActions {

        public void registerUser(String email, String password, SignupActivity signupActivity, User newUser) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    newUser.setUserId(user.getUid());
                                    App.getAppInstance().setUser(newUser);
                                    signupActivity.showNextScreen();
                                } else {
                                    signupActivity.userRegistrationFailed("User registration returned no user info");
                                }
                            } else {
                                if (task.getException() != null) {
                                    signupActivity.userRegistrationFailed(task.getException().toString());
                                } else {
                                    signupActivity.userRegistrationFailed("createUserWithEmailAndPassword failed: for unknown reasons");
                                }

                            }
                        }
                    });
        }


        public void logInUser(String email, String password, LoginScreen loginScreen) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            App.getAppInstance().setCurrentUserName(user.getDisplayName());
                        }
                        loginScreen.showNextScreen();
                    } else {
                        // If sign in fails, display a message to the user.
                        loginScreen.userLoginFailed("Login failed for user: " + task.getException());
                    }
                }
            });
        }
    }



}