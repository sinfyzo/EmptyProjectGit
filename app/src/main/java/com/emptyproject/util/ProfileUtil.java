package com.emptyproject.util;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emptyproject.R;
import com.emptyproject.webapi.WebUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * Created by INFYZO\shailesh.pateliya on 30/6/17.
 */

public class ProfileUtil implements WebUtil {
    public static ProfileUtil profileUtil;
    public static Activity mactivity;

    public static ProfileUtil getInstance(Activity activity) {
        mactivity = activity;
        if (profileUtil == null)
            profileUtil = new ProfileUtil();
        return profileUtil;
    }

    public ArrayList<UserNew> getUsers(JSONArray jArray, int profileType) {
        ArrayList<UserNew> users = new ArrayList<>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                UserNew user = getUser(jObject, profileType);
                users.add(user);
            }

        } catch (JSONException e) {
            Log.e("JSONException...", e.toString());
        }
        return users;
    }

    public UserNew getUser(JSONObject jObject, int profileType) {
        UserNew user = new UserNew();
        try {
            user.setName(Util.getInstance(mactivity).setFirstLatterCaps(jObject.getString(KEY_NAME)));
            if (jObject.has(KEY_AGE))
                user.setAge(jObject.getInt(KEY_AGE));
            if (jObject.has(KEY_ID))
                user.setUid(jObject.getString(KEY_ID));
            if (jObject.has(KEY_CITY))
                user.setCity(jObject.getString(KEY_CITY));
            if (jObject.has(KEY_LIKES))
                user.setLikes(jObject.getInt(KEY_LIKES));
            if (jObject.has(KEY_TAGLINE))
                user.setTagLine(jObject.getString(KEY_TAGLINE));
            if (jObject.has(KEY_ABOUTME))
                user.setAboutMe(jObject.getString(KEY_ABOUTME));
            if (jObject.has(KEY_IDEAL_DATE))
                user.setIdealDate(jObject.getString(KEY_IDEAL_DATE));
            if (jObject.has(KEY_OCCUPATION))
                user.setOccupation(jObject.getString(KEY_OCCUPATION));
            if (jObject.has(KEY_DATE_PERCENTAGE))
                user.setDatePercentage(jObject.get(KEY_DATE_PERCENTAGE).toString());
            if (jObject.has(KEY_INTERESTS))
                user.setInterestList(makeInterestsList(jObject.getJSONArray(KEY_INTERESTS)));
            if (jObject.has(KEY_PROFILEPICS))
                user.setProfilePicList(makeProfilePicsList(jObject.getJSONArray(KEY_PROFILEPICS), profileType));
            else {
                if (profileType == PROFILE_TYPE_SELF)
                    user.setProfilePicList(makeProfilePicsList());
            }
            if (jObject.has(KEY_PERSONAL_DETAILS))
                user.setPersonalDetailList(makePersonalDetailsList(jObject.getJSONObject(KEY_PERSONAL_DETAILS)));
            if (jObject.has(KEY_RATINGS)) {
                JSONObject jRatings = jObject.getJSONObject(KEY_RATINGS);
                if (jRatings.has(KEY_TOTAL))
                    user.setTotalRating(jRatings.getDouble(KEY_TOTAL));
                if (jObject.has(KEY_ITEMS))
                    user.setRatingList(makeRatingList(jObject.getJSONArray(KEY_ITEMS)));
            }
            if (jObject.has(KEY_REVIEWS))
                user.setReviewList(makeReviewList(jObject.getJSONArray(KEY_REVIEWS)));
            if (jObject.has(KEY_ONLINE_TIME))
                user.setOnline(jObject.getBoolean(KEY_ONLINE_TIME));
            if (jObject.has(KEY_INDUSTRY))
                user.setIndustry(jObject.getString(KEY_INDUSTRY));
            if (jObject.has(KEY_DESIGNATION))
                user.setDesignation(jObject.getString(KEY_DESIGNATION));
            if (jObject.has(KEY_PROFILE_AUDIO)) {
                JSONObject jObj = jObject.getJSONObject(KEY_PROFILE_AUDIO);
                user.setProfileAudio(jObj.getString(KEY_URL));
                user.setIsProfileAudioApproved(jObj.getInt(KEY_IS_APPROVED));
                user.setAudioViewCount(jObj.has(KEY_VIEW_COUNT) ? jObj.getInt(KEY_VIEW_COUNT) : 0);
            }
            if (jObject.has(KEY_PROFILE_VIDEO)) {
                JSONObject jObj = jObject.getJSONObject(KEY_PROFILE_VIDEO);
                if (jObj.has(KEY_URL))
                    user.setProfileVideo(jObj.getString(KEY_URL));
                if (jObj.has(KEY_THUMBNAIL)) {
                    user.setProfileVideoThumb(jObj.getString(KEY_THUMBNAIL));
                }
                if (jObj.has(KEY_IS_APPROVED))
                    user.setIsProfileVideoApproved(jObj.getInt(KEY_IS_APPROVED));
                user.setVideoViewCount(jObj.has(KEY_VIEW_COUNT) ? jObj.getInt(KEY_VIEW_COUNT) : 0);
            }
            if (profileType == PROFILE_TYPE_SELF) {
                String profilePic = user.getProfilePicList().get(0).getLarge();
                PrefsManager.setProfileImage(profilePic.length() > 0 ? profilePic : "");
            }
            if (jObject.has(KEY_YOULIKED))
                user.setYouLiked(jObject.getBoolean(KEY_YOULIKED));
            if (jObject.has(KEY_ILIKED))
                user.setiLiked(jObject.getBoolean(KEY_ILIKED));
            if (jObject.has(KEY_IS_ONLINE))
                user.setOnline(jObject.getBoolean(KEY_IS_ONLINE));
            if (jObject.has(KEY_ACTION_INFO))
                user.setActionInfo(jObject.getString(KEY_ACTION_INFO));
            if (jObject.has(KEY_DATE_CONFIRMBEFORE))
                user.setConfirmBefore(Util.getInstance(mactivity).convertUtcToCurrentTimeZone(jObject.getString(KEY_DATE_CONFIRMBEFORE), 5));
            if (jObject.has(KEY_DATE_INFO))
                user.setDateInfo(jObject.getString(KEY_DATE_INFO));
        } catch (JSONException e) {
            Log.e("JSON...", e.toString());
        }
        return user;
    }
 /*     type=0 for Login User ProfilePics
           type=2 for search User ProfilePics
           type=3 for Chat User ProfilePics
           type=4 for Hold User ProfilePics
           type=5 for Proposal User ProfilePics
           */

    public ArrayList<ProfilePicItem> makeProfilePicsList(JSONArray jArray, int type) {
        ArrayList<ProfilePicItem> profilePicsList = new ArrayList<>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jImage = jArray.getJSONObject(i);
                ProfilePicItem interest = new ProfilePicItem();
                interest.setLarge(jImage.getString(KEY_LARGE));
                interest.setMedium(jImage.getString(KEY_MEDIUM));
                interest.setThumb(jImage.getString(KEY_THUMB));
                interest.setIsApproved(jImage.has(KEY_IS_APPROVED) ? jImage.getInt(KEY_IS_APPROVED) : 1);
                interest.setViewCount(jImage.has(KEY_VIEW_COUNT) ? jImage.getInt(KEY_VIEW_COUNT) : 0);
                profilePicsList.add(interest);
            }
            if (type == PROFILE_TYPE_SELF) {
                for (int i = 0; i < 10 - jArray.length(); i++) {
                    profilePicsList.add(addDummyImageEntry());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profilePicsList;
    }

    public ProfilePicItem getFirstProfilePic(JSONArray jArray) {
        ProfilePicItem profilePicItem = new ProfilePicItem();
        try {
            JSONObject jImage = jArray.getJSONObject(0);
            profilePicItem.setLarge(jImage.getString(KEY_LARGE));
            profilePicItem.setMedium(jImage.getString(KEY_MEDIUM));
            profilePicItem.setThumb(jImage.getString(KEY_THUMB));
        } catch (Exception e) {
        }
        return profilePicItem;
    }

    public ArrayList<ProfilePicItem> makeProfilePicsList() {
        ArrayList<ProfilePicItem> profilePicsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            profilePicsList.add(addDummyImageEntry());
        }
        return profilePicsList;
    }

    private ProfilePicItem addDummyImageEntry() {
        ProfilePicItem interest = new ProfilePicItem();
        interest.setLarge("");
        interest.setMedium("");
        interest.setThumb("");
        interest.setIsApproved(0);
        interest.setViewCount(0);
        return interest;
    }

    public ArrayList<ProfileField> makeInterestsList(JSONArray jArray) {
        ArrayList<ProfileField> interestList = new ArrayList<>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                ProfileField interest = new ProfileField();
                String value = jArray.getString(i).toLowerCase().replace(" ", "");
                interest.setIcon(Util.getInstance(mactivity).getImagePath("ic_profile_" + value));
                interest.setValue(jArray.getString(i));
                interestList.add(interest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return interestList;
    }

    private ArrayList<ProfileField> makePersonalDetailsList(JSONObject jObject) {
        ArrayList<ProfileField> profileFields = new ArrayList<>();
        try {
            Iterator<String> iterator = jObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ProfileField interest = new ProfileField();
                String value = key.toLowerCase().replace(' ', '_');
                interest.setIcon(Util.getInstance(mactivity).getImagePath("ic_profile_" + value));
                interest.setValue(String.valueOf(jObject.get(key)));
                interest.setKey(key);
                interest.setOrderIndex(getOrderIndex(key));
                profileFields.add(interest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(profileFields);
        return profileFields;
    }

    private int getOrderIndex(String key) {
        switch (key) {
            case KEY_HEIGHT:
                return 1;
            case KEY_HAIRCOLOR:
                return 2;
            case KEY_WEIGHT:
                return 3;
            case KEY_SUNSIGN:
                return 4;
            case KEY_COMPLEXION:
                return 5;
            case KEY_DIET:
                return 6;
            case KEY_BODYTYPE:
                return 7;
            case KEY_SMOKING:
                return 8;
            case KEY_EYECOLOR:
                return 9;
            case KEY_DRINKING:
                return 10;
            case KEY_KNOWNLANGUAGE:
                return 11;
            default:
                return 0;
        }
    }

    private ArrayList<ProfileField> makeRatingList(JSONArray jArray) {
        ArrayList<ProfileField> ratingList = new ArrayList<>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                ProfileField interest = new ProfileField();
                JSONObject jItem = jArray.getJSONObject(i);
                Iterator<String> iterator = jItem.keys();
                // while (iterator.hasNext()) {
                String key = iterator.next();
                interest.setIcon(Util.getInstance(mactivity).getImagePath("ic_profile_" + key));
                interest.setRating(jItem.getDouble(KEY_LOOKS));
                interest.setValue(key.substring(0, 1).toUpperCase() + key.substring(1));
                ratingList.add(interest);
                ratingList.add(interest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ratingList;
    }

    private ArrayList<Review> makeReviewList(JSONArray jArray) {
        ArrayList<Review> reviewList = new ArrayList<>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                Review review = new Review();
                JSONObject jItem = jArray.getJSONObject(i);
                if (jItem.has(KEY_PHOTO)) {
                    review.setPicUrl(jItem.getString(KEY_PHOTO));
                }
                if (jItem.has(KEY_NAME)) {
                    review.setName(jItem.getString(KEY_NAME));
                }
                if (jItem.has(KEY_CONTENT)) {
                    review.setContent(jItem.getString(KEY_CONTENT));
                }
                if (jItem.has(KEY_TIME)) {
                    review.setContent(jItem.getString(KEY_TIME));
                }
                reviewList.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    public void loadGrid(View contentView, ArrayList<ProfileField> interestList) {
        View view1 = null;
        for (int i = 1; i <= interestList.size(); i++) {
            ProfileField interest = interestList.get(i - 1);
            if (i % 2 == 1) {
                view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_items_two, null, false);
                ((ViewGroup) contentView).addView(view1);
                ((TextView) view1.findViewById(R.id.grid_text)).setText(interest.getValue());
                ((ImageView) view1.findViewById(R.id.grid_image)).setImageResource(interest.getIcon());
            } else {
                ((TextView) view1.findViewById(R.id.grid_text1)).setText(interest.getValue());
                ((ImageView) view1.findViewById(R.id.grid_image1)).setImageResource(interest.getIcon());
            }
        }
        if (interestList.size() % 2 == 1) {
            ((LinearLayout) view1.findViewById(R.id.item_two)).setVisibility(View.GONE);
        }
    }

    public void loadRatings(View contentView, ArrayList<ProfileField> ratingList) {
        View view1 = null;
        for (int i = 1; i <= ratingList.size(); i++) {
            ProfileField interest = ratingList.get(i - 1);
            view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_item_ratings, null, false);
            ((ViewGroup) contentView).addView(view1);
            ((TextView) view1.findViewById(R.id.item_text)).setText(interest.getValue());
            ((ImageView) view1.findViewById(R.id.item_icon)).setImageResource(interest.getIcon());
        }
    }

    public void addHeader(View view, String interests) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_text_header, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.header)).setText(interests);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, Util.getInstance(mactivity).dpToPixel(16));
        view1.setLayoutParams(params);
    }

    public void addText(View view, String interests) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_text_normal, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.normal)).setText(interests);
    }

    public void addTextDateStatus(View view, String interests) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_text_date_status, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.date_status)).setText(interests);
    }

    public void addDivider(View view) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_divider_horizontal, null, false);
        ((ViewGroup) view).addView(view1);
        ((View) view1.findViewById(R.id.viewonq)).setMinimumHeight(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, Util.getInstance(mactivity).dpToPixel(8), 0, Util.getInstance(mactivity).dpToPixel(8));
        view1.setLayoutParams(params);
    }

    public void addRatingHeader(View view, String text, double ratings) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_rating_header, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.header)).setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, Util.getInstance(mactivity).dpToPixel(16));
        view1.setLayoutParams(params);
    }

    public void loadReviews(View view, ArrayList<Review> reviewList) {
        for (int i = 0; i < reviewList.size(); i++) {
            View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_item_review, null, false);
            ((ViewGroup) view).addView(view1);
            Review review = reviewList.get(i);
            Util.getInstance(mactivity).setProfilePhotos(review.getPicUrl(), ((ImageView) view1.findViewById(R.id.iv_favourite_profile)));
            ((TextView) view1.findViewById(R.id.txt_favourite_name)).setText(review.getName());
            ((TextView) view1.findViewById(R.id.txt_favourite_Time)).setText(review.getTime());
            ((TextView) view1.findViewById(R.id.content)).setText(review.getContent());
        }
    }

    public View addBasicDetail(View view) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_basic_detail, null, false);
        ((ViewGroup) view).addView(view1);
        return view1;
    }

    public View addSearchProfileBar(View view) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_search_action_bar, null, false);
        ((ViewGroup) view).addView(view1);
        return view1;
    }

    public void addPaymentView(View view) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_payment_view, null, false);
        ((ViewGroup) view).addView(view1);
    }

    public void setBasicDetails(View zoomView, UserNew user, int profileType) {
        if (profileType == PROFILE_TYPE_SELF) {
            Util.getInstance(mactivity).setProfilePhotos(user.getProfilePicList().size() > 0 ? user.getProfilePicList().get(0).getLarge() : null, (ImageView) zoomView.findViewById(R.id.iv_zoom));
        } else {
            Util.getInstance(mactivity).setProfilePhotosSearch(user.getProfilePicList().size() > 0 ? user.getProfilePicList().get(0).getLarge() : null, (ImageView) zoomView.findViewById(R.id.iv_zoom));
        }
        ((TextView) zoomView.findViewById(R.id.name)).setText(user.getName());
        ((TextView) zoomView.findViewById(R.id.age)).setText(", " + user.getAge());
        ((ImageView) zoomView.findViewById(R.id.iv_online)).setVisibility(user.isOnline() ? View.VISIBLE : View.GONE);
    }

    public View setProfileDetails(View contentView, UserNew user, int profileType) {
        if (((ViewGroup) contentView).getChildCount() > 0)
            ((ViewGroup) contentView).removeAllViews();
        View searchActionsView = null;
        View basicDetail = ProfileUtil.getInstance(mactivity).addBasicDetail(contentView);
        if (profileType == PROFILE_TYPE_SEARCH || profileType == PROFILE_TYPE_HOLD || profileType == PROFILE_TYPE_PROPOSAL) {
            searchActionsView = ProfileUtil.getInstance(mactivity).addSearchProfileBar(contentView);
        } else if (profileType == PROFILE_TYPE_SELF) {
            ProfileUtil.getInstance(mactivity).addPaymentView(contentView);
            setPaymentText(contentView);
        }
        if (user.getOccupation().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.designation)).setText(user.getOccupation());
        else
            ((TextView) basicDetail.findViewById(R.id.designation)).setVisibility(View.GONE);
        if (user.getLikes() != 0)
            ((TextView) basicDetail.findViewById(R.id.like_count)).setText("" + user.getLikes());
        else
            ((FrameLayout) basicDetail.findViewById(R.id.flikes)).setVisibility(View.GONE);
        if (user.getDatePercentage().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.percentage)).setText(user.getDatePercentage() + "%");
        else
            ((LinearLayout) basicDetail.findViewById(R.id.llper)).setVisibility(View.GONE);
        if (user.getLocation().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.location)).setText(user.getLocation());
        else
            ((LinearLayout) basicDetail.findViewById(R.id.llloc)).setVisibility(View.GONE);
        if (user.getTagLine().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.tag_line)).setText(user.getTagLine());
        else
            ((TextView) basicDetail.findViewById(R.id.tag_line)).setVisibility(View.GONE);
        //Following code for set margin below with blank space

        if (user.getAboutMe().length() > 0) {
            addDivider(contentView);
            addHeader(contentView, "About Me");
            //addText(contentView, "My name is Randy Patterson, and I’m currently looking for a job in youth services. I have 10 years of experience working with youth agencies. I have a bachelor’s degree in outdoor education. I raise money, train leaders, and organize units.");
            addText(contentView, user.getAboutMe());
        }
        if (user.getIdealDate().length() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Ideal Date");
            //addText(contentView, "My name is Randy Patterson, and I’m currently looking for a job in youth services. I have 10 years of experience working with youth agencies. I have a bachelor’s degree in outdoor education. I raise money, train leaders, and organize units.");
            addText(contentView, user.getIdealDate());
        }

        if (user.getInterestList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Interests");
            loadGrid(contentView, user.getInterestList());
        }
        if (user.getRatingList().size() > 0) {
            addDivider(contentView);
            addRatingHeader(contentView, "Ratings", user.getTotalRating());
            loadRatings(contentView, user.getRatingList());
        }
        if (user.getReviewList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Reviews");
            loadReviews(contentView, user.getReviewList());
        }
        if (user.getPersonalDetailList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Personal Details");
            loadGrid(contentView, user.getPersonalDetailList());
        }
        return searchActionsView;
    }

    public View[] setProfileDetails1(View contentView, UserNew user, int profileType) {
        View[] views = new View[2];
        if (((ViewGroup) contentView).getChildCount() > 0)
            ((ViewGroup) contentView).removeAllViews();
        View searchActionsView = null;
        View basicDetail = ProfileUtil.getInstance(mactivity).addBasicDetail(contentView);
        if (profileType == PROFILE_TYPE_SEARCH || profileType == PROFILE_TYPE_HOLD || profileType == PROFILE_TYPE_PROPOSAL) {
            searchActionsView = ProfileUtil.getInstance(mactivity).addSearchProfileBar(contentView);
        } else if (profileType == PROFILE_TYPE_SELF) {
            ProfileUtil.getInstance(mactivity).addPaymentView(contentView);
            setPaymentText(contentView);
        } else if (profileType == PROFILE_TYPE_DATE) {
            searchActionsView = ProfileUtil.getInstance(mactivity).addSearchProfileBar(contentView);
            setDateStatusORActions(contentView, user);
        }
        if (user.getOccupation().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.designation)).setText(user.getOccupation());
        else
            ((TextView) basicDetail.findViewById(R.id.designation)).setVisibility(View.GONE);
        if (user.getLikes() != 0)
            ((TextView) basicDetail.findViewById(R.id.like_count)).setText("" + user.getLikes());
        else
            ((FrameLayout) basicDetail.findViewById(R.id.flikes)).setVisibility(View.GONE);
        if (user.getDatePercentage().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.percentage)).setText(user.getDatePercentage() + "%");
        else
            ((LinearLayout) basicDetail.findViewById(R.id.llper)).setVisibility(View.GONE);
        if (user.getLocation().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.location)).setText(user.getLocation());
        else
            ((LinearLayout) basicDetail.findViewById(R.id.llloc)).setVisibility(View.GONE);
        if (user.getTagLine().length() != 0)
            ((TextView) basicDetail.findViewById(R.id.tag_line)).setText(user.getTagLine());
        else
            ((TextView) basicDetail.findViewById(R.id.tag_line)).setVisibility(View.GONE);
        //Following code for set margin below with blank space
        /*if (user.getOccupation().length() == 0){
            TextView textView = (TextView) contentView.findViewById(R.id.designation);
            textView.measure(0, 0);
            textView.getMeasuredHeight();
            LinearLayout.LayoutParams params=new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0,textView.getMeasuredHeight());
            ((TextView) contentView.findViewById(R.id.tag_line)).setLayoutParams(params);
        }*/
        if (user.getAboutMe().length() > 0) {
            addDivider(contentView);
            addHeader(contentView, "About Me");
            //addText(contentView, "My name is Randy Patterson, and I’m currently looking for a job in youth services. I have 10 years of experience working with youth agencies. I have a bachelor’s degree in outdoor education. I raise money, train leaders, and organize units.");
            addText(contentView, user.getAboutMe());
        }
        if (user.getIdealDate().length() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Ideal Date");
            //addText(contentView, "My name is Randy Patterson, and I’m currently looking for a job in youth services. I have 10 years of experience working with youth agencies. I have a bachelor’s degree in outdoor education. I raise money, train leaders, and organize units.");
            addText(contentView, user.getIdealDate());
        }

        if (user.getInterestList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Interests");
            loadGrid(contentView, user.getInterestList());
        }
        if (user.getRatingList().size() > 0) {
            addDivider(contentView);
            addRatingHeader(contentView, "Ratings", user.getTotalRating());
            loadRatings(contentView, user.getRatingList());
        }
        if (user.getReviewList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Reviews");
            loadReviews(contentView, user.getReviewList());
        }
        if (user.getPersonalDetailList().size() > 0) {
            addDivider(contentView);
            addHeader(contentView, "Personal Details");
            loadGrid(contentView, user.getPersonalDetailList());
        }
        views[0] = basicDetail;
        views[1] = searchActionsView;
        return views;
    }

    private void setDateStatusORActions(View contentView, UserNew user) {
        switch (user.getActionInfo()) {
            case DATE_STATUS_BLOCK:
                ProfileUtil.getInstance(mactivity).addTextDateStatus(contentView, PROFILE_YOU_DECLINED);
                break;
            case DATE_STATUS_BLOCKED_BY:
                ProfileUtil.getInstance(mactivity).addTextDateStatus(contentView, PROFILE_DECLINED);
                break;
            /*case DATE_STATUS_PENDING:
                //like,Accept,Declined
                break;
            case DATE_STATUS_REQUESTED:

                break;
            case DATE_STATUS_HOLD:

                break;
            case DATE_STATUS_DATE_REQUEST:

                break;
            case DATE_STATUS_NO_RELATION:

                break;*/
        }

    }

    private void setPaymentText(View contentView) {
        String fontStartBlue = "<font color=#4689F4>";
        String fontStartRed = "<font color=#dc4a3d>";
        String fontEnd = "</font>";
        StringBuilder headerText = new StringBuilder("");
        headerText.append("Kindly enjoy ").append(fontStartRed + "Free" + fontEnd).append(" subscription during ").append(fontStartBlue + "Promotional" + fontEnd).append(" period");
        ((TextView) contentView.findViewById(R.id.txtpayment)).setText(Html.fromHtml(headerText.toString()));
    }

    public ArrayList<ProfileField> setPersonalDetailsItem(ArrayList<ProfileField> list, JSONObject jObject) {
        boolean isFieldAvailable = false;
        try {
            for (int i = 0; i < list.size(); i++) {
                ProfileField field = list.get(i);
                for (int j = 0; j < personalDeatialFields.length; j++) {
                    if (jObject.has(personalDeatialFields[j]) && field.getKey().equalsIgnoreCase(personalDeatialFields[j])) {
                        if (!personalDeatialFields[j].equals(KEY_KNOWNLANGUAGE)) {
                            String value = String.valueOf(jObject.get(personalDeatialFields[j]));
                            field.setValue(value.length() > 0 ? (!personalDeatialFields[j].equals(KEY_WEIGHT) ? value : value + " Kg") : NO_VALUE);
                        } else {//for KNOWNLANGUAGE
                            JSONArray array = jObject.getJSONArray(personalDeatialFields[j]);
                            field.setValue(array.length() > 0 ? Util.getInstance(mactivity).convertJsonArrayToString(jObject.getJSONArray(personalDeatialFields[j])) : NO_VALUE);
                        }
                        list.set(i, field);
                        isFieldAvailable = true;
                        break;
                    }
                }
            }
            if (!isFieldAvailable) {
                Iterator<String> iterator = jObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    ProfileField interest = new ProfileField();
                    String value = key.toLowerCase().replace(' ', '_');
                    interest.setIcon(Util.getInstance(mactivity).getImagePath("ic_profile_" + value));
                    if (key.equals(KEY_KNOWNLANGUAGE)) {// for KNOWNLANGUAGE
                        JSONArray array = jObject.getJSONArray(KEY_KNOWNLANGUAGE);
                        interest.setValue(array.length() > 0 ? Util.getInstance(mactivity).convertJsonArrayToString(jObject.getJSONArray(KEY_KNOWNLANGUAGE)) : NO_VALUE);
                    } else {
                        String value1 = String.valueOf(jObject.get(key));
                        interest.setValue(value1.length() > 0 ? (!key.equals(KEY_WEIGHT) ? value1 : value1 + " Kg") : NO_VALUE);
                    }
                    //interest.setValue(String.valueOf(jObject.get(key)));
                    interest.setKey(key);
                    interest.setOrderIndex(getOrderIndex(key));
                    list.add(interest);
                }
            }
            Collections.sort(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void increaseLikeCount(View contentView, ImageView like, ImageView likeSticky, UserNew dateUser) {
        FrameLayout flikes = (FrameLayout) contentView.findViewById(R.id.flikes);
        TextView like_count = (TextView) contentView.findViewById(R.id.like_count);
        flikes.setVisibility(View.VISIBLE);
        int likeCount = ((like_count.getText().length() != 0 ? Integer.parseInt(like_count.getText().toString()) : 0) + 1);
        like_count.setText("" + likeCount);
        like.setClickable(false);
        likeSticky.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        likeSticky.setClickable(false);
        like.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        if (!dateUser.isYouLiked()) {
            dateUser.setYouLiked(true);
            dateUser.setLikes(likeCount);
        }
    }

    public void increaseLikeCount1(View contentView, ImageView like, ImageView likeSticky, ArrayList<UserNew> list, int position) {
        FrameLayout flikes = (FrameLayout) contentView.findViewById(R.id.flikes);
        TextView like_count = (TextView) contentView.findViewById(R.id.like_count);
        flikes.setVisibility(View.VISIBLE);
        int likeCount = ((like_count.getText().length() != 0 ? Integer.parseInt(like_count.getText().toString()) : 0) + 1);
        like_count.setText("" + likeCount);
        like.setClickable(false);
        likeSticky.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        likeSticky.setClickable(false);
        like.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        if (!list.get(position).isYouLiked()) {
            list.get(position).setYouLiked(true);
            list.get(position).setLikes(likeCount);
        }
    }

    public void setProfileImageSizeWithTab(PullToZoomScrollViewEx scrollView) {
        int[] mScreenSize = Util.getInstance(mactivity).setLayoutParams(0, 0.60);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenSize[0], mScreenSize[1]);
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setParallax(false);
        scrollView.setScrollBarSize(100);
    }

    public void setProfileImageSizeWithoutTab(PullToZoomScrollViewEx scrollView) {
        int[] mScreenSize = Util.getInstance(mactivity).setLayoutParams(0, 0.70);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenSize[0], mScreenSize[1]);
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setParallax(false);
        scrollView.setScrollBarSize(100);
    }

    public void nextProfileAnimation(PullToZoomScrollViewEx scrollView) {
        Animation animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_profile);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollView.startAnimation(animation2);
    }

    public void previousProfileAnimation(PullToZoomScrollViewEx scrollView) {
        Animation animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_profile);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollView.startAnimation(animation2);
    }

    public void slideDownAnimation(PullToZoomScrollViewEx scrollView) {
        mactivity.overridePendingTransition(R.anim.slide_up,
                R.anim.slide_down);
    }

    //Update Profile Progress
    public void updateProfileProgress(UserNew user) {
        int filledFieldcount = 0;
        if (user.getProfilePicList().get(0).getLarge().length() > 0)
            filledFieldcount = filledFieldcount + 1;
        if (user.getProfileAudio().length() > 0)
            filledFieldcount = filledFieldcount + 1;
        if (user.getProfileVideo().length() > 0)
            filledFieldcount = filledFieldcount + 1;
        if (user.getTagLine().length() > 0 && !user.getTagLine().equals(NO_VALUE))
            filledFieldcount = filledFieldcount + 1;
        if (user.getAboutMe().length() > 0 && !user.getAboutMe().equals(NO_VALUE))
            filledFieldcount = filledFieldcount + 1;
        if (user.getIdealDate().length() > 0 && !user.getIdealDate().equals(NO_VALUE))
            filledFieldcount = filledFieldcount + 1;
        if (user.getInterestList().size() > 0)
            filledFieldcount = filledFieldcount + 1;
        if (user.getOccupation().length() > 0 && !user.getOccupation().equals(NO_VALUE))
            filledFieldcount = filledFieldcount + 1;

        ArrayList<ProfileField> list = user.getPersonalDetailList();
        for (int i = 0; i < list.size(); i++) {
            ProfileField field = list.get(i);
            if (field.getKey().equals(KEY_HEIGHT) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_HAIRCOLOR) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_WEIGHT) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_SUNSIGN) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_COMPLEXION) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_DIET) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_BODYTYPE) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_SMOKING) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_EYECOLOR) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_DRINKING) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
            if (field.getKey().equals(KEY_KNOWNLANGUAGE) && !field.getValue().equals(NO_VALUE))
                filledFieldcount = filledFieldcount + 1;
        }


        PrefsManager.setProfileProgress((100 * filledFieldcount) / Const.totalProfileField);
    }

    //TODO:         "Sticky header"     variables
    public void initLike(UserNew searchUser, ImageView like) {
        if (searchUser.isYouLiked()) {
            like.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        } else {
            like.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like));
            like.setOnClickListener((View.OnClickListener) mactivity);
        }
    }

    //TODO:         "Sticky header"     variables
    public int getInitialZoomHeight() {
        int topMarginPx = Util.getInstance(mactivity).getStatusBarHeight() /*+ Util.getInstance(ProfileSearchActivity.this).getActionBarHeight()*/;
        return -(topMarginPx - Util.getInstance(mactivity).dpToPixel(32));
    }

    //TODO:         "Sticky header"     variables
    public void initLikeSticky(UserNew searchUser, ImageView likeSticky) {
        if (searchUser.isYouLiked()) {
            likeSticky.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like_disable));
        } else {
            likeSticky.setImageDrawable(mactivity.getResources().getDrawable(R.drawable.ic_profile_like));
            likeSticky.setOnClickListener((View.OnClickListener) mactivity);
        }
    }

    //TODO:     "DateInfo"
    public void setDateInfoIcon(ImageView dateInfoIcon, String dateType) {
//        int idDateType = mactivity.getResources().getIdentifier("ic_date_" +dateType.toLowerCase(), "drawable", mactivity.getPackageName());
        dateInfoIcon.setImageResource(DateUtil.getInstance(mactivity).setDateTypeIcon(dateType));
    }

    public void setMediaViewCount(UserNew user, JSONObject success) {
        try {
            JSONObject data = success.getJSONObject(KEY_DATA);
            String mediaType = data.getString(KEY_TYPE);
            switch (mediaType){
                case MEDIA_VIDEO:
                    user.setVideoViewCount(data.getInt(KEY_VIEW_COUNT));
                    break;
                case MEDIA_AUDIO:
                    user.setAudioViewCount(data.getInt(KEY_VIEW_COUNT));
                    break;
            }
            Util.getInstance(mactivity).showLog("VideoViewCount..."+user.getVideoViewCount()+"..AudioViewCount..."+user.getAudioViewCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
