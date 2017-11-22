package com.emptyproject.util;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datecoy.R;
import com.datecoy.model.DateSearch;
import com.datecoy.model.MyDate;
import com.datecoy.mydatesearch.DateHistoryActivity;
import com.datecoy.mydatesearch.DateProfileActivity;
import com.datecoy.webapi.WebUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by INFYZO\shailesh.pateliya on 30/6/17.
 */

public class DateUtil implements WebUtil {
    public static DateUtil dateUtil;
    public static Activity mactivity;
    TextView txtName, txtAge, txtDateType, txtHourDuration, txtPaidType, txtDateTime, txtDateDay, txtDatePlaceName, txtDatePlaceArea;
    ImageView ivDateBG, ivFriend, ivDateLeftCircle, ivDateType, ivDateRightCircle, ivPaidType, ivDateIcon, ivDateLocation, ivDateInfo, ivDateProfile;
    FrameLayout flDateBG, flDateDisableBG;
    DateSearch dateSearch;

    public static DateUtil getInstance(Activity activity) {
        mactivity = activity;
        if (dateUtil == null)
            dateUtil = new DateUtil();
        return dateUtil;
    }

    public MyDate setCreatedDateBackGround(MyDate myDateCreated, int index) {
        if (index % 4 == 0) {
            myDateCreated.setLineBGSelectedColor(mactivity.getResources().getColor(R.color.colorPrimary_red));
            myDateCreated.setLineTriangleIcon("ic_mydate_triangle_red");

            myDateCreated.setBackGroundColor(R.drawable.bg_date_card_red);
            myDateCreated.setLeftDateTypeIconName("ic_date_leftcircle_red");
            myDateCreated.setDateBackGround("ic_date_movie_bg");
        } else if (index % 4 == 1) {
            myDateCreated.setLineBGSelectedColor(mactivity.getResources().getColor(R.color.colorPrimary_blue));
            myDateCreated.setLineTriangleIcon("ic_mydate_triangle_blue");

            myDateCreated.setBackGroundColor(R.drawable.bg_date_card_blue);
            myDateCreated.setLeftDateTypeIconName("ic_date_leftcircle_blue");
            myDateCreated.setDateBackGround("ic_date_pub_bg");
        } else if (index % 4 == 2) {
            myDateCreated.setLineBGSelectedColor(mactivity.getResources().getColor(R.color.colorPrimary_purple));
            myDateCreated.setLineTriangleIcon("ic_mydate_triangle_purple");

            myDateCreated.setBackGroundColor(R.drawable.bg_date_card_purple);
            myDateCreated.setLeftDateTypeIconName("ic_date_leftcircle_purpal");
            myDateCreated.setDateBackGround("ic_date_restaurants_bg");
        } else if (index % 4 == 3) {
            myDateCreated.setLineBGSelectedColor(mactivity.getResources().getColor(R.color.colorPrimary_green));
            myDateCreated.setLineTriangleIcon("ic_mydate_triangle_green");

            myDateCreated.setBackGroundColor(R.drawable.bg_date_card_green);
            myDateCreated.setLeftDateTypeIconName("ic_date_leftcircle_green");
            myDateCreated.setDateBackGround("ic_date_cafe_bg");
        }
        return myDateCreated;
    }

