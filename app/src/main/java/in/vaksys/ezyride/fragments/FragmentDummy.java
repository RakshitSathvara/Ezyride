package in.vaksys.ezyride.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.SearchRideAdapter;
import in.vaksys.ezyride.utils.DividerItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDummy extends Fragment {
    @Bind(R.id.recycler_view_search_ride)
    RecyclerView recyclerViewSearchRide;

    SearchRideAdapter searchRideAdapter;
    private String[] MyPrice = {"200", "200", "200", "200", "200", "200", "200", "200", "200", "200"};

    public static FragmentDummy getInstance(int position) {
        FragmentDummy fragmentDummy = new FragmentDummy();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentDummy.setArguments(args);
        return fragmentDummy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, layout);

        recyclerViewSearchRide.setHasFixedSize(true);
        recyclerViewSearchRide.setItemAnimator(new DefaultItemAnimator());
        searchRideAdapter = new SearchRideAdapter(getActivity(), MyPrice);
        LinearLayoutManager gridLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        gridLayoutManager.scrollToPosition(0);
        recyclerViewSearchRide.setLayoutManager(gridLayoutManager);
        recyclerViewSearchRide.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewSearchRide.setAdapter(searchRideAdapter);

        return layout;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
