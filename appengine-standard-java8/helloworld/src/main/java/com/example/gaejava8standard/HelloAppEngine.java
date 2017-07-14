/**
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gaejava8standard;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.pubsub.model.Topic;
import com.google.auth.oauth2.AccessToken;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.TopicName;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "HelloAppEngine", value = "/hello")
public class HelloAppEngine extends HttpServlet {
	
	private String topicName = "mytopic";
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
		
    response.setContentType("text/plain");
	
	String projectId = ServiceOptions.getDefaultProjectId();
	/*
	 *  Claims automatic Auth on AppEngine:
	 *    https://github.com/GoogleCloudPlatform/google-cloud-java#authentication
	 *  
	 *  Claims gcloud-java doesn't work on AppEngine Standard:
	 *    https://github.com/GoogleCloudPlatform/google-cloud-java/tree/master/google-cloud-pubsub#quickstart
	 *    
	 *  Claims gcloud-java DOES work on AppEngine Standard Java 8:
	 *    https://cloudplatform.googleblog.com/2017/06/Google-App-Engine-standard-now-supports-Java-8.html
	 * 	
	 */

	// Use gcloud-java
	TopicAdminSettings settings = TopicAdminSettings.defaultBuilder()
		.setCredentialsProvider(
			new MyCP(
				new AccessToken(
					"TOKEN",
					null
				)
			)
		).build();
	
	try (TopicAdminClient topicAdminClient = TopicAdminClient.create(settings)) {
		com.google.pubsub.v1.Topic topic = topicAdminClient.getTopic(TopicName.create(projectId, topicName));
		response.getWriter().println("new: " + topic.getName());
	} catch (Exception e) {
		throw new IOException(e);
	}
	
	// Use google-api-client
	try {
		Pubsub pubsub = new Pubsub.Builder(
			GoogleNetHttpTransport.newTrustedTransport(),
			JacksonFactory.getDefaultInstance(),
			GoogleCredential.getApplicationDefault().createScoped(PubsubScopes.all())
		).build();
		
		Topic topic = pubsub.projects().topics().get(String.format("projects/%s/topics/%s", projectId, topicName)).execute();
		response.getWriter().println("old: " + topic.getName());
	} catch (GeneralSecurityException e) {
		response.getWriter().println(e);
	}
	  
    response.getWriter().println("Hello");

  }

}
// [END example]
