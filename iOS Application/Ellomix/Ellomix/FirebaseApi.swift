//
//  FirebaseApi.swift
//  Ellomix
//
//  Created by Akshay Vyas on 2/17/17.
//  Copyright © 2017 Akshay Vyas. All rights reserved.
//

import Foundation
import FirebaseDatabase
import FirebaseAuth

class FirebaseApi {
    private static let CHATS = "Chats"
    private static let USERS = "Users"
    private static let USER_CHAT = "chatIds"
    private static let USER_FRIEND = "followingIds"
    private static let CHAT_GROUP_PLAYLIST = "groupPlaylist"
    
    private static let mDatabase: FIRDatabaseReference = FIRDatabase.database().reference()
    
    //private static let mAuth : FIRAuth = FIRAuth
    
    static func getDatabaseRef() -> FIRDatabaseReference {
        return mDatabase;
    }
    
    static func getChatQuery() -> FIRDatabaseReference {
        return mDatabase.child(CHATS)
    }
    
    static func getUsersQuery() -> FIRDatabaseReference {
        return mDatabase.child(USERS)
    }
    
    static func getChatPlaylistQuery(chatId:String) -> FIRDatabaseReference {
        return mDatabase.child(CHATS).child(chatId).child(CHAT_GROUP_PLAYLIST)
    }
    
    static func getMainUserFollowingQuery() -> FIRDatabaseReference {
        return getUsersQuery().child(getFirebaseUser().getUid()).child(USER_FRIEND)
    }
    
    static func getUserQuery(userId:String) -> FIRDatabaseReference {
        return mDatabase.child(USERS).child(userId)
    }
    
    //Finish push new user
    static func pushNewUser() -> Void {
        
    }
    
    //TODO: Finish add new friends
//    static func addNewFriend(userId:String, User friend) {
//        //TODO: Add to current user +1 to his following count and +1 to the "friend"'s follower count
//        mDatabase.child(USERS).child(userId).child(USER_FRIEND).child(friend.getId()).setValue(friend.getId());
//    }
    
    //Finish implementing
//    // Assume chat id not already set
//    public static String pushNewChat(Chat chat) {
//    
//    String key = mDatabase.child(CHATS).push().getKey();
//    chat.setId(key);
//    mDatabase.child(CHATS).child(key).setValue(chat);
//    return key;
//    }
//    
//    // just need the User id present
//    public static void addChatIdToUser(User user, Chat chat) {
//    if (user.getId() == null) {
//    return;
//    }
//    mDatabase.child(USERS).child(user.getId()).child(USER_CHAT).child(chat.getId()).setValue(chat.getId());
//    }
//    
//    // just need the User id present
//    public static void addChatIdToUser(String userId, Chat chat) {
//    
//    mDatabase.child(USERS).child(userId).child(USER_CHAT).child(chat.getId()).setValue(chat.getId());
//    }
//    
//    // Add music to group playlist
//    public static void addMusic(String chatId, Track track) {
//    getChatsQuery().child(chatId).child(CHAT_GROUP_PLAYLIST).push().setValue(track);
//    }
    
}
