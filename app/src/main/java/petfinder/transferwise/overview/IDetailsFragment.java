package petfinder.transferwise.overview;

import java.util.List;

import petfinder.transferwise.dao.Pet;
import petfinder.transferwise.dao.Pet_Breed;
import petfinder.transferwise.dao.Pet_Contact;
import petfinder.transferwise.dao.Pet_Media;

public interface IDetailsFragment {

    void refreshResult(Pet pet, List<Pet_Media> listMedia, List<Pet_Breed> listBreed, Pet_Contact contact);

    void showErrorServerResponse(Throwable response);

}
