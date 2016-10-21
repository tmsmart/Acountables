package group.g203.countables.base.view;

public interface BaseDialogView extends BaseInnerView {

    void setPositiveButton(String buttonText);

    void setNegativeButton(String buttonText);

    void onSetTitle(String title);
}
