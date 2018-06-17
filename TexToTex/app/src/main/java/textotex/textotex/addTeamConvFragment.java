package textotex.textotex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class addTeamConvFragment extends Fragment {

    public addTeamConvFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_team_conv_fragment, container, false);

        return rootView;
    }

}
