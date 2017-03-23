package net.sparkworks.mapper.util;

import io.swagger.client.ApiException;
import io.swagger.client.model.LatestDTO;
import io.swagger.client.model.ResourceDTO;
import net.sparkworks.mapper.netsens.Meter;
import net.sparkworks.mapper.netsens.NetSensConverter;
import net.sparkworks.mapper.netsens.NetsensClient;
import net.sparkworks.mapper.service.SenderService;
import net.sparkworks.mapper.sparkworks.SparksService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NetsensPollingService {

	private static final Logger LOGGER = Logger.getLogger(NetsensPollingService.class);
	@Value("${netsens.url}")
	String baseUrl;
	@Value("${netsens.username}")
	String username;
	@Value("${netsens.password}")
	String password;
	@Value("${netsens.station}")
	String station;
	@Value("${netsens.meter}")
	String meter;
	@Value("${netsens.baseURI}")
	String baseURI;
	@Value("${app.checkLatest}")
	boolean checkLatestMeasurement;
	@Autowired
	SenderService senderService;
	@Autowired
	NetSensConverter netSensConverter;
	@Autowired
	SparksService sparks;
	int messageSentForMeter;
	int totalMessagesSent;
	Map<String, Long> uriMap = new HashMap<>();
	Map<String, Double> scaleFactorMap = new HashMap<>();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	@Value("${sparkworks.maxMessages}")
	private int MAX_MESSAGES;

	@PostConstruct
	public void init() throws IOException {
		BasicConfigurator.configure();
		System.setProperty("jsse.enableSNIExtension", "false");
		InputStream in = null;
		in = new FileInputStream("./meters.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		LOGGER.info("Mapping URIs to IDs");
		while ((line = br.readLine()) != null) {
			String meterUri = line.split(",")[0];
			Long id = getResourceIdFromURI(baseURI + "/" + meterUri);
			Double scaleFactor = Double.parseDouble(line.split(",")[1]);
			if (id != null) {
				uriMap.put(meterUri, id);
				scaleFactorMap.put(meterUri, scaleFactor);
			}
		}
		LOGGER.info("Mapped " + uriMap.size() + " resources");
	}

	@Scheduled(fixedDelayString = "${app.delay}")
	public void sendMeasurement() throws ParserConfigurationException, IOException, SAXException {
		totalMessagesSent = 0;
		if (!checkLatestMeasurement)
			LOGGER.info("Iteration : sending only latest measurements");
		else
			LOGGER.info("Iteration : checking latest value");
		Long now = (new Date()).getTime();
		NetsensClient netsens = new NetsensClient(baseUrl, username, password, station);
		for (String meterUri : uriMap.keySet()) {
			messageSentForMeter = 0;
			Double scaleFactor = scaleFactorMap.get(meterUri);
			String resUri = baseURI + "/" + meterUri;
			Long resourceId = uriMap.get(meterUri);
			LatestDTO latestResource = null;
			if (checkLatestMeasurement)
				latestResource = getResource(resourceId);

			//If it is possible to read the latest value stored in the GAIA platform send all the new values
			if (latestResource != null) {
				LOGGER.debug(latestResource.getUri() + ": " + sdf.format(latestResource.getLatestTime()));
				List<Meter> measurementList = netsens.getTimerangeMeasurement(meterUri, latestResource.getLatestTime(), now);
				for (Meter meter : measurementList) {
					if (messageSentForMeter >= MAX_MESSAGES) {
						LOGGER.debug(String.format("Sent %d messages [%s - %s]", messageSentForMeter, meterUri, meter.getTimestamp()));
						break;
					}
					if (meter.getId().equals("Temperatura aria") && meter.getValue().equals("0")) {
						LOGGER.warn(meterUri + ": Zero temperature found, not sending");
					} else {
						send(resUri, Double.parseDouble(meter.getValue()) * scaleFactor, Long.parseLong(meter.getTimestamp()));
					}
				}
			}
			//If it is not possible to read the latest value from the GAIA platform then send only the latest value and WARN
			else {
				Meter meter = netsens.getLatestMeasurement(meterUri);
				if (meter != null) {
					LOGGER.debug("Sending only latest value for: " + meterUri);
					//Check if we are dealing with temperaturem if true discar zero values
					if (meter.getId().equals("Temperatura aria") && meter.getValue().equals("0")) {
						LOGGER.warn(meterUri + ": Zero temperature found, not sending");
					} else {
						send(resUri, Double.parseDouble(meter.getValue()) * scaleFactor, Long.parseLong(meter.getTimestamp()));
					}
				} else {
					LOGGER.warn("No measurements for meter: " + meterUri);
				}
			}
			totalMessagesSent += messageSentForMeter;
		}
		LOGGER.info(String.format("Sent %d messages", totalMessagesSent));
		//in.reset();
	}


	private void send(String URI, double value, long timestamp) {
		senderService.sendMeasurement(URI, value, timestamp);
		messageSentForMeter++;
	}

	private LatestDTO getResourceFromURI(String resURI) {
		try {
			ResourceDTO resource = sparks.resApi.getByUriUsingGET(resURI);
			long id = resource.getResourceId();
			LatestDTO latestValue = sparks.dataApi.getLatestValuesUsingGET(id);
			return latestValue;
		} catch (ApiException e) {
			LOGGER.warn("Resource error [" + resURI + "]: " + e.getMessage());
		}
		return null;
	}

	private Long getResourceIdFromURI(String resURI) {
		try {
			ResourceDTO resource = sparks.resApi.getByUriUsingGET(resURI);
			Long id = resource.getResourceId();
			return id;
		} catch (ApiException e) {
			LOGGER.warn("Resource error [" + resURI + "]: " + e.getMessage());
		}
		return null;
	}

	private LatestDTO getResource(Long id) {
		try {
			LatestDTO latestValue = sparks.dataApi.getLatestValuesUsingGET(id);
			return latestValue;
		} catch (ApiException e) {
			LOGGER.warn("Resource error [" + id + "]: " + e.getMessage());
		}
		return null;
	}


}
