package petfinder.transferwise.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.flowlayout.FlowLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.IgnoreWhen;

import java.util.List;

import petfinder.transferwise.R;
import petfinder.transferwise.dao.Catalogue;
import petfinder.transferwise.overview.DetailsFragment;
import petfinder.transferwise.rest.RetrofitException;
import petfinder.transferwise.utils.SharedStatesMap;


public class HomeFragment extends Fragment implements IHomeFragment {

    SharedStatesMap mSharedStates;
    TextView tvOutput, tvHello;
    HomeFragmentPresenter mPresenter;
    FlowLayout flowLayout;
    String prev = "", animal = "cat";


    @AfterViews
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            mPresenter = new HomeFragmentPresenter(this, getContext());
            mSharedStates = SharedStatesMap.getInstance();

            flowLayout = (FlowLayout) v.findViewById(R.id.flow);

            tvHello = (TextView) v.findViewById(R.id.tv_hello);
            tvOutput = (TextView) v.findViewById(R.id.tv_output);
            tvOutput.setText(getString(R.string.please_wait_loading));


            mSharedStates.setKey("selectedAnimal", animal);
            mPresenter.getBreedList(animal);

        } catch (Exception ex) {
            ex.printStackTrace();
            tvOutput.setText(ex.getMessage());
        }
        return v;
    }


    @UiThread
    @IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
    @Override
    public void refreshResult(List<Catalogue> resultList) {
        try {
            //Toast.makeText(getContext(), resultList.size(), Toast.LENGTH_SHORT).show();
            tvOutput.setText(getString(R.string.categories_found) + ": " + resultList.size());
            for (Catalogue list : resultList) {
                if (!prev.equals(list.getBreed())) {
                    TextView textView = buildLabel(list.getBreed());
                    flowLayout.addView(textView);
                }
                prev = list.getBreed();
            }
            tvHello.setVisibility(View.VISIBLE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @UiThread
    @IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
    @Override
    public void showErrorServerResponse(Throwable response) {

        String msg = response.getMessage();
        tvOutput.setText(getString(R.string.server_response) + ": " + msg);
        if (msg.contains("Unable to resolve host") || msg.contains("No address associated with hostname") || msg.contains("403 Forbidden"))
            tvOutput.append("\n\n" + getString(R.string.no_response));
        RetrofitException.showException(tvOutput, response);
        //progressBar.setVisibility(View.GONE);
    }


    private TextView buildLabel(final String breed) {
        final TextView textView = new TextView(getContext());
        textView.setText(breed);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setPadding((int) dpToPx(18), (int) dpToPx(8), (int) dpToPx(18), (int) dpToPx(8));
        textView.setBackgroundResource(R.drawable.label_bg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedStates.setKey("selectedBreed", breed);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLUE);

                jumpOverview();
            }
        });
        return textView;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private void jumpOverview() {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.home_fragment_container, new DetailsFragment(), DetailsFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

}