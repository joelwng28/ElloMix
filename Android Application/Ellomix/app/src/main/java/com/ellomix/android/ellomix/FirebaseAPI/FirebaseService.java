package com.ellomix.android.ellomix.FirebaseAPI;

import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by ATH-AJT2437 on 1/21/2017.
 */

public class FirebaseService {

    private static final String CHATS = "Chats";
    private static final String USERS = "Users";
    private static final String USER_CHAT = "chatIds";
    private static final String USER_FRIEND = "followingIds";
    private Query chatsQuery;
    private Query usersQuery;

    private static final DatabaseReference mDatabase =
            FirebaseDatabase.getInstance().getReference();
    private static final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    public static DatabaseReference getFirebaseDatabase() {
        return mDatabase;
    }

    public static FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public static FirebaseUser getFirebaseUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public static Query getChatsQuery(){
        return mDatabase.child(CHATS);
    }

    public static Query getUsersQuery() {
        return mDatabase.child(USERS);
    }

    public static void addNewFriend(String userId, User friend) {
        mDatabase.child(USERS).child(userId).child(USER_FRIEND).child(friend.getId()).setValue(friend.getId());
    }

    // Assume user has already set the user id
    public static void pushNewUser(User user) {
        if (user.getId() == null) {
            return;
        }

        mDatabase.child(USERS).child(user.getId()).setValue(user);
    }

    // just need the User id present
    public static void addChatIdToUser(User user, Chat chat) {
        if (user.getId() == null) {
            return;
        }
        mDatabase.child(USERS).child(user.getId()).child(USER_CHAT).child(chat.getId()).setValue(chat.getId());
    }

    // just need the User id present
    public static void addChatIdToUser(String userId, Chat chat) {

        mDatabase.child(USERS).child(userId).child(USER_CHAT).child(chat.getId()).setValue(chat.getId());
    }

    // Assume chat id not already set
    public static String pushNewChat(Chat chat) {

        String key = mDatabase.child(CHATS).push().getKey();
        chat.setId(key);
        mDatabase.child(CHATS).child(key).setValue(chat);
        return key;
    }

}