package com.emptyproject.webapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.emptyproject.BuildConfig;
import com.emptyproject.R;
import com.emptyproject.listeners.WebServiceListener;
import com.emptyproject.util.AppController;
import com.emptyproject.util.Const;
import com.emptyproject.util.DeviceInfoUtil;
import com.emptyproject.util.InternetReachability;
import com.emptyproject.util.PrefsManager;
import com.emptyproject.util.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetWebservice implements WebUtil {

    public static GetWebservice getWebservice;
    public static Activity mactivity;
    ProgressDialog PD;
    JSONObject jDataRequest;
    private RequestQueue mRequestQueue;
    public static final String TAG = AppController.class
            .getSimpleName();

    public static GetWebservice getInstance(Activity activity) {
        mactivity = activity;
        if (getWebservice == null)
            getWebservice = new GetWebservice();
        return getWebservice;
    }

    private JsonObjectRequest mJsObjRequest;
    private String mTag;


    public void getClientRequest(int methodname, String url, final JSONObject sRequest, final String requestqueue, final WebServiceListener listner) {

        mTag = requestqueue;

        try {
            jDataRequest = new JSONObject();
            jDataRequest.put(KEY_DATA, sRequest);
            Util.getInstance(mactivity).showLog(url);
            Util.getInstance(mactivity).showLog(jDataRequest.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (InternetReachability.hasConnection(mactivity)) {
            //splashUser connectionActions
            if (mTag.equalsIgnoreCase("getMyMatchPagination")
                    || mTag.equalsIgnoreCase("connectionLike")
                    || mTag.equalsIgnoreCase("splashUser")
                    || mTag.equalsIgnoreCase("getChatGift")
                    || mTag.equalsIgnoreCase("connectionActions")
                    || mTag.equalsIgnoreCase("getUpdateLocation")
                    || mTag.equalsIgnoreCase("updateGCMToken")) {
            } else {
                if (PD == null || !PD.isShowing()) {
                    PD = new ProgressDialog(mactivity);
                    PD.setCancelable(false);
                    PD.show();
                    PD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    PD.setContentView(R.layout.progressdialog);
                }
            }
            JsonObjectRequest stringRequest = new JsonObjectRequest(methodname, url, jDataRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Util.getInstance(mactivity).showLog(response.toString());
                            setRequestQueryDismiss();
                            try {
                                if (response.getInt(RESPONSE_CODE) == RESPONSE_CODE_VALUE) {
                                    listner.onLoadSuccess(response);
                                } else if (response.getInt(RESPONSE_CODE) == RESPONSE_CODE_204
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_999
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_997
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_996) {
                                    listner.onLoadFail(response);
                                } else if (response.getInt(RESPONSE_CODE) == RESPONSE_CODE_401
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_422
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_498
                                        || response.getInt(RESPONSE_CODE) == RESPONSE_CODE_500) {
                                    if (response.has(KEY_RESPONSE_MSG)) {
                                        Util.getInstance(mactivity).ShowToast(response.getString(KEY_RESPONSE_MSG));
                                    }
                                } else if (response.getInt(RESPONSE_CODE) == RESPONSE_CODE_403) {
                                    Util.getInstance(mactivity).showErrorAlertDialog("Error!", response.getString(KEY_RESPONSE_MSG));
                                } else if (response.getInt(RESPONSE_CODE) == RESPONSE_CODE_998) {
                                /*    Util.getInstance(mactivity).showErrorAlertDialog("Error!", response.getString(KEY_RESPONSE_MSG));*/
                                }
                              /*  else {
                                    errorMessage(response);
                                }*/

                            } catch (Exception e) {
                                listner.onLoadFail(response);
                                setRequestQueryDismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setRequestQueryDismiss();
                            SetVolleyError(error, requestqueue, listner);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    if (PrefsManager.getAccessTokenLogin().length() > 0) {
                        Util.getInstance(mactivity).showLog("getHeaders..." + PrefsManager.getAccessTokenLogin());
                        params.put(KEY_X_ACCESS_TOKEN, PrefsManager.getAccessTokenLogin());
                    }
                    return params;
                }
               /* @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_DATA,sRequest.toString());
                    return params;
                }*/
            };
            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            addToRequestQueue(stringRequest, requestqueue);
        } else {
            Util.getInstance(mactivity).ShowToast(mactivity.getResources().getString(R.string.alert_no_internet));
        }

    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mactivity);
        }
//        RequestQueue queue = new RequestQueue(new DiskBasedCache(new File(getCacheDir(), "volley")), network);
        return mRequestQueue;
    }

    public void setRequestQueryDismiss() {
        if (mTag.equalsIgnoreCase("getMyMatchPagination") || mTag.equalsIgnoreCase("getSwipeAction") || mTag.equalsIgnoreCase("splashUser")) {
        } else {
            isDismiss();
        }
    }


    public void isDismiss() {
        if (PD != null) {
            if (PD.isShowing()) {
                PD.dismiss();
            }
        }
    }

    public void SetVolleyError(VolleyError error, String methodType, WebServiceListener listner) {
        if (error != null) {
            if (error.networkResponse != null) {
                if (error.getClass().equals(com.android.volley.TimeoutError.class)) {
                    Util.getInstance(mactivity).ShowToast("Timeout error");
                }
                if (error.networkResponse.data != null) {
                    byte[] bytes = error.networkResponse.data;
                    String sResponse = new String(bytes);
                    try {
                        JSONObject jResponse = new JSONObject(sResponse);
                    } catch (Exception e) {
                        setError(error);
                    }
                }
            } else {
                setError(error);
            }
        }
    }

    public void setError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Util.getInstance(mactivity).ShowToast("Sorry, there was a problem communicating with Datecoy. Please check network connectivity");
        } else if (error instanceof AuthFailureError) {
        } else if (error instanceof ServerError) {
            Util.getInstance(mactivity).ShowToast("The server is temporarily unable to service your request due to maintenance downtime or capacity problems. Please try again later.");
        } else if (error instanceof NetworkError) {
            //We can not proceed due to slow internet connection. Please try again later
            Util.getInstance(mactivity).ShowToast("The server is temporarily unable to service your request due to maintenance downtime or capacity problems. Please try again later.");
        } else if (error instanceof ParseError) {
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static String multipartRequest2(String urlTo, String parmas, String filepath[], String filefield[], String fileMimeType) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        try {
            //int i=0;
            //File upload start
            for (int i = 0; i < filepath.length; i++) {
                if (!filepath[i].equals("0")) {
                    File file = new File(filepath[i]);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    URL url = new URL(urlTo);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty(KEY_X_ACCESS_TOKEN, PrefsManager.getAccessTokenLogin());
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    connection.setRequestProperty(filefield[i], filepath[i]);
                    outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield[i] + "\";filename=\"" + filepath[i] + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    String issue_details_key = "data";
                    String issue_details_value = parmas;
                    try {
                        outputStream.writeBytes("Content-Disposition: form-data; name=\""
                                + issue_details_key + "\"" + lineEnd
                                + "Content-Type: application/json" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(issue_details_value);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    } catch (IOException ioe) {
                        Util.getInstance(mactivity).showLog("Debug" + "error: " + ioe.getMessage() + ioe);
                    }

                    // Responses from the server (code and message)
                    int serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                    inputStream = connection.getInputStream();
                    result = convertStreamToString(inputStream);
                    fileInputStream.close();
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                    // 400 422  401
                    //500
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getCommonPara(JSONObject jBasic) {
        try {
            String deviceId = Util.getInstance(mactivity).getDeviceId();
            jBasic.put(KEY_UUID, deviceId);
            if (PrefsManager.getUid().length() > 0 && !PrefsManager.getUid().equalsIgnoreCase("0")) {
                jBasic.put(KEY_UID, PrefsManager.getUid());
            }
        } catch (Exception e) {
        }
        return jBasic;
    }

    /* Forgot Password API call*/
    public void resendOTP(JSONObject jForgotPwdPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.OTP_RESEND, getCommonPara(jForgotPwdPara), "ResendOTP", WebServiceListener);
    }

    /* Forgot Password API call*/
    public void forgotpasswordUser(JSONObject jForgotPwdPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.FORGOTPASSWORD, getCommonPara(jForgotPwdPara), "ForgotPassword", WebServiceListener);
    }

    /* Change Password or Forgot Change Password API call*/
    public void changepasswordUser(JSONObject jChangePWdPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.CHANGEPASSWORD, getCommonPara(jChangePWdPara), "ChangePassword", WebServiceListener);
    }

    /* Mobile change OTP API call*/
    public void setMobile(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_SET_MOBILE, getCommonPara(jOtpPara), "OTP", WebServiceListener);
    }

    /* OTP API call*/
    public void getOTPUser(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.OTP, getCommonPara(jOtpPara), "OTP", WebServiceListener);
    }
    /* MySubscription API call*/
    public void getMySubscription(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_SUBSCRIPTION, jOtpPara, "MySubscription", WebServiceListener);
    }

    /* PaymentHistory API call*/
    public void getPaymentHistory(WebServiceListener WebServiceListener) {
        JSONObject jData = new JSONObject();
        getClientRequest(Request.Method.POST, Const.USER_PAYMENT_HISTORY, getCommonPara(jData), "PaymentHistory", WebServiceListener);
    }

    /* PaymentDetails API call*/
    public void getPaymentDetails(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_PAYMENT_SLAB, getCommonPara(jOtpPara), "PaymentDetails", WebServiceListener);
    }

    /* PaymentReceipt API call*/
    public void getPaymentReceipt(WebServiceListener WebServiceListener) {
        JSONObject jPaymentReceipt = new JSONObject();
        try {
            jPaymentReceipt = getCommonPara(jPaymentReceipt);
            jPaymentReceipt.put(KEY_PAYMENT_TRANSACTIONID, Const.payment_txnid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getClientRequest(Request.Method.POST, Const.USER_PAYMENT_RECEIPT, jPaymentReceipt, "GetPaymentReceipt", WebServiceListener);
    }

    /* makePayment API call*/
    public void makePayment(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_MAKE_PAYMENT, getCommonPara(jOtpPara), "makePayment", WebServiceListener);
    }

    /* makePaymentInfo API call*/
    public void getPaymentInfo(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_MAKE_PAYMENT, getCommonPara(jOtpPara), "getPaymentInfo", WebServiceListener);
    }

    /* get payment dialog API call*/
    public void getPaymentDialog(JSONObject jOtpPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_PAYMENT_GET, getCommonPara(jOtpPara), "getPaymentDialog", WebServiceListener);
    }

    /* Login API call*/
    public void loginUser(JSONObject jLoginUserPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.LOGIN, getCommonPara(jLoginUserPara), "Login", WebServiceListener);
    }

    public void saveProfileField(JSONObject jLoginUserPara, WebServiceListener WebServiceListener, String keyfield) {
        try {
            jLoginUserPara = getCommonPara(jLoginUserPara);
            jLoginUserPara.put("type", "oneField");
            jLoginUserPara.put("field", keyfield);
        } catch (Exception e) {
        }
        getClientRequest(Request.Method.POST, Const.USER_SAVE, jLoginUserPara, "ProfileField", WebServiceListener);
    }

    public void getDeleteUserProfile(WebServiceListener WebServiceListener) {
        JSONObject jData = new JSONObject();
        getClientRequest(Request.Method.POST, Const.ACCOUNT_DELETE, getCommonPara(jData), "DeleteUserProfile", WebServiceListener);
    }
    public void getUserProfileNew(WebServiceListener WebServiceListener) {
        JSONObject jData = new JSONObject();
        getClientRequest(Request.Method.POST, Const.USER_VIEW, getCommonPara(jData), "getUserProfileView", WebServiceListener);
    }
    public void getChatProfileNew(JSONObject jProfileSearchPara, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.USER_VIEW, getCommonPara(jProfileSearchPara), "getChatProfileNew", WebServiceListener);
    }
    //get search list
    public void getProfileSearch(JSONObject jProfileSearchPara, WebServiceListener WebServiceListener) {
        try {
            jProfileSearchPara = getCommonPara(jProfileSearchPara);
        } catch (Exception e) {

        }
        getClientRequest(Request.Method.POST, Const.SEARCH, jProfileSearchPara, "getProfileSearch", WebServiceListener);
    }

    //TODO Profile hold,ignore or request on search screen
    public void connectionActions(JSONObject jProfileSearchPara, WebServiceListener WebServiceListener) {
        try {
            jProfileSearchPara = getCommonPara(jProfileSearchPara);
        } catch (Exception e) {

        }
        getClientRequest(Request.Method.POST, Const.CHAT_REQUEST, jProfileSearchPara, "connectionActions", WebServiceListener);
    }

    //Profile like on search screen
    public void connectionLike(JSONObject jProfileSearchPara, WebServiceListener WebServiceListener) {
        try {
            jProfileSearchPara = getCommonPara(jProfileSearchPara);
        } catch (Exception e) {

        }
        getClientRequest(Request.Method.POST, Const.USER_LIKE, jProfileSearchPara, "connectionLike", WebServiceListener);
    }

    //Profile like on search screen
    public void viewMedia(JSONObject jProfileSearchPara, WebServiceListener WebServiceListener) {
        try {
            jProfileSearchPara = getCommonPara(jProfileSearchPara);
        } catch (Exception e) {

        }
        getClientRequest(Request.Method.POST, Const.USER_PROFILE_PROFILE_MEDIA_ACTION, jProfileSearchPara, "viewMedia", WebServiceListener);
    }

    public void getProfileField(JSONObject jLoginUserPara, WebServiceListener WebServiceListener) {
        try {
            jLoginUserPara = getCommonPara(jLoginUserPara);
            jLoginUserPara.put("type", "oneField");
        } catch (Exception e) {
            e.printStackTrace();
        }
        getClientRequest(Request.Method.POST, Const.USER_EDIT, jLoginUserPara, "getProfileField", WebServiceListener);
    }
    public void deletePofilePics(JSONObject jLoginUserPara, WebServiceListener WebServiceListener) {
        jLoginUserPara = getCommonPara(jLoginUserPara);
        getClientRequest(Request.Method.POST, Const.USER_PROFILE_PROFILE_MEDIA_ACTION, jLoginUserPara, "deletePofilePics", WebServiceListener);
    }

    public void changePofilePics(JSONObject jLoginUserPara, WebServiceListener WebServiceListener) {
        jLoginUserPara = getCommonPara(jLoginUserPara);
        getClientRequest(Request.Method.POST, Const.USER_PROFILE_PROFILE_MEDIA_ACTION, jLoginUserPara, "changePofilePics", WebServiceListener);
    }
    // Chat web apis
    public void chatUesrsList(WebServiceListener WebServiceListener) {
        JSONObject jData = new JSONObject();
        getClientRequest(Request.Method.POST, Const.CHAT_USER_LIST, getCommonPara(jData), "chatUesrsList", WebServiceListener);
    }

    public void getChatCan(JSONObject jLoginUserPara, WebServiceListener WebServiceListener, String mChatUserID) {
        getClientRequest(Request.Method.POST, Const.CAN_CHAT_GET, chatRequest(jLoginUserPara, mChatUserID), "getChatCan", WebServiceListener);
    }

    public JSONObject chatRequest(JSONObject jLoginUserPara, String mChatUserID) {
        try {
            jLoginUserPara = getCommonPara(jLoginUserPara);
            jLoginUserPara.put(KEY_CHAT_TO, mChatUserID);
        } catch (Exception e) {
        }
        return jLoginUserPara;
    }
    // GCM token update
    public void updateGCMToken(WebServiceListener WebServiceListener) {
        JSONObject params = new JSONObject();
        try {
            params = getCommonPara(params);
            params.put(KEY_TYPE, "updateToken");
            params.put(KEY_CHAT_GCMTOKEN, PrefsManager.getGcmToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getClientRequest(Request.Method.POST, Const.USER_SAVE, params, "updateGCMToken", WebServiceListener);
    }
    public void getLatLonUpdate(JSONObject jLatLonPara, WebServiceListener WebServiceListener) {
        try {
            jLatLonPara = getCommonPara(jLatLonPara);
            jLatLonPara.put(KEY_TYPE, "updateLocation");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception");
        }
        getClientRequest(Request.Method.POST, Const.UPDATE_LOCATION, jLatLonPara, "getUpdateLocation", WebServiceListener);
    }
    //Hold User Get List
    public void getProposalList(WebServiceListener WebServiceListener) {
        JSONObject params = new JSONObject();
        params = getCommonPara(params);
        getClientRequest(Request.Method.POST, Const.USER_PROPOSAL, params, "getjProposalList", WebServiceListener);
    }

    //Signout
    public void signOutUser(WebServiceListener WebServiceListener) {
        JSONObject request = new JSONObject();
        getClientRequest(Request.Method.POST, Const.USER_SIGNOUT, getCommonPara(request), "signOutUser", WebServiceListener);
    }

    //User Splash Screen For Exclusive Chat
    public void getSplashUser(WebServiceListener WebServiceListener) {
        JSONObject jDeviceInfo = new JSONObject();
        JSONObject jParam = new JSONObject();
        try {
            jParam = getCommonPara(jParam);
            jParam.put(KEY_VERSION, BuildConfig.VERSION_NAME.toString());

            jDeviceInfo.put(KEY_OS_VERSION, System.getProperty("os.version"));
            jDeviceInfo.put(KEY_OS_RELEASE, Build.VERSION.RELEASE);
            jDeviceInfo.put(KEY_DEVICE, Build.DEVICE);
            jDeviceInfo.put(KEY_MODEL, Build.MODEL);
            jDeviceInfo.put(KEY_PRODUCT, Build.PRODUCT);
            jDeviceInfo.put(KEY_BRAND, Build.BRAND);
            jDeviceInfo.put(KEY_DISPLAY, Build.DISPLAY);
            jDeviceInfo.put(KEY_HARDWARE, Build.HARDWARE);
            jDeviceInfo.put(KEY_DEVICE_ID, Build.ID);
            jDeviceInfo.put(KEY_MANUFACTURER, Build.MANUFACTURER);
            jDeviceInfo.put(KEY_SERIAL, Build.SERIAL);
            jDeviceInfo.put(KEY_USER, Build.USER);
            jDeviceInfo.put(KEY_HOST, Build.HOST);

            jDeviceInfo.put(KEY_OS_NAME, DeviceInfoUtil.getInstance(mactivity).getAndroidVersion(Build.VERSION.SDK_INT));
            //jDeviceInfo.put(KEY_UI, Build.USER);
            jDeviceInfo.put(KEY_CPU, DeviceInfoUtil.getInstance(mactivity).getCPUInfo());
            jDeviceInfo.put(KEY_MAC_ADDRESS, DeviceInfoUtil.getInstance(mactivity).getMACAddress("wlan0"));
            jDeviceInfo.put(KEY_RAM, DeviceInfoUtil.getInstance(mactivity).getRAMInfo());
            jDeviceInfo.put(KEY_INTERNAL_MEMORY, DeviceInfoUtil.getInstance(mactivity).getTotalInternalMemorySize());
            jDeviceInfo.put(KEY_IP_ADDRESS, DeviceInfoUtil.getInstance(mactivity).getIPAddress(true));//"true" for IPv4 and "false" for IPv6
            jDeviceInfo.put(KEY_RESOLUTION_IN_PIXELS, DeviceInfoUtil.getInstance(mactivity).getResolution());
            jDeviceInfo.put(KEY_SCREEN_HEIGHT, DeviceInfoUtil.getInstance(mactivity).getPhysicalSize());

            jParam.put(KEY_DEVICE_INFO, jDeviceInfo);
        } catch (Exception e) {
        }
        getClientRequest(Request.Method.POST, Const.USER_SPLASH, jParam, "splashUser", WebServiceListener);
    }


    public void getDateView(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATEVIEW, getCommonPara(jdateView), "dateView", WebServiceListener);
    }
    public void saveDateView(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATEVIEW_SAVE, getCommonPara(jdateView), "dateView", WebServiceListener);
    }
    public void getDateSearch(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATE_SEARCH, getCommonPara(jdateView), "dateSearch", WebServiceListener);
    }
    public void dateActions(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATE_ACTIONS, getCommonPara(jdateView), "dateActions", WebServiceListener);
    }
    //Web service for Date List - My Created , My Requested, My Confirmed
    public void getDateList(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATE_LIST, getCommonPara(jdateView), "dateList", WebServiceListener);
    }
    public void editDate(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATE_EDIT, getCommonPara(jdateView), "dateEdit", WebServiceListener);
    }
    public void getDateHistory(JSONObject jdateView, WebServiceListener WebServiceListener) {
        getClientRequest(Request.Method.POST, Const.DATE_HISTORY, getCommonPara(jdateView), "dateHistory", WebServiceListener);
    }
}
