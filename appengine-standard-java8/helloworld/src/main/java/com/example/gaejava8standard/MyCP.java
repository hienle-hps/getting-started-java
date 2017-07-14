package com.example.gaejava8standard;

import java.io.IOException;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

public class MyCP implements CredentialsProvider {

	private AccessToken accessToken;
	
	public MyCP(AccessToken accessToken) {
		super();
		this.accessToken = accessToken;
	}

	@Override
	public Credentials getCredentials() throws IOException {
		return new GoogleCredentials(accessToken);
	}

}
