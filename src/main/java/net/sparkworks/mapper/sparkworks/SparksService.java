package net.sparkworks.mapper.sparkworks;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.squareup.okhttp.OkHttpClient;
import io.swagger.client.ApiClient;
import io.swagger.client.api.DataapicontrollerApi;
import io.swagger.client.api.ResourceapicontrollerApi;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SparksService {
	public final DataapicontrollerApi dataApi = new DataapicontrollerApi();
	public final ResourceapicontrollerApi resApi = new ResourceapicontrollerApi();

	public SparksService() {
		//Read credentials from file
		Map<String, String> map = null;
		ApiClient client;
		String confFile = "./account.yaml";
		FileReader fr = null;
		try {
			fr = new FileReader(confFile);
		} catch (FileNotFoundException e) {
			System.err.println("Account configuration file not found ['account.yaml']");
		}
		YamlReader yaml = new YamlReader(fr);
		try {
			map = (Map<String, String>) yaml.read();
		} catch (YamlException e) {
			e.printStackTrace();
		}

		//Request access token
		SwaggerTokenRequest tokenRequest = new SwaggerTokenRequest(map.get("username"), map.get("password"), map.get("secret"), map.get("client"));
		client = new ApiClient();
		client.setAccessToken(tokenRequest.getAccess_token());

		OkHttpClient httpClient = client.getHttpClient();
		//Set larger timeouts
		httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
		httpClient.setReadTimeout(60, TimeUnit.SECONDS);
		httpClient.setWriteTimeout(60, TimeUnit.SECONDS);
		dataApi.setApiClient(client);
		resApi.setApiClient(client);
	}


}
