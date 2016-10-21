package group.g203.countables.path.main.presenter;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.List;

import group.g203.countables.base.manager.BaseDialogManager;
import group.g203.countables.base.presenter.BasePresenter;
import group.g203.countables.base.view.BaseDialog;
import group.g203.countables.base.view.BaseView;
import group.g203.countables.custom_view.loading_view.LoadingAspect;
import group.g203.countables.custom_view.loading_view.LoadingPresenter;
import group.g203.countables.path.main.view.CreditsDialog;
import group.g203.countables.path.main.view.MainActivity;
import group.g203.countables.path.main.view.MainInfoDialog;
import group.g203.countables.path.main.view.MainView;
import group.g203.countables.path.main.view.SortDialog;

public class MainPresenter implements BasePresenter, CreditsDialogPresenter, MainInfoDialogPresenter,
    SortDialogPresenter, LoadingPresenter {

    private final static int EMPTY_ICON_DIMEN = 120;

    MainView mMainView;
    LoadingAspect mLoadingAspect;
    MainInfoDialog mInfoDialog;
    CreditsDialog mCreditsDialog;
    SortDialog mSortDialog;

    @Override
    public void bindModels() {

    }

    @Override
    public void unbindModels() {

    }

    @Override
    public void bindViews(BaseView... views) {
        mMainView = (MainView) views[0];

        mLoadingAspect = ((MainActivity)mMainView).mLoadingAspect;
        mLoadingAspect.setPresenter(((MainActivity) mMainView).getPresenter());
    }

    @Override
    public void unbindViews() {

    }

    @Override
    public void unbindModelsAndViews() {
        unbindModels();
        unbindViews();
    }

    @Override
    public void displayError(String errorMessage) {

    }

    @Override
    public void setCreditsDialogTitle(String title) {
        mCreditsDialog.builder.setTitle(title);
    }

    @Override
    public void setCreditsDialogPositiveButton(String buttonText) {
        mCreditsDialog.builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCreditsDialog.onDismiss(dialog);
            }
        });
    }

    @Override
    public void setInfoDialogTitle(String title) {
        mInfoDialog.builder.setTitle(title);
    }

    @Override
    public void setInfoDialogPositiveButton(String buttonText) {
        mInfoDialog.builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void setInfoDialogMessage(String message) {
        mInfoDialog.builder.setMessage(message);
    }

    public void displayCreditsDialog(FragmentManager fm) {
        mCreditsDialog = CreditsDialog.getInstance();
        BaseDialogManager.displayFragmentDialog(mCreditsDialog, fm);
    }

    public void displayInfoDialog(FragmentManager fm) {
        mInfoDialog = MainInfoDialog.getInstance();
        BaseDialogManager.displayFragmentDialog(mInfoDialog, fm);
    }

    public void displaySortDialog(FragmentManager fm) {
        mSortDialog = SortDialog.getInstance();
        BaseDialogManager.displayFragmentDialog(mSortDialog, fm);
    }

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void setSortDialogTitle(String title) {
        mSortDialog.builder.setTitle(title);
    }

    @Override
    public void setSortDialogItems(int arrayResource) {
        mSortDialog.builder.setSingleChoiceItems(arrayResource, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void displayLoading() {
        mLoadingAspect.mEmptyMessage.setVisibility(View.GONE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.GONE);
        mLoadingAspect.mContent.setVisibility(View.GONE);

        mLoadingAspect.mIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayContent() {
        mLoadingAspect.mContent.setVisibility(View.VISIBLE);

        mLoadingAspect.mEmptyMessage.setVisibility(View.GONE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.GONE);
        mLoadingAspect.mIndicator.setVisibility(View.GONE);
    }

    @Override
    public void displayEmptyView() {
        mLoadingAspect.mEmptyMessage.setVisibility(View.VISIBLE);
        mLoadingAspect.mEmptyIcon.setVisibility(View.VISIBLE);

        mLoadingAspect.mIndicator.setVisibility(View.GONE);
        mLoadingAspect.mContent.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyMessage(String message) {
        mLoadingAspect.mEmptyMessage.setText(message);
    }

    @Override
    public void setEmptyIcon(int iconResource) {
        Picasso.with(mLoadingAspect.getContext()).load(iconResource).resize(EMPTY_ICON_DIMEN, EMPTY_ICON_DIMEN).into(mLoadingAspect.mEmptyIcon);
    }

    public void displayDialogsAfterStateChange(List<String> dialogTags, FragmentManager fm) {
        for (String tag : dialogTags) {
            BaseDialog dialog = null;
            
            if (tag.equals(SortDialog.TAG)) {
                dialog = mSortDialog = SortDialog.getInstance();
            } else if (tag.equals(CreditsDialog.TAG)) {
                dialog = mCreditsDialog = CreditsDialog.getInstance();
            } else if (tag.equals(MainInfoDialog.TAG)) {
                dialog = mInfoDialog = MainInfoDialog.getInstance();
            }
            
            if (dialog != null) {
                BaseDialogManager.displayFragmentDialog(dialog, fm);
            }
        }
    }
}
