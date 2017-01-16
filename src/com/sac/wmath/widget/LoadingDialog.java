package com.sac.wmath.widget;

import com.sac.wmath.R;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

/**
 * 自定义加载框实现等待对话框效果
 * 
 */
public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialogStyle);
		setContentView(R.layout.dialog_loading);
		// 禁止单击对话框以外的部分退出对话框
		setCanceledOnTouchOutside(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 禁止返回键退出对话框
		return true;
	}
}
