package com.example.quanlidiemrenluyen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlidiemrenluyen.Models.Chat;
import com.example.quanlidiemrenluyen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> mChats;
    public static final int CHAT_OF_LEFT = 0;
    public static final int CHAT_OF_RIGHT = 1;
    String imageURL;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> Chats, String imageURL) {

        this.mContext = context;
        this.mChats = Chats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==CHAT_OF_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);

            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);

            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = mChats.get(i);
        viewHolder.showMeseger.setText(chat.getMesenger());
        if(imageURL.equals("default")){
            viewHolder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageURL).into(viewHolder.profileImage);
        }
        if(i == mChats.size()-1){
            if(chat.isIsseen()){
                viewHolder.isSeen.setText("Đã xem");
            }else{
                viewHolder.isSeen.setText("Đã nhận");
            }
        }else{
            viewHolder.isSeen.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMeseger;
        public ImageView profileImage;
        public TextView isSeen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMeseger = itemView.findViewById(R.id.textviewmsg);
            profileImage = itemView.findViewById(R.id.imageviewitemchat);
            isSeen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(firebaseUser.getUid())){
            return CHAT_OF_RIGHT;
        }else
            return CHAT_OF_LEFT;
    }
}