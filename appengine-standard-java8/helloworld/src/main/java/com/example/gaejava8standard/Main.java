package com.example.gaejava8standard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.services.pubsub.PubsubScopes;

public class Main {

	public static void main(String[] args) throws Exception {
		
		GoogleCredentialsProvider provider = GoogleCredentialsProvider
			.newBuilder()
			.setScopesToApply(
				new ArrayList<>(PubsubScopes.all())
			).build();
		Map<String, List<String>> requestMetadata = provider.getCredentials().getRequestMetadata();
		for (String key : requestMetadata.keySet()) {
			System.out.print(key + " : ");
			for (String val : requestMetadata.get(key)) {
				System.out.println(" " + val);
			}
			System.out.println();
		}
	}

}
