package com.xiaoxu.music.community.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.xiaoxu.music.community.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 日期时间选择控件
 */
public class DateTimePickDialogUtil implements OnDateChangedListener{
	
	public Dialog dialog;
	private String dateTime;
	private TextView datetitle;
	private TextView tv_birthday;
	private String initDateTime;
	private DatePicker datePicker;
	private Activity activity;
	private Button ok_btn,cancle_btn;

	/**
	 * 日期时间弹出选择框构造函数
	 * 初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DateTimePickDialogUtil(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;

	}

	public void init(DatePicker datePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 ";
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
	}

	/**
	 * 弹出日期时间选择框方法
	 * @param inputDate :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public TextView dateTimePicKDialog(final TextView tv_birthday,OnClickListener clickListener) {
		this.tv_birthday = tv_birthday;
		LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_datetime, null);
		datetitle = (TextView) dateTimeLayout.findViewById(R.id.title_date);
		ok_btn = (Button) dateTimeLayout.findViewById(R.id.ok);
		cancle_btn = (Button) dateTimeLayout.findViewById(R.id.cancel);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		init(datePicker);
		datetitle.setText(initDateTime);
		ok_btn.setOnClickListener(clickListener);
		cancle_btn.setOnClickListener(clickListener);
		dialog = new Dialog(activity, R.style.Custom_Dialog);
		dialog.setContentView(dateTimeLayout);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
		onDateChanged(null, 0, 0, 0);
		return tv_birthday;
	}
	

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), 0,0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		dateTime = sdf.format(calendar.getTime());
		tv_birthday.setText(dateTime);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 ,并赋值给calendar
	 * @param initDateTime 初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, 0, 0);
		return calendar;
	}

	/**
	 * 截取子串
	 * @param srcStr 源串
	 * @param pattern 匹配模式
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

}

