package com.example.quanlidiemrenluyen.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlidiemrenluyen.Adapters.MessageAdapter;
import com.example.quanlidiemrenluyen.Models.Chat;
import com.example.quanlidiemrenluyen.Models.User;
import com.example.quanlidiemrenluyen.Notification.Client;
import com.example.quanlidiemrenluyen.Notification.Data;
import com.example.quanlidiemrenluyen.Notification.MyResponse;
import com.example.quanlidiemrenluyen.Notification.Sender;
import com.example.quanlidiemrenluyen.Notification.Token;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.fragments.APIService;
import com.example.quanlidiemrenluyen.fragments.ChatFragment;
import com.example.quanlidiemrenluyen.fragments.SemesterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profileImage;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageButton imageButtonSend;
    EditText editTextSend;
    RecyclerView recyclerView;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    ValueEventListener seenListener;
    String userID;
    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesenger);

        Toolbar toolbar = findViewById(R.id.toolbarMessenger);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        profileImage = findViewById(R.id.circleImageMessenger);
        userName = findViewById(R.id.userNameMessenger);
        imageButtonSend = findViewById(R.id.btnSend);
        editTextSend = findViewById(R.id.textSend);

        recyclerView = findViewById(R.id.recyclerViewChats);
        recyclerView.setHasFixedSize(true);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        intent = getIntent();
        userID = intent.getStringExtra("userid");

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = editTextSend.getText().toString();
                sendMesenge(firebaseUser.getUid(), userID, msg);

//                if(!isVisible()){
//                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-2);
//                }
                editTextSend.setText("");
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                if (user.getImageURL().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profileImage);
                }
                readMeseger(firebaseUser.getUid(), userID, user.getImageURL());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userID);

    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        data.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMesenge(String sender, final String receiver, String mesenge) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("mesenger", mesenge);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userID);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userID)
                .child(firebaseUser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef1.child("id").setValue(firebaseUser.getUid());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = mesenge;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(notify) {
                    sendNotification(receiver, user.getUserName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendNotification(String receiver, final String name, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot data: dataSnapshot.getChildren()) {
                   Token token = data.getValue(Token.class);
                   Data dt = new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,message,name,userID);
                   Sender sender = new Sender(dt,token.getToken());
                   apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                       @Override
                       public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                           if(response.code()==200){
                               if(response.body().success !=1 ){
                                   Toast.makeText(MessageActivity.this, "failed", Toast.LENGTH_SHORT).show();

                               }
                           }
                       }

                       @Override
                       public void onFailure(Call<MyResponse> call, Throwable t) {

                       }
                   });
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMeseger(final String id, final String userID, final String imageURL) {
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if ((chat.getReceiver().equals(userID) && chat.getSender().equals(id))
                            || (chat.getSender().equals(userID) && chat.getReceiver().equals(id))
                    ) {
                        chats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chats, imageURL);
                    recyclerView.setAdapter(messageAdapter);
                    messageAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private  void currentUser(String id){
        SharedPreferences.Editor editor = getSharedPreferences("PREPS",MODE_PRIVATE).edit();
        editor.putString("currentuser",id);
        editor.apply();
    }
    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
    public boolean isVisible() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int posititionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
        return (posititionOfLastVisibleItem >= itemCount);
    }

}
