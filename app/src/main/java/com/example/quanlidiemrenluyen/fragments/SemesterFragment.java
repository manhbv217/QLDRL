package com.example.quanlidiemrenluyen.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quanlidiemrenluyen.Adapters.HocKyAdapter;
import com.example.quanlidiemrenluyen.Models.DiemRenLuyen;
import com.example.quanlidiemrenluyen.Models.HocKy;
import com.example.quanlidiemrenluyen.Models.HocKyView;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SemesterFragment extends Fragment {

    RecyclerView recyclerView;
    List <HocKyView> hocKyViewList;
    HocKyAdapter hocKyAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference ref;
    List<HocKy> hocKyList = new ArrayList<>();
    List <HocKyView> listest;



    public SemesterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle("Học Kỳ");

        View view = inflater.inflate(R.layout.fragment_semester, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_hocky);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hocKyViewList = new ArrayList<>();
        listest = new ArrayList<>();
        listest.add(new HocKyView("hk1","đã nộp","1"));
        listest.add(new HocKyView("hk2","chua nop","2"));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("DiemRenLuyen");
        reference = FirebaseDatabase.getInstance().getReference("HocKy");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hocKyViewList.clear();
                hocKyList.clear();
                    Log.v("datavvvvv",dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    HocKy hocKy = data.getValue(HocKy.class);

                    hocKyList.add(hocKy);
                    hocKyViewList.add(new HocKyView(hocKy.getName(),"",hocKy.getId()));

                }
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            DiemRenLuyen diemRenLuyen = data.getValue(DiemRenLuyen.class);
                            for(HocKyView hocKyView: hocKyViewList){
                                if(hocKyView.getId().equals(diemRenLuyen.getHockyid()) && diemRenLuyen.getStudenid().equals(firebaseUser.getUid())){
                                    hocKyView.setNote(diemRenLuyen.getStatus());
                                }
                            }
                        }
                        Log.v("hockylist",hocKyViewList.size()+"");
                        hocKyAdapter = new HocKyAdapter(getContext(),hocKyViewList);
                        recyclerView.setAdapter(hocKyAdapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for (int i=0; i< hocKyViewList.size(); i++){
            HocKyView hocKyView = hocKyViewList.get(i);
            Log.v("aaaaaaaaaaaaaaa",hocKyView.getHocky()+" "+hocKyView.getId());
        }

        reference = FirebaseDatabase.getInstance().getReference("DiemRenLuyen");






    return  view;
    }

}
