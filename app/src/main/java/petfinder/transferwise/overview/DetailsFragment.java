package petfinder.transferwise.overview;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.IgnoreWhen;

import java.util.List;

import petfinder.transferwise.R;
import petfinder.transferwise.dao.Pet;
import petfinder.transferwise.dao.Pet_Breed;
import petfinder.transferwise.dao.Pet_Contact;
import petfinder.transferwise.dao.Pet_Media;
import petfinder.transferwise.rest.RetrofitException;
import petfinder.transferwise.utils.SharedStatesMap;

public class DetailsFragment extends Fragment implements IDetailsFragment {

    SharedStatesMap mSharedStates;
    TextView tvMain, tvDescription, tvBreed, tvAge, tvGender, tvSize, tvDate, tvOutput;
    TextView tvLabelBreed, tvLabelDescription, tvLabelContact, tvAddress, tvPhone, tvEmail;
    ImageView imageView;
    ViewFlipper vfLayout;
    DetailsFragmentPresenter mPresenter;
    DiagonalLayout dl;
    KenBurnsView kenBurnsView;
    String breed = "";


    @AfterViews
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        try {

            mPresenter = new DetailsFragmentPresenter(this, getContext());
            mSharedStates = SharedStatesMap.getInstance();

            String animal = mSharedStates.getKey("selectedAnimal");
            breed = mSharedStates.getKey("selectedBreed");


            tvOutput = (TextView) v.findViewById(R.id.tv_output);
            tvOutput.setText(getString(R.string.please_wait_loading));

            dl = (DiagonalLayout) v.findViewById(R.id.diagonalLayout);
            kenBurnsView = (KenBurnsView) v.findViewById(R.id.kenBurnsView);
            kenBurnsView.setOnClickListener(null);

            tvMain = (TextView) v.findViewById(R.id.tv_main);
            tvDescription = (TextView) v.findViewById(R.id.tv_description);
            tvBreed = (TextView) v.findViewById(R.id.tv_breed);
            tvAge = (TextView) v.findViewById(R.id.tv_age);
            tvGender = (TextView) v.findViewById(R.id.tv_gender);
            tvSize = (TextView) v.findViewById(R.id.tv_size);

            tvLabelBreed = (TextView) v.findViewById(R.id.tv_label_breed);
            tvLabelDescription = (TextView) v.findViewById(R.id.tv_label_description);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            tvLabelContact = (TextView) v.findViewById(R.id.tv_label_contact);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            tvPhone = (TextView) v.findViewById(R.id.tv_phone);
            tvEmail = (TextView) v.findViewById(R.id.tv_email);


            vfLayout = (ViewFlipper) v.findViewById(R.id.viewflipper);

            mPresenter.getPetRandom(animal, breed);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }


    @UiThread
    @IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
    @Override
    public void refreshResult(Pet result, List<Pet_Media> listMedia, List<Pet_Breed> listBreed, Pet_Contact contact) {
        String link = "";
        try {
            if (result.getPet_id() ==null ) {
                dl.setVisibility(View.GONE);
                tvLabelBreed.setVisibility(View.GONE);
                tvLabelDescription.setVisibility(View.GONE);
                tvLabelContact.setVisibility(View.GONE);
                tvOutput.setText(getString(R.string.info_not_found));
            } else {
                tvOutput.setText("");
                tvMain.setText(result.getName());
                tvDescription.setText(result.getDescription());
                if (result.getDescription().equals("")) tvLabelDescription.setVisibility(View.GONE);

                tvAge.setText(result.getAge());
                tvGender.setText(result.getSex());
                tvSize.setText(result.getSize());
                tvDate.setText(getString(R.string.date_update) + ": " + result.getLast_update());

                if (!listMedia.isEmpty()) {
                    if (listBreed.size()==0) {
                        tvBreed.setText(breed);
                    } else {
                        for (Pet_Breed item : listBreed) {
                            tvBreed.append(item.getBreed() + "\n");
                        }
                    }
                }

                if (!listMedia.isEmpty()) {
                    if (listMedia.size() > 1) {
                        vfLayout.setVisibility(View.VISIBLE);
                    }
                    for (Pet_Media item : listMedia) {
                        link = item.getPhoto();

                        if (listMedia.size() > 1) {
                            imageView = new ImageView(getContext());
                            //imageView.setId(j);
                            imageView.setPadding(2, 2, 2, 2);
                            vfLayout.addView(imageView);
                            Glide
                                    .with(getContext())
                                    .load(link)
                                    .centerCrop()
                                    .placeholder(R.drawable.noimage)
                                    .crossFade()
                                    .into(imageView);
                        }
                    }
                    Glide
                            .with(getContext())
                            .load(link)
                            .centerCrop()
                            .placeholder(R.drawable.noimage)
                            .crossFade()
                            .into(kenBurnsView);
                }

                tvAddress.setText(contact.getState() + " " + contact.getCity() + " " + contact.getZip() + ", " + contact.getAddress());
                tvPhone.setText(getString(R.string.phone) + ": " + contact.getPhone());
                tvEmail.setText(contact.getEmail());
            }

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

}