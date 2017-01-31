package net.sparkworks.mapper.netsens;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.List;

public class NetsensClient {

    RestOperations restTemplate = new RestTemplate();
    Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();

	String baseUri;

	public NetsensClient(String url, String username, String password, String station) {
		baseUri = url + "?user=" + username + "&password=" + password + "&station=" + station;
		unmarshaller.setClassesToBeBound(Meter.class, Measurement.class, Meters.class);
    }

	public List<Meter> getLatestMeasurements() {
		String result = restTemplate.getForObject(baseUri + "&meter=last_reading", String.class);
		StringReader stringReader = new StringReader(result);
        StreamSource streamSource = new StreamSource(stringReader);
        Meters meters = (Meters) unmarshaller.unmarshal(streamSource);
        return meters.getMeters();
    }

	public List<Meter> getTimerangeMeasurement(String meter, long from, long to) {
		String result = restTemplate.getForObject(baseUri + "&meter=" + meter + "&from=" + from + "&to=" + to, String.class);
		StringReader stringReader = new StringReader(result);
		StreamSource streamSource = new StreamSource(stringReader);
		Meters meters = (Meters) unmarshaller.unmarshal(streamSource);
		return meters.getMeters();
	}

	public Meter getLatestMeasurement(String meter) {
		String result = restTemplate.getForObject(baseUri + "&meter=" + meter + "/last_reading", String.class);
		StringReader stringReader = new StringReader(result);
		StreamSource streamSource = new StreamSource(stringReader);
		Meters meters = (Meters) unmarshaller.unmarshal(streamSource);
		if (meters.getMeters().size() > 0)
			return meters.getMeters().get(0);
		return null;
	}






}
