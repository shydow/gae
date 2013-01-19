package com.tangpian.sna.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.security.PrivateKeys;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;

public class OauthUtil {
	/** Global instance of the HTTP transport. */
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static final String SERVICE_ACCOUNT_EMAIL = "517743315265@developer.gserviceaccount.com";
	private static Plus plus = null;

	public static Plus getServicePlus() throws GeneralSecurityException,
			IOException {

		if (null == plus) {
			PrivateKey key = PrivateKeys.loadFromKeyStore(
					KeyStore.getInstance("PKCS12"),
					OauthUtil.class.getResourceAsStream("/key.p12"),
					"notasecret", "privatekey", "notasecret");

			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
					.setServiceAccountScopes(PlusScopes.PLUS_ME)
					.setServiceAccountPrivateKey(key).build();

			plus = new Plus.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("gplustrend").build();
		}
		return plus;
	}

	private static AuthorizationCodeFlow authorizationCodeFlow = null;
	private static final String GOOGLE_OAUTH2_AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
	private static final String GOOGLE_OAUTH2_TOKEN_URI = "https://accounts.google.com/o/oauth2/token";
	// OAuth client ID
	public static String CLIENT_ID = ConfigUtil.getProperty("oauth_client_id");

	// OAuth client secret
	public static String CLIENT_SECRET = ConfigUtil
			.getProperty("oauth_client_secret");

	// OAuth client secret
	public static String GOOGLE_API_KEY = ConfigUtil
			.getProperty("google_api_key");

	// Space separated list of OAuth scopes
	private static final List<String> SCOPES = Arrays
			.asList("https://www.googleapis.com/auth/plus.me");

	// OAuth redirect URI
	public static String REDIRECT_URI = ConfigUtil
			.getProperty("oauth_redirect_uri");

	public static AuthorizationCodeFlow getClientFlow() {
		// if (null == authorizationCodeFlow) {
		// authorizationCodeFlow = new AuthorizationCodeFlow.Builder(
		// BearerToken.authorizationHeaderAccessMethod(),
		// HTTP_TRANSPORT,
		// JSON_FACTORY,
		// new GenericUrl(GOOGLE_OAUTH2_TOKEN_URI),
		// new ClientParametersAuthentication(CLIENT_ID, CLIENT_SECRET),
		// CLIENT_ID, GOOGLE_OAUTH2_AUTH_URI)
		// .setCredentialStore(new MemoryCredentialStore())
		// .setScopes(Arrays.asList(SCOPES)).build();
		// }
		GoogleClientSecrets clientSecrets;
		try {
			clientSecrets = GoogleClientSecrets
					.load(JSON_FACTORY, OauthUtil.class
							.getResourceAsStream("/client_secrets.json"));
			if (null == authorizationCodeFlow) {
				authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
						OauthUtil.HTTP_TRANSPORT, OauthUtil.JSON_FACTORY,
						clientSecrets, SCOPES).setAccessType("offline")
						.setApprovalPrompt("force").build();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return authorizationCodeFlow;
	}

	public static String stripTags(String input) {
		return input.replaceAll("\\<[^>]*>", "");
	}

	public static void main(String[] args) {
		System.out.println(stripTags("adsbadafds"));
	}
}
