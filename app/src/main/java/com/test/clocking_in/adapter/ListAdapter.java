package com.test.clocking_in.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.clocking_in.R;
import com.test.clocking_in.data.DateTimeRecord;

public class ListAdapter extends BaseAdapter {
    private final static String TAG = ListAdapter.class.getName();

    private Context mContext;

    private List<DateTimeRecord> mList;

    private LayoutParams itemLayoutParams = null;

    public ListAdapter(Context context, List<DateTimeRecord> list) {
        mContext = context;
        mList = list;
    }

    public void update(List<DateTimeRecord> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public List<DateTimeRecord> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mList == null) ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mContext) {
            Log.d(TAG, "getView...null == mContext");
            return null;
        }

        if (null == mList || mList.isEmpty()) {
            Log.d(TAG, "getView...list isEmpty");
            return null;
        }

        ViewHolder itemLayout = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record, null);
            itemLayout = new ViewHolder();
            itemLayout.tvDatetime = (TextView) convertView.findViewById(R.id.tv_date_time);
            itemLayout.tvWeekDay = (TextView) convertView.findViewById(R.id.tv_day_of_week);
            convertView.setTag(itemLayout);
        } else {
            itemLayout = (ViewHolder) convertView.getTag();
        }

        DateTimeRecord item = null;
        if ((item = mList.get(position)) != null) {
            if (item.isWeekSummary()) {
                convertView.setBackgroundColor(Color.parseColor("#C1FFC1"));
                itemLayout.tvDatetime.setText("第" + item.getWeeks() + "周");
                itemLayout.tvWeekDay.setText("工时：" + getHourMinute(item));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
                itemLayout.tvDatetime.setText(item.getDateTime() == null ? "" : item.getDateTime());
                itemLayout.tvWeekDay.setText("周" + getChineseWeekDay(item));
            }
        }

        return convertView;
    }

    private String getHourMinute(DateTimeRecord item) {
        int minute = item.getWeekWorkTime() / 1000 / 60;
        int hour = 0;
        if (minute >= 60 ) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return hour + "小时" + minute + "分钟";
    }

    private String getChineseWeekDay(DateTimeRecord item) {
        String dayString = String.valueOf(item.getWeekday());
        try {
            int day = item.getWeekday();
            switch (day) {
                case 1:
                    dayString = "一";
                    break;
                case 2:
                    dayString = "二";
                    break;
                case 3:
                    dayString = "三";
                    break;
                case 4:
                    dayString = "四";
                    break;
                case 5:
                    dayString = "五";
                    break;
                case 6:
                    dayString = "六";
                    break;
                case 7:
                    dayString = "日";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayString;
    }

    class ViewHolder {
        TextView tvDatetime;
        TextView tvWeekDay;
    }

}
