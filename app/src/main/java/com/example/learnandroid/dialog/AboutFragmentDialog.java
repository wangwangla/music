package com.example.learnandroid.dialog;

import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.learnandroid.R;

/**
 * 标准 About 弹窗，使用 DialogFragment 避免因配置变化导致的窗口泄漏。
 */
public class AboutFragmentDialog extends DialogFragment {
	private static final String TAG = "AboutFragmentDialog";

	public static void show(@NonNull FragmentManager fragmentManager) {
        if (fragmentManager.isStateSaved()) {
            return;
        }
        if (fragmentManager.findFragmentByTag(TAG) != null) {
			return;
		}
		new AboutFragmentDialog().show(fragmentManager, TAG);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.about_dialog, null, false);
		TextView versionView = contentView.findViewById(R.id.about_version);
		versionView.setText(getString(R.string.about_version_format, getVersionName()));

		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.action_about)
				.setView(contentView)
				.setPositiveButton(android.R.string.ok, null)
				.create();
	}

	@NonNull
	private String getVersionName() {
		try {
			PackageManager packageManager = requireContext().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), 0);
			return packageInfo.versionName == null ? "1.0" : packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "1.0";
		}
	}
}
