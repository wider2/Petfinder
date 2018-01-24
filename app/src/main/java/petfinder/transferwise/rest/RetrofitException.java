package petfinder.transferwise.rest;


import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import petfinder.transferwise.utils.GlobalConstants;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitException extends RuntimeException {

    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        return new RetrofitException(message, url, response, Kind.HTTP, null, retrofit);
    }

    public static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, null);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, null);
    }

    public static Retrofit getInstanceRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(GlobalConstants.SERVER_SSL_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    /** Identifies the event kind which triggered a {@link RetrofitException}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private final String url;
    private final Response response;
    private final Kind kind;
    private final Retrofit retrofit;
    private String errorMessage = null;

    RetrofitException(String message, String url, Response response, Kind kind, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.retrofit = retrofit;
        try {
            if (response !=null) {
                if (response.errorBody() != null) {
                    this.errorMessage = response.errorBody().string();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /** The request URL which produced the error. */
    public String getUrl() {
        return url;
    }

    /** Response object containing status code, headers, body, etc. */
    public Response getResponse() {
        return response;
    }

    /** The event kind which triggered this error. */
    public Kind getKind() {
        return kind;
    }

    /** The Retrofit this request was executed on */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(response.errorBody());
    }


    public static void showException(View v, Throwable response) {

        Throwable cause = response;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        String msg = "";
        if (cause instanceof RetrofitException) {
            RetrofitException error = (RetrofitException) cause;
            msg = error.getErrorMessage();
        } else {
            msg = cause.getMessage();
            if (msg==null) msg = cause.toString();
        }
        response.printStackTrace();
        Snackbar snackBar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

}