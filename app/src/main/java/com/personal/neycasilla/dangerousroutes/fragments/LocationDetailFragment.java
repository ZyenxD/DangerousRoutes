package com.personal.neycasilla.dangerousroutes.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.personal.neycasilla.dangerousroutes.R;
import com.personal.neycasilla.dangerousroutes.adapters.ZoneAdapter;
import com.personal.neycasilla.dangerousroutes.model.Comments;
import com.personal.neycasilla.dangerousroutes.model.DangerZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationDetailFragment extends Fragment {

    private dangerZoneData dangerZoneData;
    private ImageView imageView;
    private TextView textViewProvincia;
    private TextView textViewSector;
    private RecyclerView recyclerView;
    private EditText editText;
    private ZoneAdapter zoneAdapter;
    private Button commentButton;
    private Toolbar toolbar;
    private List<Comments> comments;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        comments = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_location_detail, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        imageView = view.findViewById(R.id.image_zone);
        textViewProvincia = view.findViewById(R.id.textview_provincia);
        textViewSector = view.findViewById(R.id.textview_sector);
        editText = view.findViewById(R.id.add_a_comment);
        recyclerView = view.findViewById(R.id.comments_recycle_view);
        commentButton = view.findViewById(R.id.comments_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        loadDanger();
        zoneAdapter = new ZoneAdapter(getActivity(), comments);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(editText.getText().length()<=0)){
                    Comments comment = new Comments();
                    comment.setCommentText(editText.getText().toString());
                    comment.setDateMade(Calendar.getInstance().getTime());
                    comment.setZoneKey(dangerZoneData.getZone().getKey());
                    comment.setKey(reference.push().getKey());
                    reference.child(comment.getKey()).setValue(comment);
                    editText.setText("");
                }

            }
        });
        setEveryAspect();
        recyclerView.setAdapter(zoneAdapter);
        return view;
    }

    public void loadDanger() {
        reference.limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comments comments = dataSnapshot.getValue(Comments.class);
                if(comments.getZoneKey().equals(dangerZoneData.getZone().getKey())){
                    zoneAdapter.addItem(comments);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEveryAspect() {
        DangerZone dangerZone = dangerZoneData.getZone();
        textViewProvincia.setText(dangerZone.getProvincia());
        textViewSector.setText(dangerZone.getSector());
        toolbar.setTitle(dangerZone.getDangerName());
        imageView.setImageBitmap(null);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("DangerImages").child(dangerZone.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).into(imageView);
            }
        });
    }

    public interface dangerZoneData {
        DangerZone getZone();
    }


    public void setDangerZoneData(LocationDetailFragment.dangerZoneData dangerZoneData) {
        this.dangerZoneData = dangerZoneData;
    }

}
