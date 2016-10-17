package group.g203.acountables.base.presenter;

public interface BaseModelViewPresenter extends BaseViewPresenter {

    public void bindModels();

    public void unbindModels();

    public void resetModelAndView();
}
