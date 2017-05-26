package com.test.clocking_in;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.test.clocking_in.adapter.ListAdapter;
import com.test.clocking_in.data.DateTimeRecord;
import com.test.clocking_in.utils.DatabaseHelper;
import com.test.clocking_in.utils.DateTimePickDialogUtil;
import com.test.clocking_in.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getName();

    private EditText etDateTime;
    private Button btnRecord;
    private ListView lvRecordList;

    private ListAdapter mListAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        etDateTime = (EditText) findViewById(R.id.et_set_date);
        etDateTime.setText(DateTimePickDialogUtil.getCurrentDateTime(MainActivity.this));
        etDateTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil();
                dateTimePicKDialog.createDialog(etDateTime);
            }
        });

        btnRecord = (Button) findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater lf = LayoutInflater.from(view.getContext());
                TextView tvInfo = (TextView) lf.inflate(R.layout.dialog_content, null);
                String info = (etDateTime != null && !TextUtils.isEmpty(etDateTime.getText())) ? etDateTime.getText().toString() : "";
                tvInfo.setText(info);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(MainActivity.this.getString(R.string.is_confirm_record))
                        .setView(tvInfo)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                saveRecord();
                                updateListView();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do nothing
                            }
                        }).show();
            }
        });

        lvRecordList = (ListView) findViewById(R.id.lv_list);
        lvRecordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater lf = LayoutInflater.from(view.getContext());
                TextView tvInfo = (TextView) lf.inflate(R.layout.dialog_content, null);
                tvInfo.setText(mListAdapter.getList().get(position).getDateTime());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(MainActivity.this.getString(R.string.is_confirm_delete_record))
                        .setView(tvInfo)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteRecord(mListAdapter.getList().get(position).getMilliseconds());
                                updateListView();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do nothing
                            }
                        }).show();
                return true;
            }
        });
        mListAdapter = new ListAdapter(MainActivity.this, null);
        lvRecordList.setAdapter(mListAdapter);
        updateListView();
    }

    private void updateListView() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(MainActivity.this);
        }
        List<DateTimeRecord> list =  mDatabaseHelper.queryList();
        if (list != null) {
            // TODO: 2016/12/18
            List<DateTimeRecord> summaryList = new ArrayList<DateTimeRecord>();
            int size = list.size();
            int weeks = list.get(0).getWeeks();
            int weekday = list.get(0).getWeekday();
            long millisecond = list.get(0).getMilliseconds();
            int dayWorkTime = 0;
            int weekWorkTime = 0;
            int weekSummaryIndex = 0;
            for (int i = 1; i < size; i++) {
                if (list.get(i).getWeeks() == weeks ) {
                    // 同一周
                    if (list.get(i).getWeekday() == weekday) {
                        // 同一天
                        dayWorkTime = Math.abs((int) (list.get(i).getMilliseconds() - millisecond));
                        Log.d(TAG, "the same week same day...start millisecond = " + millisecond
                                + ", end = " + list.get(i).getMilliseconds()
                                + ", dayWorkTime = " + dayWorkTime);
                    } else {
                        // 换新一天记录
                        weekWorkTime += dayWorkTime;
                        weekday = list.get(i).getWeekday();
                        millisecond = list.get(i).getMilliseconds();
                        dayWorkTime = 0;
                        Log.d(TAG, "the same week diffrent day...weekWorkTime = " + weekWorkTime
                                + ", new start = " + millisecond);
                    }
                } else {
                    // 换新一周记录
                    weekWorkTime += dayWorkTime;
                    addWeekSummary(summaryList, weeks, weekWorkTime, weekSummaryIndex);

                    weekSummaryIndex = i;
                    weeks = list.get(i).getWeeks();
                    weekday = list.get(i).getWeekday();
                    millisecond = list.get(i).getMilliseconds();
                    dayWorkTime = 0;
                    weekWorkTime = 0;
                }
            }
            weekWorkTime += dayWorkTime;
            addWeekSummary(summaryList, weeks, weekWorkTime, weekSummaryIndex);

            int count = summaryList.size();
            int index = 0;
            for (int i = 0; i < count; i++) {
                index = summaryList.get(i).getWeekSummaryIndex() + i;
                list.add(index, summaryList.get(i));
            }
        }

        if (mListAdapter != null) {
            mListAdapter.update(list);
        }
    }

    private void addWeekSummary(List<DateTimeRecord> summaryList, int weeks, int weekWorkTime, int weekSummaryIndex) {
        DateTimeRecord record = new DateTimeRecord();
        record.setWeekSummary(true);
        record.setWeekSummaryIndex(weekSummaryIndex);
        record.setWeeks(weeks);
        record.setWeekWorkTime(weekWorkTime);
        summaryList.add(record);
        Log.d(TAG, "the " + weeks + " week...final weekWorkTime = " + weekWorkTime);
    }

    private void deleteRecord(long milliseconds) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(MainActivity.this);
        }
        mDatabaseHelper.deleteByMilliseconds(milliseconds);
    }

    private void saveRecord() {
        if (etDateTime == null || TextUtils.isEmpty(etDateTime.getText())) {
            Log.d(TAG, "etDateTime == NULL or getText == null");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        String dateTimeString = String.valueOf(etDateTime.getText());
        Date date = DateTimeUtil.changeStringToDate(dateTimeString,
                MainActivity.this.getString(R.string.default_date_time_format));
        calendar.setTime(date);

        DateTimeRecord record = new DateTimeRecord();
        record.setDateTime(dateTimeString);
        record.setWeeks(DateTimeUtil.getWeekOfYear(calendar));
        record.setWeekday(DateTimeUtil.getDayOfWeek(calendar));
        record.setMilliseconds(date.getTime());

        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(MainActivity.this);
        }
        mDatabaseHelper.insert(record);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
        super.onDestroy();
    }
}
