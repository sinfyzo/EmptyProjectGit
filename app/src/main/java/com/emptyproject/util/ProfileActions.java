package com.emptyproject.util;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import com.datecoy.R;
import com.datecoy.listioner.CommonListioner;
import com.datecoy.listioner.DownloadListioner;
import com.datecoy.model.UserNew;
import com.datecoy.mydatesearch.DateHistoryActivity;
import com.datecoy.profile.PhotoGallaryActivity;
import com.datecoy.webapi.DownloadFileAsync;
import com.datecoy.webapi.GetWebservice;
import com.datecoy.webapi.WebUtil;

import org.json.JSONObject;

/**
 * Created by INFYZO\shailesh.pateliya on 21/8/17.
 */

public class ProfileActions implements WebUtil{
    public static ProfileActions profileActions;
    public static Activity mactivity;

    public static ProfileActions getInstance(Activity activity) {
        mactivity = activity;
        if (profileActions == null)
            profileActions = new ProfileActions();
        return profileActions;
    }
    // TODO     "Actions" Called when Like request(Like Button clicked)
    public void requestLike(UserNew user,  CommonListioner listner, int profileType) {
        try {
            JSONObject jProfileSearch = new JSONObject();
            jProfileSearch.put(KEY_NAME, PrefsManager.getUserName());
            jProfileSearch.put(KEY_CHAT_TO, user.getUid());
            jProfileSearch.put(KEY_CONNECTIONSTATUS, KEY_CONNECTION_TYPE_REQUESTED);

            GetWebservice.getInstance(mactivity).connectionLike(jProfileSearch, listner);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // TODO:    "Actions" Called when chat or hold or ignore request(Hey or hold Button clicked or swipe action)
    public void requestProfileAction(UserNew user,  CommonListioner listner, int profileType, String connectionStatusType) {
        try {
            JSONObject jProfileSearch = new JSONObject();
            jProfileSearch.put(KEY_NAME, PrefsManager.getUserName());
            jProfileSearch.put(KEY_CHAT_TO, user.getUid());
            jProfileSearch.put(KEY_CONNECTIONSTATUS, connectionStatusType);
            GetWebservice.getInstance(mactivity).connectionActions(jProfileSearch, listner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // TODO:    "Date Actions"
    public void requestDateProfileAction(UserNew user, CommonListioner listner, int profileType, String connectionStatusType, String dateId) {
        try {
            JSONObject jProfileSearch = new JSONObject();
//            jProfileSearch.put(KEY_NAME, PrefsManager.getUserName());
            jProfileSearch.put(KEY_CHAT_TO, user.getUid());
            jProfileSearch.put(KEY_CONNECTIONSTATUS, connectionStatusType);
            jProfileSearch.put(KEY_DATE_ID, dateId);
            if (user.getActionInfo().equals(DATE_STATUS_PENDING)) {
                jProfileSearch.put(KEY_FROM_PROPOSAL, 1);
            }

            Util.getInstance(mactivity).showLog("requestProfileAction" + connectionStatusType);
            switch (user.getActionInfo()) {
                case DATE_STATUS_HOLD:
                case DATE_STATUS_NO_RELATION:
                    GetWebservice.getInstance(mactivity).connectionActions(jProfileSearch, listner);
                    break;
                case DATE_STATUS_PENDING:
                case DATE_STATUS_DATE_REQUEST:
                    GetWebservice.getInstance(mactivity).dateActions(jProfileSearch, listner);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPhotoGallary(UserNew user, int profileType){
        if (user.getProfilePicList().size() > 0) {
            if (user.getProfilePicList().get(0).getLarge().length() > 0) {
                Intent intent;
                intent = new Intent(mactivity, PhotoGallaryActivity.class);
                intent.putExtra("user", user);
                intent.putExtra(KEY_TYPE, profileType);
                Util.getInstance(mactivity).nextActivity(intent);
            } else {
                Util.getInstance(mactivity).ShowToast(mactivity.getResources().getString(R.string.msg_images_searched_profile));
            }
        } else {
            Util.getInstance(mactivity).ShowToast(mactivity.getResources().getString(R.string.msg_images_searched_profile));
        }
    }

    public void downloadMedia(String filePath, DownloadListioner downloadListioner, int profileType, int mediaType){
        DownloadFileAsync downloadFileAsync = new DownloadFileAsync(mactivity, filePath, true, downloadListioner, mediaType);
        downloadFileAsync.execute("", "", "");
    }
    public void playVideo(UserNew user, String filePath, CommonListioner listner, int profileType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(filePath), "video/*");
        mactivity.startActivity(intent);
        if(profileType != PROFILE_TYPE_SELF){
            notifyMediaView(user, listner, profileType, MEDIA_VIDEO,"" );
        }
    }
    public void playAudio(UserNew user, String filePath, CommonListioner listner, int profileType) {
        AudioUtil.getInstance(mactivity).showAudioDialog(new MediaPlayer(), filePath, user, listner,profileType);
    }
    public void showDateHistory(UserNew user, int profileType){
        Intent intent = new Intent(mactivity, DateHistoryActivity.class);
        intent.putExtra(KEY_ID, user.getUid());
        intent.putExtra(KEY_NAME, user.getName());
        Util.getInstance(mactivity).nextActivity(intent);
    }

    //TODO:     "Media View" Call Web service for Media view count
    public void notifyMediaView(UserNew user, CommonListioner listner, int profileType, String mediaType, String mediaUrl) {
        try {
            JSONObject jProfileSearch = new JSONObject();
            jProfileSearch.put(KEY_CHAT_TO, user.getUid());
            jProfileSearch.put(KEY_TYPE, mediaType);
            if(mediaType.equals(MEDIA_IMAGE)){
                jProfileSearch.put(KEY_URL, mediaUrl);
            }
            GetWebservice.getInstance(mactivity).viewMedia(jProfileSearch, listner);
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
