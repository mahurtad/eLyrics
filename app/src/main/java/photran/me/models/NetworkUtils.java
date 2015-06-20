package photran.me.models;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


public class NetworkUtils {

	private static final String TAG = NetworkUtils.class.getName();

    public static String getContentFormURL(String url) {
        String result = "";
        URL yahoo = null;
        try {
            yahoo = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            yahoo.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;

        try {
            while ((inputLine = in.readLine()) != null)
                result = result + "\n" +inputLine;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


	/**
	 * get HTTP content by url
	 * 
	 * @param url
	 * @return
	 */
	public static String getContentByURL(String url) {
		InputStream is = null;
		String strContext = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Error Encoding");
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error ClientProtocol");
		} catch (IOException e) {
			Log.e(TAG, "Error IO");
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			strContext = sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "Error converting result");
		}
		return strContext;
	}

	/**
	 * check if device current has connection or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * check if GPS is enable or not
	 * 
	 * @param mLocationManager
	 * @return
	 */
	public static boolean isGPSProviderEnabled(LocationManager mLocationManager) {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}
}