    public DateSearch setDateBackGround(DateSearch dateSearch, int index) {
        if (dateSearch.getType().equalsIgnoreCase(Const.DATETYPE_MOVIE)) {
            dateSearch.setDateBackGround("ic_date_movie_bg");
        } else if (dateSearch.getType().equalsIgnoreCase(Const.DATETYPE_COFFEE)) {
            dateSearch.setDateBackGround("ic_date_cafe_bg");
        } else if (dateSearch.getType().equalsIgnoreCase(Const.DATETYPE_PUB)) {
            dateSearch.setDateBackGround("ic_date_pub_bg");
        } else if (dateSearch.getType().equalsIgnoreCase(Const.DATETYPE_LUNCH)) {
            dateSearch.setDateBackGround("ic_date_restaurants_bg");
        } else if (dateSearch.getType().equalsIgnoreCase(Const.DATETYPE_DINNER)) {
            dateSearch.setDateBackGround("ic_date_restaurants_bg");
        }
        if (index % 4 == 0) {
            dateSearch.setBackGroundColor(R.drawable.bg_date_card_red);
            dateSearch.setLeftDateTypeIconName("ic_date_leftcircle_red");
            dateSearch.setDateStatusIcon("ic_dateh_" + dateSearch.getDateStatus().toLowerCase() + "_red");
            dateSearch.setDateStatusTextColor(mactivity.getResources().getColor(R.color.red));
        } else if (index % 4 == 1) {
            dateSearch.setBackGroundColor(R.drawable.bg_date_card_blue);
            dateSearch.setLeftDateTypeIconName("ic_date_leftcircle_blue");
            dateSearch.setDateStatusIcon("ic_dateh_" + dateSearch.getDateStatus().toLowerCase() + "_blue");
            dateSearch.setDateStatusTextColor(mactivity.getResources().getColor(R.color.blue));
        } else if (index % 4 == 2) {
            dateSearch.setBackGroundColor(R.drawable.bg_date_card_purple);
            dateSearch.setLeftDateTypeIconName("ic_date_leftcircle_purpal");
            dateSearch.setDateStatusIcon("ic_dateh_" + dateSearch.getDateStatus().toLowerCase() + "_purple");
            dateSearch.setDateStatusTextColor(mactivity.getResources().getColor(R.color.purple));
        } else if (index % 4 == 3) {
            dateSearch.setBackGroundColor(R.drawable.bg_date_card_green);
            dateSearch.setLeftDateTypeIconName("ic_date_leftcircle_green");
            dateSearch.setDateStatusIcon("ic_dateh_" + dateSearch.getDateStatus().toLowerCase() + "_green");
            dateSearch.setDateStatusTextColor(mactivity.getResources().getColor(R.color.green));
        }
        dateSearch = setPaidByIconData(dateSearch);

        return dateSearch;
    }

    public DateSearch setPaidByIconData(DateSearch dateSearch) {
        if (dateSearch.getPaidBy().equalsIgnoreCase(Const.PAIDBY_YOU) || dateSearch.getPaidBy().equalsIgnoreCase(Const.PAIDBY_ME)) {
            dateSearch.setPaidByIcon("ic_date_paid_by_you");
        } else if (dateSearch.getPaidBy().equalsIgnoreCase(Const.PAIDBY_50)) {
            dateSearch.setPaidByIcon("ic_date_paid_by_50_50");
        }
        return dateSearch;
    }

    public static ArrayList<DateSearch> setDateCardRespone(JSONObject response) {
        ArrayList<DateSearch> dateCardList = new ArrayList<>();
        try {
            JSONArray jDatas = response.getJSONArray(KEY_DATA);
            for (int i = 0; i < jDatas.length(); i++) {
                JSONObject jData = jDatas.getJSONObject(i);
                dateCardList.add(getDateCardObject(jData, i));
            }

        } catch (Exception e) {
            Util.getInstance(mactivity).showLog(e.toString());
        }
        return dateCardList;
    }

