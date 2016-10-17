package group.g203.acountables.path.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import group.g203.acountables.R;
import group.g203.acountables.base.presenter.BaseViewPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        setContentView(contentView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayEmptyView() {

    }

    @Override
    public void displayCountables() {

    }

    @Override
    public void addCountable() {

    }

    @Override
    public void deleteCountable() {

    }

    @Override
    public void undoCountableDelete() {

    }

    @Override
    public void displayInfoDialog() {

    }

    @Override
    public void displaySortDialog() {

    }

    @Override
    public void enableAddButton() {

    }

    @Override
    public void disableAddButton() {

    }

    @Override
    public void resetAddCountableView() {

    }

    @Override
    public <P extends BaseViewPresenter> void setPresenter(P presenter) {

    }
}
