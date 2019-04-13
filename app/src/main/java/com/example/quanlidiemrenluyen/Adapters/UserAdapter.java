package com.example.quanlidiemrenluyen.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlidiemrenluyen.Models.Chat;
import com.example.quanlidiemrenluyen.Models.User;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.activities.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUser;
    private boolean isChat;

    String lastMesage;


    public UserAdapter(Context context, List<User> mUsers, boolean isChat) {
        this.mContext = context;
        this.mUser = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUser.get(i);

        viewHolder.username.setText(user.getUserName());

        if (user.getImageURL().equals("default")) {
            viewHolder.profileImage.setImageResource(R.mipmap.ic_launcher);

        } else {
            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profileImage);
        }
        if(isChat){
            lastMessage(user.getId(),viewHolder.lastMsg);
        }
        else {
            viewHolder.lastMsg.setVisibility(View.GONE);
        }
        if (isChat) {
            if (user.getStatus().equals("online")) {
                viewHolder.imgOn.setVisibility(View.VISIBLE);
                viewHolder.imgOff.setVisibility(View.GONE);
            } else {
                viewHolder.imgOff.setVisibility(View.VISIBLE);
                viewHolder.imgOn.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgOff.setVisibility(View.GONE);
            viewHolder.imgOn.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        private ImageView imgOn;
        private ImageView imgOff;
        private TextView lastMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_image);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);
            lastMsg = itemView.findViewById(R.id.last_msg);


        }
    }

    private void lastMessage(final String userid, final TextView lastMsg) {
        lastMesage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    if (chat.getSender().equals(userid) && chat.getReceiver().equals(firebaseUser.getUid()) ||
                            chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid)
                    ) {
                        lastMesage = chat.getMesenger();
                    }
                }
                switch (lastMesage){
                    case "default":lastMsg.setText("no msg");
                    break;
                    default:lastMsg.setText(lastMesage);
                    break;
                }
                lastMesage="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
