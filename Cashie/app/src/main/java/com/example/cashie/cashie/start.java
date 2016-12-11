package com.example.cashie.cashie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cashie.cashie.Entity.Profile;
import com.example.cashie.cashie.dummy.DummyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class start extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AccountSettingsFragment.OnFragmentInteractionListener, FrontFragment.OnFragmentInteractionListener, MenFragment.OnListFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    private FragmentTransaction fragmentTransaction;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "StartActivity";
    private ProgressDialog mProgressDialog;
    private static final int PICK_IMAGE = 100;
    private Profile userProfile;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.navigationView = navigationView;
        navigationView.setNavigationItemSelectedListener(this);

        FrontFragment front = new FrontFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        removeFragmentArrayList();
        addCenterFragments(front);

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://cashie-e7c50.appspot.com/");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    if (userProfile == null) {
                        System.out.println("User Photo URL: " + user.getPhotoUrl());
                        if (user.getPhotoUrl() == null) {
                            userProfile = new Profile(null, user.getDisplayName(), user.getEmail(), false);
                        } else {
                            StorageReference usersRef = mStorageRef.child("users/" + user.getUid() + ".jpg");

                            final long ONE_MEGABYTE = 1024 * 1024;
                            usersRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Data for "users/uid.jpg" is returns, use this as needed
                                    userProfile = new Profile(bytes, user.getDisplayName(), user.getEmail(), false, true);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    }
                } else {
                    // User is signed out
                    userProfile = null;
                    Log.d(TAG, "OnAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
            // ...
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                // TODO handle home action
                FrontFragment front = new FrontFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                removeFragmentArrayList();
                addCenterFragments(front);
                break;
            case R.id.shop_men:
                // TODO handle men action
                MenFragment men = new MenFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                removeFragmentArrayList();
                addCenterFragments(men);
                break;
            case R.id.shop_women:
                // TODO handle women action
//                goToNextActivity = new Intent(getApplicationContext(), men.class);
//                startActivity(goToNextActivity);
                break;
            case R.id.shop_acc:
                // TODO handle accessories action
//                goToNextActivity = new Intent(getApplicationContext(), men.class);
//                startActivity(goToNextActivity);
                break;
            case R.id.login:
                LoginFragment login = new LoginFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                removeFragmentArrayList();
                addCenterFragments(login);
                break;
            case R.id.register:
                // TODO handle register action
                RegisterFragment register = new RegisterFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                removeFragmentArrayList();
                addCenterFragments(register);
                break;
            case R.id.account:
                // TODO handle account action
                AccountSettingsFragment accSettings =
                        new AccountSettingsFragment().newInstance(
                                userProfile.getName(),
                                userProfile.getEmail(),
                                userProfile.getProfilePhoto());
                fragmentTransaction = getSupportFragmentManager().beginTransaction();

                removeFragmentArrayList();
                addCenterFragments(accSettings);
                break;
            case R.id.logout:
                mAuth.signOut();
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.login));
                updateUI(null);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void addCenterFragments(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_start, fragment);
        fragmentArrayList.add(fragment);
        fragmentTransaction.commit();
    }

    private void removeFragmentArrayList() {
        if (fragmentArrayList.size() > 0) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment activeFragment: fragmentArrayList) {
                fragmentTransaction.remove(activeFragment);
            }
            fragmentArrayList.clear();
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    public void onSubmitPressed(View view) {
        EditText emailField = (EditText) findViewById(R.id.login_email_field);
        EditText passwordField = (EditText) findViewById(R.id.login_password_field);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        signIn(emailField, email, passwordField, password);
    }

    public void onRegisterSubmitPressed(View view) {
        // What happens when user tries to register
        EditText registerEmailField = (EditText) findViewById(R.id.register_email_field);
        String email = registerEmailField.getText().toString();
        EditText registerPasswordField = (EditText) findViewById(R.id.register_password_field);
        String password = registerPasswordField.getText().toString();
        EditText registerConfirmField = (EditText) findViewById(R.id.confirm_password_field);
        String confirm = registerConfirmField.getText().toString();

        createAccount(registerEmailField, email, registerPasswordField, password, registerConfirmField, confirm);
    }

    private void signInAnonymously() {
        showProgressDialog(getString(R.string.loading));

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(start.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                        hideProgressDialog();
                    }
                });
    }

    private void createAccount(EditText emailField, final String email, EditText passwordField, String password, EditText confirmField, String confirm) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(emailField, email, passwordField, password, confirmField, confirm)) {
            return;
        }

        showProgressDialog(getString(R.string.registering));

        // [START create user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Toast.makeText(start.this, "Unable to create this account!", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users");

                        myRef.child(mAuth.getCurrentUser().getUid()).setValue(email);
                        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));
                    }

                    hideProgressDialog();
                }
            });
        // [END create user_with_email]
    }

    private void signIn(EditText emailField, String email, EditText passwordField, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(emailField, email, passwordField, password)) {
            return;
        }

        showProgressDialog(getString(R.string.signin));

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(start.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm(EditText emailField, String email, EditText passwordField, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private boolean validateForm(EditText emailField, String email, EditText passwordField, String password, EditText confirmField, String confirm) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        if (TextUtils.isEmpty(confirm)) {
            confirmField.setError("Required.");
            valid = false;
        } else {
            confirmField.setError(null);
        }

        if (!TextUtils.equals(password, confirm)) {
            confirmField.setError("Does not match password!");
            valid = false;
        } else {
            confirmField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        Menu navMenu = navigationView.getMenu();
        if (user != null) {

            navMenu.findItem(R.id.account).setVisible(true);
            navMenu.findItem(R.id.logout).setVisible(true);
            navMenu.findItem(R.id.login).setVisible(false);
            navMenu.findItem(R.id.register).setVisible(false);
        } else {
            navMenu.findItem(R.id.logout).setVisible(false);
            navMenu.findItem(R.id.account).setVisible(false);
            navMenu.findItem(R.id.login).setVisible(true);
            navMenu.findItem(R.id.register).setVisible(true);
        }
    }

    public void showProgressDialog(String string) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(string);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void uploadPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload."), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // Display an error
                return;
            }
            // Load image into profile photo temporarily
            Uri uri = data.getData();
            try {
                InputStream stream = this.getContentResolver().openInputStream(data.getData());
                        //new FileInputStream(new File(uri.getPath()));
                userProfile.setProfilePhoto(stream);
                userProfile.setChanged(true);
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.account));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void onUpdateProfilePressed(View view) {
        EditText nameField = (EditText) findViewById(R.id.profile_name_field);
        String name = "";
        Uri userPhotoUrl = null;
        if (nameField.getText() != null) {
            name = nameField.getText().toString();
        }

        userProfile.setName(name);

        if (userProfile.isChanged()) {
            // Update to database
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference usersRef = mStorageRef.child("users");

            if (userProfile.getProfilePhoto() != null) {
                UploadTask uploadTask = usersRef.child(user.getUid() + ".jpg").putBytes(userProfile.getProfilePhoto());
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        System.out.println("Failed to upload");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        EditText successNameField = (EditText) findViewById(R.id.profile_name_field);
                        String successName = "";
                        if (successNameField.getText() != null) {
                            successName = successNameField.getText().toString();
                        }
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(successName)
                                .setPhotoUri(downloadUrl)
                                .build();
                        userProfile.setChanged(false);
                        showProgressDialog("Updating...");

                        user.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User Profile Updated");
                                            Toast.makeText(start.this, "Profile has been updated.", Toast.LENGTH_SHORT).show();
                                            hideProgressDialog();
                                        } else {
                                            Toast.makeText(start.this, "Update failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            } else {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                userProfile.setChanged(false);
                showProgressDialog("Updating...");

                user.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User Profile Updated");
                                    Toast.makeText(start.this, "Profile has been updated.", Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                } else {
                                    Toast.makeText(start.this, "Update failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}