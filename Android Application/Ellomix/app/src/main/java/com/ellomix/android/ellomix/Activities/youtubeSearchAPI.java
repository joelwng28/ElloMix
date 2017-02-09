package com.ellomix.android.ellomix.Activities;

/**
 * Created by Abhi on 2/3/2017.
 */

import android.os.AsyncTask;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Abhi on 2/3/2017.
 */

public class youtubeSearchAPI {
    interface AsyncResponse {
        void processFinish(String output);
    }
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;
    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final String API_KEY = "AIzaSyCmL8ycwQoL1UDUz9EWpWHTq3hy3e7r2ck";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private AsyncResponse callback;

    public youtubeSearchAPI(AsyncResponse resp, String url) {
        this.callback = resp;
        new BackgroundSearch().execute(url);
    }

    public class BackgroundSearch extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return getFirstURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            callback.processFinish(s);
        }
    }

    public static String getFirstURL (String query) {
        String retURL = "2k0SmqbBIpQ";
        try {
            System.out.println("=============== try block started =================");
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Prompt the user to enter a query term.

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = API_KEY;
            search.setKey(apiKey);
            search.setQ(query);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            retURL = firstURL(searchResultList.iterator(), query);
            /*if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }*/
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        //current bug: for some reason it's skipping the entire "try" block above
        System.out.println("============ URL fetched ================");
        return retURL;

    }

    public static String firstURL(Iterator<SearchResult> results, String query) {
        if (!results.hasNext()) {
            return "2k0SmqbBIpQ";
        }

        SearchResult video = results.next();
        ResourceId rId = video.getId();

        return rId.getVideoId();
    }
}