    public static DateSearch getDateCardObject(JSONObject jData, int i) {
        DateSearch dateSearch = new DateSearch();
        try {
            if (jData.has(KEY_ID))
                dateSearch.setDateId(jData.getString(KEY_ID));
            if (jData.has(KEY_CHAT_TO))
                dateSearch.setId(jData.getString(KEY_CHAT_TO));
            if (jData.has(KEY_DATE_USERNAME))
                dateSearch.setFname(Util.getInstance(mactivity).setFirstLatterCaps(jData.getString(KEY_DATE_USERNAME)));
            if (jData.has(KEY_AGE))
                dateSearch.setAge(jData.getString(KEY_AGE));
            if (jData.has(KEY_DATE_AREA))
                dateSearch.setArea(jData.getString(KEY_DATE_AREA));
            if (jData.has(KEY_TYPE))
                dateSearch.setType(jData.getString(KEY_TYPE).toUpperCase());
            if (jData.has(KEY_DATE_DURATION))
                dateSearch.setHours(jData.getString(KEY_DATE_DURATION));
            if (jData.has(KEY_DATE_PAIDBY))
                dateSearch.setPaidBy(jData.getString(KEY_DATE_PAIDBY));
            if (jData.has(KEY_DATE_DATENAME))
                dateSearch.setName(jData.getString(KEY_DATE_DATENAME));
            if (jData.has(KEY_DATE_VENUE))
                dateSearch.setVenue(jData.getString(KEY_DATE_VENUE));
            if (jData.has(KEY_DATE_ISFRIEND))
                dateSearch.setFriend(jData.getBoolean(KEY_DATE_ISFRIEND));
            if (jData.has(KEY_DATE_ISEXCLUSIVE))
                dateSearch.setExclusive(jData.getBoolean(KEY_DATE_ISEXCLUSIVE));
            if (jData.has(KEY_STARTTIME)) {
                dateSearch.setDateTime(Util.getInstance(mactivity).convertUtcToCurrentTimeZone(jData.getString(KEY_STARTTIME), 2));
                dateSearch.setDays(Util.getInstance(mactivity).convertUtcToCurrentTimeZone(jData.getString(KEY_STARTTIME), 6));
                dateSearch.setEditDateTime(jData.getLong(KEY_STARTTIME));
            }
            if (jData.has(KEY_PROFILEPICS)) {
                //profilePics
                JSONArray jProfilePics = jData.getJSONArray(KEY_PROFILEPICS);
                dateSearch.setProfilePic(ProfileUtil.getInstance(mactivity).getFirstProfilePic(jProfilePics).getLarge());
            }
            if (jData.has(KEY_DATE_SHOW_BUTTON))
                dateSearch.setShowButton(jData.getString(KEY_DATE_SHOW_BUTTON));
            if (jData.has(KEY_DATE_FRIENDSHIPRELATION))
                dateSearch.setFriendShipRelation(jData.getString(KEY_DATE_FRIENDSHIPRELATION));
            if (jData.has(KEY_DATE_EDIT_INFO))
                dateSearch.setEditedInfo(jData.getJSONObject(KEY_DATE_EDIT_INFO).toString());
            if (jData.has(KEY_DATE_STATUS)) {
                dateSearch.setDateStatus(Util.getInstance(mactivity).setFirstLatterCaps(jData.getString(KEY_DATE_STATUS)));
            }
            if(jData.has(KEY_DATE_INFO)){
                dateSearch.setDateInfo(true);
            }
            dateSearch = DateUtil.getInstance(mactivity).setDateBackGround(dateSearch, i);
            dateSearch.setLongPress(false);
        } catch (Exception e) {

        }
        return dateSearch;

    }

