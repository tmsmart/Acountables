package group.g203.countables.path.detail.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import group.g203.countables.R;
import group.g203.countables.base.Constants;
import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.utils.DisplayUtils;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.model.Countable;
import group.g203.countables.path.detail.view.DeleteDialog;
import group.g203.countables.path.detail.view.DetailActivity;
import group.g203.countables.path.detail.view.DetailView;
import group.g203.countables.path.detail.view.EditDialog;
import group.g203.countables.path.main.presenter.InfoDialogPresenter;
import group.g203.countables.path.main.presenter.MainPresenter;
import group.g203.countables.path.main.view.InfoDialog;
import io.realm.Realm;
import io.realm.Sort;

public class DetailPresenter implements BasePresenter, InfoDialogPresenter, DeleteDialogPresenter,
        EditDialogPresenter {

    private final static String INDEX = "index";
    private final static String COUNTABLE_DELETED = "Countable deleted successfully";
    private final static String COUNTABLE_EDITED = "Countable named edited successfully";

    Realm mRealm;
    DetailView mDetailView;
    View mView;
    Context mContext;
    Countable mCountable;
    InfoDialog mInfoDialog;
    DeleteDialog mDeleteDialog;
    EditDialog mEditDialog;

    @Override
    public void bindModels() {
        getRealmInstance().beginTransaction();
        mCountable = getRealmInstance().where(Countable.class).equalTo(INDEX,
                ((DetailActivity) mDetailView).getIntent().getExtras().getInt(Constants.COUNTABLE_INDEX)).findFirst();
        getRealmInstance().commitTransaction();
    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(BaseView... views) {
        mDetailView = (DetailView) views[0];
        mView = ((DetailActivity) mDetailView).mView;
        mContext = mView.getContext();
        bindModels();
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {

    }

    @Override
    public void displaySnackbarMessage(String errorMessage) {

    }

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void setInfoDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setInfoDialogPositiveButton(AlertDialog.Builder builder, String buttonText) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setInfoDialogMessage(AlertDialog.Builder builder, String message) {
        builder.setMessage(message);
    }

    @Override
    public void setDeleteDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setDeleteDialogNegativeButton(AlertDialog.Builder builder, String buttonText) {
        builder.setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setDeleteDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                                final DeleteDialog.DeleteCountableListener listener,
                                                final DeleteDialog deleteDialog) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeleteCountableClick(deleteDialog);
            }
        });
    }

    @Override
    public void setEditDialogTitle(AlertDialog.Builder builder, String title) {
        builder.setTitle(title);
    }

    @Override
    public void setEditDialogNegativeButton(AlertDialog.Builder builder, String buttonText) {
        builder.setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setEditDialogPositiveButton(AlertDialog.Builder builder, String buttonText,
                                            final EditDialog.EditCountableListener listener, final EditDialog editDialog) {
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onEditCountableClick(editDialog);
            }
        });
    }

    public void displayInfoDialog(FragmentManager fm) {
        mInfoDialog = InfoDialog.getInstance(InfoDialog.TAG, Constants.DETAIL_INFO);
        BaseDialogManager.displayFragmentDialog(mInfoDialog, InfoDialog.TAG, fm);
    }

    public void displayDeleteDialog(FragmentManager fm) {
        mDeleteDialog = DeleteDialog.getInstance(DeleteDialog.TAG);
        BaseDialogManager.displayFragmentDialog(mDeleteDialog, DeleteDialog.TAG, fm);
    }

    public void displayEditDialog(FragmentManager fm) {
        mEditDialog = EditDialog.getInstance(EditDialog.TAG, mCountable.name);
        BaseDialogManager.displayFragmentDialog(mEditDialog, EditDialog.TAG, fm);
    }

    public void handleInitDisplay(String name, String count) {
        ((TextView)mView.findViewById(R.id.tvCountableTitle)).setText(name);

        String completionsText = mContext.getString(R.string.completed_count);
        String countText = completionsText + Constants.SPACE + count;

        final SpannableStringBuilder spanString = new SpannableStringBuilder(countText);
        final StyleSpan boldFont = new StyleSpan(android.graphics.Typeface.BOLD);

        spanString.setSpan(boldFont, countText.indexOf(count),
                countText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.app_green)), countText.indexOf(count),
                countText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        ((TextView)mView.findViewById(R.id.tvCompletedCount)).setText(spanString);
    }

    public void deleteAndGoBack() {
        getRealmInstance().beginTransaction();
        mCountable.deleteFromRealm();
        MainPresenter.assignCountableIndices(getRealmInstance().where(Countable.class).findAll().sort(INDEX, Sort.ASCENDING));
        getRealmInstance().commitTransaction();

        DisplayUtils.displayToast(mContext, COUNTABLE_DELETED, Toast.LENGTH_SHORT);
        NavUtils.navigateUpFromSameTask(((DetailActivity) mDetailView));
    }

    public void renameCountable(String countableName) {
        getRealmInstance().beginTransaction();
        mCountable.name = countableName;
        getRealmInstance().commitTransaction();

        ((TextView)mView.findViewById(R.id.tvCountableTitle)).setText(countableName);
        DisplayUtils.displayToast(mContext, COUNTABLE_EDITED, Toast.LENGTH_SHORT);
    }

    Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

}
