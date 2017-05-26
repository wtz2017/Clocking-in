package com.test.clocking_in.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.test.clocking_in.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickDialogUtil implements OnDateChangedListener, OnTimeChangedListener {
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private AlertDialog mDialog;
    private EditText etInputDateTime;
    private String mDateTime;

    public static String getCurrentDateTime(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                context.getString(R.string.default_date_time_format));
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param editText 需要设置日期时间的文本编辑框
     * @return
     */
    public AlertDialog createDialog(final EditText editText) {
        etInputDateTime = editText;
        Context context = editText.getContext();
        LinearLayout dateTimeLayout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.date_time_picker_layout, null);
        mDatePicker = (DatePicker) dateTimeLayout.findViewById(R.id.date_picker);
        mTimePicker = (TimePicker) dateTimeLayout.findViewById(R.id.time_picker);

        Calendar calendar = getDefaultDateTimeCalendar(editText);
        mDatePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        mTimePicker.setIs24HourView(true);
        mTimePicker.setOnTimeChangedListener(this);

        mDialog = new AlertDialog.Builder(editText.getContext())
                .setTitle(DateTimeUtil.getSpecifiedDateTime(calendar.getTime(), context.getString(R.string.default_date_time_format)))
                .setView(dateTimeLayout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editText.setText(mDateTime);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                }).show();

        onDateChanged(null, 0, 0, 0);
        return mDialog;
    }

    private Calendar getDefaultDateTimeCalendar(EditText inputDate) {
        Calendar calendar = Calendar.getInstance();
        if (inputDate != null && !TextUtils.isEmpty(inputDate.getText())) {
            String deaufltDateTime = String.valueOf(inputDate.getText());
            Date defaultDate = DateTimeUtil.changeStringToDate(deaufltDateTime,
                    inputDate.getContext().getString(R.string.default_date_time_format));
            calendar.setTime(defaultDate);
        }

        return calendar;
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        onDateTimeChanged();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateTimeChanged();
    }

    private void onDateTimeChanged() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),
                mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(),
                mTimePicker.getCurrentMinute());

        SimpleDateFormat sdf = new SimpleDateFormat(etInputDateTime.getResources().getString(R.string.default_date_time_format));
        mDateTime = sdf.format(calendar.getTime());
        mDialog.setTitle(mDateTime);
    }

}