    public View addCard(View view, DateSearch dateSearchPara, int type) {
        dateSearch = dateSearchPara;
        View itemView = LayoutInflater.from(mactivity).inflate(R.layout.activity_date_card_common, null, false);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
        txtAge = (TextView) itemView.findViewById(R.id.txtAge);
        txtDateType = (TextView) itemView.findViewById(R.id.txtDateType);
        txtHourDuration = (TextView) itemView.findViewById(R.id.txtHourDuration);
        txtPaidType = (TextView) itemView.findViewById(R.id.txtPaidType);
        txtDateTime = (TextView) itemView.findViewById(R.id.txtDateTime);
        txtDateDay = (TextView) itemView.findViewById(R.id.txtDateDay);
        txtDatePlaceName = (TextView) itemView.findViewById(R.id.txtDatePlaceName);
        txtDatePlaceArea = (TextView) itemView.findViewById(R.id.txtDatePlaceArea);

        ivDateProfile = (ImageView) itemView.findViewById(R.id.ivDateProfile);
        ivDateBG = (ImageView) itemView.findViewById(R.id.ivDateBG);
        ivFriend = (ImageView) itemView.findViewById(R.id.ivFriend);
        ivDateLeftCircle = (ImageView) itemView.findViewById(R.id.ivDateLeftCircle);
        ivDateType = (ImageView) itemView.findViewById(R.id.ivDateType);
        ivDateRightCircle = (ImageView) itemView.findViewById(R.id.ivDateRightCircle);
        ivPaidType = (ImageView) itemView.findViewById(R.id.ivPaidType);
        ivDateIcon = (ImageView) itemView.findViewById(R.id.ivDateIcon);
        ivDateLocation = (ImageView) itemView.findViewById(R.id.ivDateLocation);
        ivDateInfo = (ImageView) itemView.findViewById(R.id.ivDateInfo);

        flDateBG = (FrameLayout) itemView.findViewById(R.id.flDateBG);
        flDateDisableBG = (FrameLayout) itemView.findViewById(R.id.flDateDisableBG);


        dateSearch = setPaidByIconData(dateSearch);

        Util.getInstance(mactivity).setProfilePhotosSearch(dateSearch.getProfilePic(), ivDateProfile);
        flDateBG.setBackgroundResource(dateSearch.getBackGroundColor());
        ivPaidType.setImageResource(setPaidTypeIcon(dateSearch));
        ivDateType.setImageResource(setDateTypeIcon(dateSearch));
        ivDateLeftCircle.setImageResource(setLeftIcon(dateSearch));
        ivDateBG.setImageResource(setDateBG(dateSearch));


        if (type == 1) {
            txtName.setText(dateSearch.getFname() + ",");
            txtAge.setText(dateSearch.getAge());
            txtDateType.setText(dateSearch.getType());
            txtHourDuration.setText(dateSearch.getHours());
            txtPaidType.setText(dateSearch.getPaidBy());
            txtDateTime.setText("@" + dateSearch.getDateTime() + ":");
            txtDateDay.setText(dateSearch.getDays());
            if (dateSearch.getName().length() > 0) {
                txtDatePlaceName.setText(dateSearch.getName() + ", " + dateSearch.getVenue());
            } else {
                txtDatePlaceName.setText(dateSearch.getVenue());
            }
            txtDatePlaceArea.setText(dateSearch.getArea());
            flDateDisableBG.setVisibility(View.VISIBLE);

        } else {
            flDateDisableBG.setVisibility(View.GONE);
            String editInfo = dateSearch.getEditedInfo();
            Util.getInstance(mactivity).showLog("editInfo..." + editInfo);
            try {
                JSONObject jData = new JSONObject(editInfo);
                DateSearch dateSearchEdit = new DateSearch();

                txtName.setText(dateSearch.getFname() + ",");
                txtAge.setText(dateSearch.getAge());

                if (jData.has(KEY_STARTTIME)) {
                    dateSearchEdit.setDateTime(Util.getInstance(mactivity).convertUtcToCurrentTimeZone(jData.getString(KEY_STARTTIME), 2));
                    dateSearchEdit.setDays(Util.getInstance(mactivity).convertUtcToCurrentTimeZone(jData.getString(KEY_STARTTIME), 6));
                    dateSearchEdit.setEditDateTime(jData.getLong(KEY_STARTTIME));
                    txtDateTime.setText("@" + dateSearchEdit.getDateTime() + ":");
                    txtDateDay.setText(dateSearchEdit.getDays());
                } else {
                    txtDateTime.setText("@" + dateSearch.getDateTime() + ":");
                    txtDateDay.setText(dateSearch.getDays());
                }
                if (jData.has(KEY_DATE_AREA)) {
                    dateSearchEdit.setArea(jData.getString(KEY_DATE_AREA));
                    txtDatePlaceArea.setText(dateSearchEdit.getArea());
                } else {
                    txtDatePlaceArea.setText(dateSearch.getArea());
                }
                if (jData.has(KEY_TYPE)) {
                    dateSearchEdit.setType(jData.getString(KEY_TYPE).toUpperCase());
                    txtDateType.setText(dateSearchEdit.getType());
                    ivDateType.setImageResource(setDateTypeIcon(dateSearchEdit));
                    dateSearchEdit = setDateBackGround(dateSearchEdit, 0);
                    ivDateBG.setImageResource(setDateBG(dateSearchEdit));
                } else {
                    txtDateType.setText(dateSearch.getType());
                }
                if (jData.has(KEY_DATE_DURATION)) {
                    dateSearchEdit.setHours(jData.getString(KEY_DATE_DURATION));
                    txtHourDuration.setText(dateSearchEdit.getHours());
                } else {
                    txtHourDuration.setText(dateSearch.getHours());
                }
                if (jData.has(KEY_DATE_PAIDBY)) {
                    dateSearchEdit.setPaidBy(jData.getString(KEY_DATE_PAIDBY));
                    txtPaidType.setText(dateSearchEdit.getPaidBy());
                    dateSearchEdit = setPaidByIconData(dateSearchEdit);
                    ivPaidType.setImageResource(setPaidTypeIcon(dateSearchEdit));
                } else {
                    txtPaidType.setText(dateSearch.getPaidBy());
                }
                if (jData.has(KEY_NAME)) {
                    dateSearchEdit.setName(jData.getString(KEY_NAME));
//                    txtDatePlaceName.setText(dateSearchEdit.getName());
                } else {
//                    txtDatePlaceName.setText(dateSearch.getName());
                }
                if (jData.has(KEY_DATE_VENUE)) {
                    dateSearchEdit.setVenue(jData.getString(KEY_DATE_VENUE));
                    if (dateSearchEdit.getName().length() > 0) {
                        txtDatePlaceName.setText(dateSearchEdit.getName() + ", " + dateSearchEdit.getVenue());
                    } else {
                        txtDatePlaceName.setText(dateSearchEdit.getVenue());
                    }
                } else {
                    if (dateSearch.getName().length() > 0) {
                        txtDatePlaceName.setText(dateSearch.getName() + ", " + dateSearch.getVenue());
                    } else {
                        txtDatePlaceName.setText(dateSearch.getVenue());
                    }
                }
                /*if (dateSearch.getName().length()>0){
                    txtDatePlaceName.setText(dateSearch.getName()+", "+dateSearch.getVenue());
                }else {
                    txtDatePlaceName.setText(dateSearch.getVenue());
                }*/
            } catch (Exception e) {
                Util.getInstance(mactivity).showLog("Edit..." + e.toString());
            }
        }


        if (!dateSearch.isFriend() && !dateSearch.isExclusive()) {
            ivFriend.setVisibility(View.INVISIBLE);
        } else {
            ivFriend.setVisibility(View.VISIBLE);
            if (dateSearch.isFriend()) {
                ivFriend.setImageResource(R.drawable.ic_date_friends);
            } else if (dateSearch.isExclusive()) {
                ivFriend.setImageResource(R.drawable.ic_date_exclusive);
            }
        }

        if (dateSearch.getFriendShipRelation().length() > 0) {
            ivDateInfo.setVisibility(View.VISIBLE);
            ivDateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    bottomToolTipDialogBox(v, dateSearch.getFriendShipRelation());
                }
            });
        } else {
            ivDateInfo.setVisibility(View.INVISIBLE);
        }
        ivDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mactivity, DateHistoryActivity.class);
                intent.putExtra(KEY_ID, dateSearch.getId());
                intent.putExtra(KEY_NAME, dateSearch.getFname());
                mactivity.startActivity(intent);
            }
        });
        ivDateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mactivity, DateProfileActivity.class);
                intent.putExtra(KEY_CHAT_TO, dateSearch.getId());
                intent.putExtra(KEY_DATE_ID, dateSearch.getDateId());
                Util.getInstance(mactivity).nextActivity(intent);
                /*if (type == Const.DATE_AVAILABLE_TYPE) {
                } else if (type == Const.DATE_EXCLUSIVE_TYPE) {
                } else if (type == Const.DATE_REQUESTED_TYPE) {

                } else if (type == Const.DATE_CONFIRMED_TYPE) {
                    intent.putExtra(KEY_DATE_FROMSCREEN, DATE_STATUS_NO_ACTION);

                    mActivity.startActivityForResult(intent, 123);
                }*/

            }
        });
        ((ViewGroup) view).addView(itemView);
        return itemView;
    }

    // TODO  Date Card Left Side Icon Path
    public int setLeftIcon(DateSearch dateSearch) {
        int idLeftIcon = mactivity.getResources().getIdentifier(dateSearch.getLeftDateTypeIconName(), "drawable", mactivity.getPackageName());
        return idLeftIcon;
    }
    public int setLeftIcon(String leftIcon) {
        int idLeftIcon = mactivity.getResources().getIdentifier(leftIcon, "drawable", mactivity.getPackageName());
        return idLeftIcon;
    }
    // TODO  Date Card Background Path
    public int setDateBG(DateSearch dateSearch) {
        int idDateBg = mactivity.getResources().getIdentifier(dateSearch.getDateBackGround(), "drawable", mactivity.getPackageName());
        return idDateBg;
    }
    public int setDateBG(String dateBackground) {
        int idDateBg = mactivity.getResources().getIdentifier(dateBackground, "drawable", mactivity.getPackageName());
        return idDateBg;
    }
    // TODO  Date Card DateType Icon Path
    public int setDateTypeIcon(DateSearch dateSearch) {
        int idDateType = mactivity.getResources().getIdentifier("ic_date_" + dateSearch.getType().toLowerCase(), "drawable", mactivity.getPackageName());
        return idDateType;
    }
    public int setDateTypeIcon(String dateType) {
        int idDateType = mactivity.getResources().getIdentifier("ic_date_" + dateType.toLowerCase(), "drawable", mactivity.getPackageName());
        return idDateType;
    }
    // TODO  Date Card PaidByType Icon Path
    public int setPaidTypeIcon(DateSearch dateSearch) {
        int idPaidBy = mactivity.getResources().getIdentifier(dateSearch.getPaidByIcon(), "drawable", mactivity.getPackageName());
        return idPaidBy;
    }
    // TODO  Date History Date Status Icon Path
    public int setDateStatusIcon(DateSearch dateSearch) {
        int idDateStatus = mactivity.getResources().getIdentifier(dateSearch.getDateStatusIcon(), "drawable", mactivity.getPackageName());
        return idDateStatus;
    }
    // TODO  CreateDate Triangle Icon Path
    public int setLineTriangleIcon(String lineTriangleIcon) {
        int idLineIconBg = mactivity.getResources().getIdentifier(lineTriangleIcon, "drawable", mactivity.getPackageName());
        return idLineIconBg;
    }

    public void addHeader(View view, String text) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.profile_text_header, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.header)).setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, Util.getInstance(mactivity).dpToPixel(16));
        view1.setLayoutParams(params);
    }

    public View addAcceptButton(View view, String text) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.date_accept_button, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.txtAccept)).setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(80, 20, 80, 10);
//        params.setMargins(0, 0, 0, Util.getInstance(mactivity).dpToPixel(16));
        view1.setLayoutParams(params);
        return view1;
    }

    public View addDeclineButton(View view, String text) {
        View view1 = LayoutInflater.from(mactivity).inflate(R.layout.date_decline_button, null, false);
        ((ViewGroup) view).addView(view1);
        ((TextView) view1.findViewById(R.id.txtDecline)).setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(80, 20, 80, 10);
//        params.setMargins(0, 0, 0, Util.getInstance(mactivity).dpToPixel(16));
        view1.setLayoutParams(params);
        return view1;
    }
}
