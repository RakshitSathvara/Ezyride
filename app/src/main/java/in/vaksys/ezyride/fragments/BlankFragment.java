package in.vaksys.ezyride.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import in.vaksys.ezyride.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

/*

    @Bind(R.id.input_from)
    EditText inputFrom;
    @Bind(R.id.input_layout_from)
    TextInputLayout inputLayoutFrom;
    @Bind(R.id.input_to)
    EditText inputTo;
    @Bind(R.id.input_layout_to)
    TextInputLayout inputLayoutTo;
    @Bind(R.id.btn_search_ride)
    Button btnSearchRide;
    @Bind(R.id.btn_offer_ride)
    Button btnOfferRide;
*/

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        /*inputFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (inputFrom.getRight() - inputFrom.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });*/
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
