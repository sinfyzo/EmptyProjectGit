package com.emptyproject.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.emptyproject.webapi.WebUtil;

import java.net.Socket;
import java.util.List;


public class AppController extends Application implements WebUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static AppController mInstance;
    private static Socket mSocket;
    private static Activity mactivity;
//    public SocketListioner socketListioner;
//    public static DBController objDBController;

    public Socket getSocket() {
        return mSocket;
    }

    public static int exclusive_other = 0, status_accept = 0, status_photo_instantselfie = 0, read_status = 0;
//    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        mInstance = this;
        sharedPreferences = initSharedPrefs();
        editor = sharedPreferences.edit();
//        objDBController = new DBController(this);
       /* try {
            mSocket = IO.socket(Const.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }*/
//        if (!isApplicationSentToBackground(getApplicationContext())) {
//        connectSocket();
//        }

    }

   /* public DBController getDatabase() {
        return objDBController;
    }*/

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        /*if (isApplicationSentToBackground(getApplicationContext())){
            Log.e("onDisconnect..." + getClass().getName(), "" + "onTrimMemory...IF");
        }else{
            Log.e("onDisconnect..." + getClass().getName(), "" + "onTrimMemory...ELSE");
        }*/
        if (!PrefsManager.getIsChatScreenVisible()) {
            Log.e("onDisconnect..." + getClass().getName(), "" + "onTrimMemory");
//            disconnectSocket();
        }

    }

    public static synchronized AppController getInstance(Activity mactivityPara) {
        mactivity = mactivityPara;
        return mInstance;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    // TODO Check Application in background or not.
    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }

        }
        return false;
    }

    public static boolean isChatActivityVisible; // Variable that will check the
    // current activity state

    public static boolean isActivityVisible() {
        return isChatActivityVisible; // return true or false
    }

    public static void activityResumed() {
        isChatActivityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        isChatActivityVisible = false;// this will set false when activity paused

    }

    // TODO  Start... Get SharedPreferences whenever any class needs
    public SharedPreferences initSharedPrefs() {
        sharedPreferences = getSharedPreferences(Const.SHARED_PREF_FILE, MODE_PRIVATE);
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getSharedPrefsEditor() {
        return editor;
    }

    public static SharedPreferences getSharedPrefs() {
        return sharedPreferences;
    }
    // TODO  End... Get SharedPreferences whenever any class needs


    // TODO Display dummy Profile pics
    /*public Drawable getDummyThumbImageProfile() {
        Drawable drawable = null;
        try {
            JSONObject object = new JSONObject(PrefsManager.getUserJson());
            if (object.has("gender")) {
                if (object.getString("gender").equals("m")) {
                    drawable = getResources().getDrawable(R.drawable.no_photo_boy);
                } else {
                    drawable = getResources().getDrawable(R.drawable.no_photo_girl);
                }
            } else {
                drawable = getResources().getDrawable(R.drawable.no_photo_boy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }*/

    // TODO Display dummy Search Profile pics
   /* public Drawable getDummyThumbImageSearch() {
        Drawable drawable = null;
        try {
//            JSONObject object = new JSONObject(PrefsManager.getUserJson());
            if (PrefsManager.getGender().equalsIgnoreCase("m")) {
                drawable = getResources().getDrawable(R.drawable.no_photo_girl);
            } else {
                drawable = getResources().getDrawable(R.drawable.no_photo_boy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }*/


    /*public void connectSocket() {

        try {
            if (null == mSocket) {
                mSocket = IO.socket(BuildConfig.CHAT_SERVER_URL);
            }
            if (null != mSocket && !PrefsManager.getUid().equalsIgnoreCase("0")) {

                mSocket.on(Socket.EVENT_CONNECT, onConnect);
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.on(Socket.EVENT_ERROR, onError);
                mSocket.on(KEY_CHAT_STARTTYPING, startTyping);
                mSocket.on(KEY_CHAT_STOPTYPING, stopTyping);
                mSocket.on(KEY_CHAT_NEWMSG, onNewMessage);
                mSocket.on(KEY_BLOCK, onUserBlock);
                mSocket.on(KEY_UNFRIEND, onUserUnfriend);
                mSocket.on(KEY_CHAT_EVENT_EXCLUSIVECHAT, onExclusiveChat);
                mSocket.on(KEY_CHAT_EVENT_ONLINE, onUserOnline);
                mSocket.on(KEY_CHAT_EVENT_OFFLINE, onUserOffline);
                mSocket.on(KEY_CHAT_READMSG, onReadMessage);
                mSocket.on(KEY_CHAT_DELIVERMSG, onDeliverMessage);
//                mSocket.on(KEY_CHAT_USERJOINED, onUserJoined);
//                mSocket.on(KEY_CHAT_USERLEFT, onUserLeft);

                mSocket.connect();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onConnect..." + getClass().getName(), "" + args);
//            if (!isApplicationSentToBackground(getApplicationContext())) {
            setUserIntro();
//            }

            if (socketListioner != null) {
                socketListioner.onConnect(args);
            }
        }
    };


    public void reConnect() {
        if (getSocket() != null) {
            if (!getSocket().connected()) {
//                Log.e("reConnect..." + mactivity.getLocalClassName(), "reConnect..IF");
                connectSocket();
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onDisconnect..." + getClass().getName(), "" + args);
            if (socketListioner != null) {
                socketListioner.onDisconnect(args);
            }
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onConnectError..." + getClass().getName(), "" + args);
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onError..." + getClass().getName(), "" + args);
        }
    };
    public Emitter.Listener onUserOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserOnline..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onUserOnline(args);
            }

        }
    };
    private Emitter.Listener onUserOffline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserOffline..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onUserOffline(args);
            }
        }
    };
    //TODO: For UnFriend Listner
    //For UnFriend,ReFriend
    private Emitter.Listener onUserUnfriend = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserUnfriend..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onUserUnfriend(args);
            }
        }
    };//TODO: For Block Listner
    private Emitter.Listener onUserBlock = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserBlock..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onUserBlock(args);
            }
        }
    };

    private Emitter.Listener onExclusiveChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onExclusiveChat..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onExclusiveChat(args);
            }
        }
    };

    private Emitter.Listener onReadMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onReadMessage..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onReadMessage(args);
            }
        }
    };
    private Emitter.Listener onDeliverMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

//            Log.e("onDeliverMessage..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onDeliverMessage(args);
            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onNewMessage..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.onNewMessage(args);
            } else {
                setNewChatMessage(args[0].toString(), getApplicationContext(), null);
            }
        }
    };
    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserJoined..." + getClass().getName(), "" + args[0]);
//            socketListioner.onUserOnline(args);
        }
    };
    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("onUserLeft..." + getClass().getName(), "" + args[0]);
//            socketListioner.onUUse(args);
        }
    };
    private Emitter.Listener startTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Log.e("startTyping..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.startTyping(args);
            }

        }
    };
    private Emitter.Listener stopTyping = new Emitter.Listener() {//Here I get "to" userId from JSON by key "uid" because server send "to" in JSON by key "uid"
        @Override
        public void call(final Object... args) {
//            Log.e("stopTyping..." + getClass().getName(), "" + args[0]);
            if (socketListioner != null) {
                socketListioner.stopTyping(args);
            }
        }
    };

    public void setUserIntro() {
        JSONObject userInfo = chatCommonRequest(false, "");
        mSocket.emit("userIntro", userInfo, new Ack() {
            @Override
            public void call(Object... args) {
//                Log.e("userIntro..." + getClass().getName(), "" + args[0]);
            }
        });
    }

    public static void setDeliverEvent(String userid, String timestamp) {
        try {
            JSONObject newMsg;
            newMsg = chatCommonRequest(true, userid);
            newMsg.put(KEY_CHAT_TIMESTAMP, timestamp);
//            Log.e("setDeliverEvent...", "" + newMsg.toString());
            mSocket.emit(KEY_CHAT_DELIVERMSG, newMsg);


        } catch (Exception e) {
        }
    }

    public static JSONObject chatCommonRequest(boolean isOtherUser, String mChatUserID) {
        JSONObject data = new JSONObject();
        try {
            if (isOtherUser) {
                data.put(KEY_CHAT_TO, mChatUserID);
            }
            data.put(KEY_UID, PrefsManager.getUid());
            data.put(KEY_JWTOKEN, PrefsManager.getAccessTokenLogin());
            data.put(KEY_CHAT_APIVERSION, Const.APIVERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void disconnectSocket() {
        if (null != mSocket && !PrefsManager.getUid().equalsIgnoreCase("0")) {
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(Socket.EVENT_ERROR, onError);
            mSocket.off(KEY_CHAT_READMSG, onReadMessage);
            mSocket.off(KEY_CHAT_NEWMSG, onNewMessage);
            mSocket.off(KEY_CHAT_DELIVERMSG, onDeliverMessage);
            mSocket.off(KEY_CHAT_USERJOINED, onUserJoined);
            mSocket.off(KEY_CHAT_USERLEFT, onUserLeft);
            mSocket.off(KEY_CHAT_STARTTYPING, startTyping);
            mSocket.off(KEY_CHAT_STOPTYPING, stopTyping);
            mSocket.off(KEY_CHAT_EVENT_EXCLUSIVECHAT, onExclusiveChat);
            mSocket.off(KEY_CHAT_EVENT_ONLINE, onUserOnline);
            mSocket.off(KEY_CHAT_EVENT_OFFLINE, onUserOffline);
            mSocket.off(KEY_UNFRIEND, onUserUnfriend);
            mSocket.disconnect();
            mSocket.close();
        }
    }

    public void setsocketListioner(SocketListioner socketListionerPara) {
        socketListioner = socketListionerPara;
    }

    public static void saveChatMessageData(String name, String userid, String categoryName, String messageTypeValue, int type, String localPath, String url, String filesize, String ts, String gender) {


        Message objMessage = new Message();
        objMessage.setCategoryName(categoryName);
        objMessage.setmUsername(name);
        objMessage.setmUserId(userid);
        objMessage.setmUserIdTo(userid);
        objMessage.setMessageTypeValue(messageTypeValue);
        objMessage.setmMessage(messageTypeValue);
        objMessage.setmType(type);
        objMessage.setLocalPath(localPath);
        objMessage.setmUrl(url);
        objMessage.setmFileSize(filesize);
        objMessage.setmTs(ts);
        objMessage.setIsExclusive_Login_User(0);
        objMessage.setIsExclusive_Other_User(exclusive_other);
        objMessage.setIsStatus(status_accept);
        objMessage.setmGender(gender);
        objMessage.setIsReadMsgStatus(read_status);
        objMessage.setIsInstantSelfiePhotoStatus(status_photo_instantselfie);
        objMessage.setIsLoginUser(0);

        objDBController.saveChatMessage(objMessage);
        objDBController.updateChatUserChatTime(ts, userid);
    }

    public static void setNewChatMessage(String notificationData, Context context, User userPara) {
        user = userPara;
//        Log.e("...notificationData", "" + notificationData);
        String categoryname = "", genderto = "", message = "", userId, ts = "", giftname = "", giftcategoryName = "", msgType, url = "", fileSize = "", imageURL = "NO";
        JSONObject msg = null, file;
        if (PrefsManager.getGender().equalsIgnoreCase("m")) {
            genderto = "m";
        } else {
            genderto = "f";
        }
        try {
            JSONObject data = new JSONObject(notificationData);
            if (data.has(KEY_CHAT_TIMESTAMP)) {
                ts = data.getString(KEY_CHAT_TIMESTAMP);
            } else {
                ts = Util.getInstance(mactivity).getCurrentUnixTime();
            }
            userId = data.getString(KEY_UID);
            msgType = data.getString(KEY_CHAT_MSGTYPE);
            String username = objDBController.getChatUserName(userId);

            read_status = 2;
            setDeliverEvent(userId, ts);
            objDBController.updateReadFlag(read_status, userId, 2, 0);

            if (msgType.equalsIgnoreCase(KEY_INSTANTSELFIE_STATUS) || msgType.equalsIgnoreCase(KEY_ONLINEDATE_STATUS)) {
                message = data.getJSONObject(KEY_CHAT_MSG).getString(KEY_DATA);

                saveChatMessageData(username, userId, categoryname, message, Const.TYPE_STATUS, "NO", "NO", "0", ts, genderto);
            } else {
                exclusive_other = data.getInt(KEY_ISEXCLUSIVE);
                if (data.has(KEY_STATUS)) {
                    status_accept = data.getInt(KEY_STATUS);
                } else {
                    status_accept = 0;
                }
                if (data.has(KEY_WITHPHOTO)) {
                    status_photo_instantselfie = data.getInt(KEY_WITHPHOTO);
                } else {
                    status_photo_instantselfie = 0;
                }
                *//*if (data.has(KEY_CHAT_READFLAG)) {
                    read_status = data.getInt(KEY_CHAT_READFLAG);
                } else {
                    read_status = 0;
                }*//*

                if (msgType.equals(KEY_STATUS) || msgType.equals(KEY_CHAT_LIKE)) {
                    message = data.getString(KEY_CHAT_MSG);
                } else {
                    msg = data.getJSONObject(KEY_CHAT_MSG);
                    if (msgType.equals(KEY_CHAT_GIFT)) {
                        message = msg.getString(KEY_CHAT_GIFT);
                    } else {
                        if (msg.has(KEY_DATA)) {
                            message = msg.getString(KEY_DATA);
                        }
                    }
                }
                if (msgType.equals(KEY_CHAT_IMAGE) || msgType.equals(KEY_CHAT_AUDIO) || msgType.equals(KEY_CHAT_VIDEO) || msgType.equals(KEY_CHAT_INSTANTSELFIE)) {
                    file = msg.getJSONObject(KEY_CHAT_FILE);
                    url = file.getString(KEY_CHAT_URL);
                    fileSize = file.getString(KEY_CHAT_SIZE);

                }
                if (msgType.equals(KEY_CHAT_MSG)) {
                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_MESSAGE, "", "", "", ts, genderto);
                } else if (msgType.equals(KEY_CHAT_IMAGE)) {
                    imageURL = url;
                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_IMAGE, url, url, fileSize, ts, genderto);
                } else if (msgType.equals(KEY_CHAT_AUDIO)) {
                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_AUDIO, url, url, fileSize, ts, genderto);
                } else if (msgType.equals(KEY_CHAT_GIF)) {
                } else if (msgType.equals(KEY_CHAT_VIDEO)) {
                    //TODO: if thumbimage url than pass url otherwise message
                    msg = data.getJSONObject(KEY_CHAT_MSG);
                    file = msg.getJSONObject(KEY_CHAT_FILE);
                    if (file.has(KEY_VIDEO_THUMBNAIL_URL)) {
                        String videoThumbUrl = file.getString(KEY_VIDEO_THUMBNAIL_URL);//TODO: change later on
                        if (videoThumbUrl.length() > 0) {
                            saveChatMessageData(username, userId, categoryname, videoThumbUrl, Const.TYPE_VIDEO, url, url, fileSize, ts, genderto);
                        } else {
                            saveChatMessageData(username, userId, categoryname, message, Const.TYPE_VIDEO, url, url, fileSize, ts, genderto);
                        }
                    } else {
                        saveChatMessageData(username, userId, categoryname, message, Const.TYPE_VIDEO, url, url, fileSize, ts, genderto);
                    }
                } else if (msgType.equals(KEY_CHAT_GIFT)) {
                    giftname = data.getJSONObject(KEY_CHAT_MSG).getString(KEY_CHAT_GIFT);
                    giftcategoryName = data.getString(KEY_CHAT_GIFTCATEGORY);
                    String iconSmallPath = "@drawable/" + Const.giftPrefix + giftname;
                    int iconSmall = context.getResources().getIdentifier(iconSmallPath, null, context.getPackageName());
//                    String iconSmallPath = Const.giftPrefix + giftname;
//                    int iconSmall = getResId(iconSmallPath, Drawable.class);

                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_GIFT, String.valueOf(iconSmall), String.valueOf(iconSmall), giftcategoryName, ts, genderto);
                } else if (msgType.equals(KEY_STATUS)) {
                    if (data.has(KEY_CHAT_GIFT)) {
                        giftname = data.getString(KEY_CHAT_GIFT);
//                        giftname=data.getJSONObject(KEY_CHAT_MSG).getString(KEY_CHAT_GIFT);
                    } else {
                        giftname = "NO";
                    }
                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_STATUS, giftname, giftname, "0", ts, genderto);
                } else if (msgType.equals(KEY_CHAT_LIKE)) {
//                            giftname = data.getString(KEY_CHAT_GIFT);
                    saveChatMessageData(username, userId, categoryname, message, Const.TYPE_LIKE, "", "", "", ts, genderto);
                } else if (msgType.equals(KEY_CHAT_INSTANTSELFIE)) {
                    imageURL = url;
                    if (data.has(KEY_CHAT_MSGTYPE1)) {
                        message = data.getString(KEY_CHAT_MSG1);
                    } else {
                        message = "No";
                    }
                    if (status_accept == 1) {
                        ts = data.getString(KEY_GIFTTIME);
                        saveChatMessageData(username, userId, categoryname, "Your instant selfie request has been accepted", Const.TYPE_STATUS, "NO", "NO", "0", ts, genderto);
                        saveChatMessageData(username, userId, categoryname, message, Const.TYPE_INSTANT_SELFIE, url, url, fileSize, ts, genderto);
                    } else if (status_accept == -1) {
                        saveChatMessageData(username, userId, categoryname, "Your instant selfie request has been rejected", Const.TYPE_STATUS, "NO", "NO", "0", ts, genderto);
                    } else {
                        if (status_accept == 2 || status_accept == -2) {
                            ts = data.getString(KEY_GIFTTIME);
                        }
                        saveChatMessageData(username, userId, categoryname, message, Const.TYPE_INSTANT_SELFIE, url, url, fileSize, ts, genderto);
                    }
                }
            }

            if (imageURL.equalsIgnoreCase("NO")) {
                if (msgType.equals(KEY_CHAT_GIFT)) {
                    String messageNotification = "You have received a gift from " + username;
                    showNotificationMessage(context, username, messageNotification, ts, userId);
                } else if (msgType.equals(KEY_CHAT_AUDIO)) {
                    String messageNotification = "You have received a audio from " + username;
                    showNotificationMessage(context, username, messageNotification, ts, userId);
                } else if (msgType.equals(KEY_CHAT_VIDEO)) {
                    String messageNotification = "You have received a video from " + username;
                    showNotificationMessage(context, username, messageNotification, ts, userId);
                } else {
                    showNotificationMessage(context, username, message, ts, userId);
                }
            } else {
                String messageNotification = "You have received a image from " + username;
                showNotificationMessageWithBigImage(context, username, messageNotification, ts, imageURL, userId);
            }
        } catch (Exception e) {
//            Log.e("setNewChatMessage...", "" + e.toString());
        }
    }

    private static NotificationUtils notificationUtils;

    *//**
     * Showing notification with text only
     *//*
    private static void showNotificationMessage(Context context, String title, String message, String timeStamp, String userid) {
        notificationUtils = new NotificationUtils(context);
        Intent intent = setNotificationIntent(userid, context);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    *//**
     * Showing notification with text and image
     *//*
    private static void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, String imageUrl, String userid) {
        notificationUtils = new NotificationUtils(context);
        Intent intent = setNotificationIntent(userid, context);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    public static Intent setNotificationIntent(String userid, Context context) {
       *//* Intent resultIntent = null;
        resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtra(KEY_LOCALNOTIFICATION, false);
        *//*
        Intent notificationIntent = new Intent(context, ChatActivity.class);
        User userOther = objDBController.getChatUsersListById(userid);
        notificationIntent.putExtra(KEY_CHAT_USERS, userOther);
        notificationIntent.putExtra(KEY_LOCALNOTIFICATION, true);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        *//*if (user != null) {
            notificationIntent.putExtra(KEY_CHAT_USERS, user);
        }*//*
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return notificationIntent;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }*/
}
