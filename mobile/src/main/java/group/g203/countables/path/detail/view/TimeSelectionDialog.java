package group.g203.countables.path.detail.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.path.detail.presenter.TimeSelectionDialogPresenter;

public class TimeSelectionDialog extends BaseDialog implements TimePickerDialog.OnTimeSetListener  {

    public static final String TAG = "TimeSelectDialog";

    TimeSelectionDialogPresenter mPresenter;

    public static TimeSelectionDialog getInstance(String tag) {
        TimeSelectionDialog dialog = new TimeSelectionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DIALOG_TAG, tag);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setPresenterFromActivity();
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));

        return dialog;
    }

    @Override
    public void setPositiveButton(AlertDialog.Builder builder, String buttonText) {

    }

    @Override
    public void setNegativeButton(AlertDialog.Builder builder, String buttonText) {

    }

    @Override
    public void onSetTitle(AlertDialog.Builder builder, String title) {

    }

    public void setPresenterFromActivity() {
        if (getActivity() != null && getActivity() instanceof DetailActivity) {
            setPresenter(((DetailActivity) getActivity()).getPresenter());
        }
    }

    public <P extends BasePresenter> void setPresenter(P presenter) {
        if (presenter instanceof TimeSelectionDialogPresenter) {
            mPresenter = (TimeSelectionDialogPresenter) presenter;
        }
    }

    public <P extends BasePresenter> P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mPresenter.setNotificationTime(getActivity().getSupportFragmentManager(), hourOfDay, minute);
    }
}
