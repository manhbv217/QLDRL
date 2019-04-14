package com.example.quanlidiemrenluyen.activities;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlidiemrenluyen.Models.Chat;
import com.example.quanlidiemrenluyen.Models.CoVan;
import com.example.quanlidiemrenluyen.Models.DiemRenLuyen;
import com.example.quanlidiemrenluyen.Models.HocKy;
import com.example.quanlidiemrenluyen.Models.Point;
import com.example.quanlidiemrenluyen.Models.User;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.fragments.ChatFragment;
import com.example.quanlidiemrenluyen.fragments.ProfileFragment;
import com.example.quanlidiemrenluyen.fragments.SemesterFragment;
import com.example.quanlidiemrenluyen.fragments.UserFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    CircleImageView circleImageMain;
    TextView userNameMain;
    View headView;

    MenuItem chatItem;
    Menu menu;

    @Override
    protected void onStart() {

        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else{
            status("online");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        headView = (View) navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        chatItem = menu.getItem(1);
        circleImageMain = headView.findViewById(R.id.imageViewMain);
        userNameMain = headView.findViewById(R.id.userNameMain);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    String tmp = user.getUserName();
                    if (!tmp.equals("")) {
                        userNameMain.setText(tmp);
                    }
                    if (user.getImageURL().equals("default")) {
                        circleImageMain.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(circleImageMain);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        if(firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int unRead = 0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Chat chat = data.getValue(Chat.class);
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                            unRead++;
                        }
                    }
                    if (unRead != 0) {
                        chatItem.setTitle("Chats" + "(" + unRead + ")");
                    } else {
                        chatItem.setTitle("Chats");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

//        reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("Covan").push().setValue(new CoVan("id","classid","covanid"));
//        reference.child("Covan").push().setValue(new CoVan("id","classid","covanid"));
//        reference.child("Covan").push().setValue(new CoVan("id","classid","covanid"));
//
//        reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("HocKy").push().setValue(new HocKy("1","hk1"));
//        reference.child("HocKy").push().setValue(new HocKy("1","hk1"));
//        reference.child("HocKy").push().setValue(new HocKy("1","hk1"));
//        reference.child("HocKy").push().setValue(new HocKy("1","hk1"));
//        reference.child("HocKy").push().setValue(new HocKy("1","hk1"));


        navigationView.setNavigationItemSelectedListener(this);

    }


    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap <String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firebaseUser != null) {
            status("offline");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseUser!= null) {
            status("offline");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_semester) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain,new SemesterFragment());
            ft.commit();
        } else if (id == R.id.nav_chat) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain,new ChatFragment());
            ft.commit();
        } else if (id == R.id.nav_logout) {
            status("offline");
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;

        }
        else if(id==R.id.nav_users){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain,new UserFragment());
            ft.commit();
        }else if(id == R.id.nav_profile){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain,new ProfileFragment());
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
