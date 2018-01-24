package petfinder.transferwise.home;

import java.util.List;

import petfinder.transferwise.dao.Catalogue;

public interface IHomeFragment {

    void refreshResult(List<Catalogue> list);

    void showErrorServerResponse(Throwable response);

}
