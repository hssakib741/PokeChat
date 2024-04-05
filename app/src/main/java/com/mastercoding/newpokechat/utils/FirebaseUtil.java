package com.mastercoding.newpokechat.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {


    public  static  String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedin(){
        if (currentUserId()!=null) {
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference alluserCollectionreference(){
        return FirebaseFirestore.getInstance().collection("users");

    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference  getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomid(String userid1, String userid2){
        if (userid1.hashCode()<userid2.hashCode()){
            return userid1+"_"+userid2;
        }else {
            return userid2+"_"+userid1;
        }

    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String>userIds){
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())){
         return alluserCollectionreference().document(userIds.get(1));
        }else {
            return alluserCollectionreference().document(userIds.get(0));
        }
    }

    public static String timestamptoString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentprofilepic(){
        return FirebaseStorage.getInstance().getReference().child("profilepic").child(FirebaseUtil.currentUserId());
    }
    public static StorageReference getOtherUserprofilepic(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profilepic").child(otherUserId);
    }

}
