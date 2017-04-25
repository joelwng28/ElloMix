package com.ellomix.android.ellomix.FirebaseAPI;

import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.Track;
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
    private static final String CHAT_GROUP_PLAYLIST = "groupPlaylist";
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

    public static DatabaseReference getChatsQuery(){
        return mDatabase.child(CHATS);
    }

    public static DatabaseReference getUsersQuery() {
        return mDatabase.child(USERS);
    }

    public static DatabaseReference getChatPlaylistQuery(String chatId) {
        return mDatabase.child(CHATS).child(chatId).child(CHAT_GROUP_PLAYLIST);
    }

    public static DatabaseReference getMainUserFollowingQuery() {
        return getUsersQuery().child(getFirebaseUser().getUid()).child(USER_FRIEND);
    }

    public static DatabaseReference getUserQuery(String userId) {
        return mDatabase.child(USERS).child(userId);
    }

    // Assume user has already set the user id
    public static void pushNewUser(User user) {
        if (user.getId() == null) {
            return;
        }

        mDatabase.child(USERS).child(user.getId()).setValue(user);
    }

    public static void addNewFriend(String userId, User friend) {
        //TODO: Add to current user +1 to his following count and +1 to the "friend"'s follower count
        mDatabase.child(USERS).child(userId).child(USER_FRIEND).child(friend.getId()).setValue(friend.getId());
    }

    // Assume chat id not already set
    public static String pushNewChat(Chat chat) {

        String key = mDatabase.child(CHATS).push().getKey();
        chat.setId(key);
        mDatabase.child(CHATS).child(key).setValue(chat);
        return key;
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

    public static DatabaseReference getChatIds(String userId) {
        return mDatabase.child(USERS).child(userId).child(USER_CHAT);
    }

    // Add music to group playlist
    public static void addMusic(String chatId, Track track) {
        getChatsQuery().child(chatId).child(CHAT_GROUP_PLAYLIST).push().setValue(track);
    }

}
