package xroom;

import java.net.*;
import java.io.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JavaAPI
{
	protected HttpURLConnection connection;

	protected String username;
	protected String secret;
	protected String apiHost = "signal.zroom.app";
	protected String apiVersion = "2";

    public JavaAPI(String username, String secret) throws Exception
    {
		if (username == null || secret == null)
		{
			throw new Exception ("Both username and secret are required.");
		}

		this.username = username;
		this.secret = secret;
    }

    protected static String getAlphaNumericString(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++)
        {
            // generate a random number between 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

	public String Exec(String endpoint, String method, String jsonString) throws Exception
	{
		if (endpoint == null || endpoint.length() == 0)
		{
			throw new Exception ("No endpoint specified");
		}

		if (method == null || method.length() == 0)
		{
			throw new Exception ("No method specified");
		}

        String salt = JavaAPI.getAlphaNumericString(32);


        // compute HMAC

        // get an hmac_sha1 key from the raw key bytes
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        // get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        // compute the hmac on input data bytes
        byte[] bytes = mac.doFinal(salt.getBytes());
        StringBuilder hash = new StringBuilder();

        for (byte b : bytes)
        {
            hash.append(String.format("%02x", b));
        }

		// setup connection to endpoint
        URL obj = new URL("https://" + apiHost + "/api/" + endpoint);
        connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty ("x-api-version", apiVersion);
        connection.setRequestProperty ("x-random", salt);
        connection.setRequestProperty ("x-auth-id", username);
        connection.setRequestProperty ("x-auth-key", hash.toString());
        connection.setDoOutput(true);

        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write("{\"method\":\"" + method + "\",\"data\":" + jsonString + "}");
        osw.flush();
        osw.close();

        // Get the response
		String line, result = "";
		InputStream stream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));

        while ((line = rd.readLine()) != null) result += line;

        rd.close();

        return result;
	}
}
