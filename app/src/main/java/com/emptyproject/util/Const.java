package com.emptyproject.util;

import android.app.Activity;

import com.datecoy.BuildConfig;

/**
 * Created by infyzo4 on 26/9/15.
 * <p>
 * It contains All strings constants and web service urls
 */
public class Const extends Activity {
    public static final String TAG = "GCM Android Example";
    public static final String SMS_ORIGIN = "Datcoy";
    public static final String IMAGE_DIRECTORY_NAME = "Datecoy/Images";
    public static final String VIDEO_DIRECTORY_NAME = "Datecoy/Videos";
    public static final String AUDIO_DIRECTORY_NAME = "Datecoy/Audios";

    public static final String CHAT_IMAGE_DIRECTORY_NAME = "Datecoy/Images";
    public static final String CHAT_VIDEO_DIRECTORY_NAME = "Datecoy/Videos";
    public static final String CHAT_AUDIO_DIRECTORY_NAME = "Datecoy/Audios";

    //For Shared Preferences keys
    public static final String SHARED_PREF_FILE = "lovecoy";

//   public static final String LIVE = "http://18.220.170.12:80/";
    public static final String APIVERSION = "v1";
    public static final String APIURL = BuildConfig.WEB_SERVER_URL + APIVERSION + "/";


    // TODO LOGIN, SIGNUP, CHANGE PASSWORD, FORGOT PASSWORD, OTP, OTP RESEND, SIGNOUT FUNCTIONALITY
    public static final String LOGIN = APIURL + "login";
    public static final String CHANGEPASSWORD = APIURL + "user/setPassword";
    public static final String FORGOTPASSWORD = APIURL + "user/forgotPassword";
    public static final String SIGNUP = APIURL + "signUp";
    public static final String OTP = APIURL + "verifyOtp";
    public static final String OTP_RESEND = APIURL + "resendOtp";
    public static final String USER_SIGNOUT = APIURL + "user/signout";

    public static final String USER_SET_MOBILE = APIURL + "user/setMobile";
    public static final String SEARCH = APIURL + "match";
    public static final String UPDATE_LOCATION = APIURL + "updateLocation";
    public static final String USER_VIEW = APIURL + "user/view";
    public static final String USER_EDIT = APIURL + "user/edit";
    public static final String USER_SAVE = APIURL + "user/save";
    public static final String ACCOUNT_DELETE = APIURL + "user/delete";

    public static final String USER_SPLASH = APIURL + "splash";
    public static final String USER_PROPOSAL = APIURL + "user/list";
    public static final String USER_SUBSCRIPTION = APIURL + "user/subscription";
    public static final String USER_PAYMENT_HISTORY = APIURL + "user/orders";
    public static final String USER_PAYMENT_SLAB = APIURL + "slabs/get";
    public static final String USER_PAYMENT_RECEIPT = APIURL + "user/paymentReceipt";
    public static final String USER_MAKE_PAYMENT = APIURL + "user/payment";
    public static final String USER_PAYMENT_GET = APIURL + "get/payment";
    public static final String USER_LIKE = APIURL + "like";
    public static final String USER_PROFILE_PROFILE_MEDIA_ACTION = APIURL + "user/mediaAction";

    // TODO CHAT FUNCTIONALITY
    public static final String CHAT_REQUEST = APIURL + "user/action";
    public static final String CHAT_FILE_UPLOAD = APIURL + "chat/saveMedia";
    public static final String CHAT_USER_LIST = APIURL + "chat/list";
    public static final String CAN_CHAT_GET = APIURL + "/chat/get";


    // TODO DATE, MYDATE FUNCTIONALITY
    public static final String DATEVIEW = APIURL + "dateView";
    public static final String DATEVIEW_SAVE = APIURL + "date/add";
    public static final String DATE_SEARCH = APIURL + "dateMatch";
    public static final String DATE_ACTIONS = APIURL + "date/action";
    public static final String DATE_LIST = APIURL + "dates";
    public static final String DATE_EDIT = APIURL + "date/edit";
    public static final String DATE_HISTORY = APIURL + "date/history";

    public static int WEIGHT_MIN = 40, WEIGHT_MAX = 150, AGE_MIN = 18, AGE_MAX = 70;

    public static int totalProfileField = 19;
    public static boolean isPaymentStatus = false;
    public static String payment_txnid = "";

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_GIF = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_IMAGE = 3;
    public static final int TYPE_AUDIO = 4;
    public static final int TYPE_GIFT = 5;
    public static final int TYPE_INSTANT_SELFIE = 6;
    public static final int TYPE_STATUS = 7;
    public static final int TYPE_LIKE = 8;
    public static final int TYPE_STATUS_DATEOMETER = 9;


    public static String[] fields = {"Tagline", "About Me", "Ideal Date", "Interests", "Profession", "Height", "Hair Color",
            "Weight", "Sun Sign", "Complexion", "Diet", "Body Type", "Smoking", "Eye Color", "Drinking", "Known Language"};
    public static String[] foots = {"3\'", "4\'", "5\'", "6\'", "7\'", "8\'"};
    public static String[] inches = {"0\"", "1\"", "2\"", "3\"", "4\"", "5\"", "6\"", "7\"", "8\"", "9\"", "10\"", "11\""};

    public static String SELECT_OPTION = "--Select--";
    public static String regex = "[0-9]+";


    public static final String MYDATETYPE_CREATED = "created";
    public static final String MYDATETYPE_REQUESTED = "requested";
    public static final String MYDATETYPE_CONFIRMED = "confirmed";


    // TODO Date Options Value
    public static final String DATETYPE_MOVIE = "MOVIE";
    public static final String DATETYPE_COFFEE = "COFFEE";
    public static final String DATETYPE_PUB = "PUB";
    public static final String DATETYPE_LUNCH = "LUNCH";
    public static final String DATETYPE_DINNER = "DINNER";

    public static final String PAIDBY_ME = "Paid by me";
    public static final String PAIDBY_YOU = "Paid by you";
    public static final String PAIDBY_50 = "50 - 50";

    public static final int SWIPE_THRESHOLD_VELOCITY = 200;
    public static final int SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_MAX_OFF_PATH = 250;


    public static final int DATE_AVAILABLE_TYPE = 1;
    public static final int DATE_EXCLUSIVE_TYPE = 2;
    public static final int DATE_REQUESTED_TYPE = 3;
    public static final int DATE_CONFIRMED_TYPE = 4;
    public static final int DATE_CONFIRMED_TYPE_EDIT = 5;
    public static final int DATE_CONFIRMED_TYPE_CANCEL = 6;
    public static final int DATE_CONFIRMED_TYPE_RECONFIRM = 7;

    public static final String NOTIFICATION_TYPE_CHAT = "chat";
    public static final String NOTIFICATION_TYPE_PROPOSAL = "proposal";
    public static final String NOTIFICATION_TYPE_DATE = "date";
    public static final String NOTIFICATION_TYPE_DATEREQUEST = "dateRequest";
    public static final String NOTIFICATION_TYPE_DATEEDIT = "dateEdit";
    public static final String NOTIFICATION_TYPE_DATECANCELREQUESTED = "dateCancelRequested";
    public static final String NOTIFICATION_TYPE_DATECANCELCONFIRMED = "dateCancelConfirmed";
    public static final String NOTIFICATION_TYPE_DATECONFIRM = "dateConfirm";

    public static int indexTab = 1;
    public static String giftPrefix = "ic_chat_gift_";
}