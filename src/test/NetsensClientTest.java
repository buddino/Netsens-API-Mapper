import net.sparkworks.mapper.netsens.Meter;
import net.sparkworks.mapper.netsens.NetsensClient;
import org.junit.Test;

import java.util.List;

public class NetsensClientTest {

	@Test
	public void testGetLatestMeasurements() {
		NetsensClient netsensClient = new NetsensClient("http://live.netsens.it/export/xml_export_2A.php", "cnit", "cnit_pwd", "723");
		List<Meter> meterList = netsensClient.getLatestMeasurements();
		for (Meter m : meterList) {
			System.out.println(m.toString());
		}
	}

	@Test
	public void testGetTimerangeMeasurement() {
		NetsensClient netsensClient = new NetsensClient("http://live.netsens.it/export/xml_export_2A.php", "cnit", "cnit_pwd", "723");
		List<Meter> meterList = netsensClient.getTimerangeMeasurement("QS", 1479798000000L, 1479811287000L);
		for (Meter m : meterList) {
			System.out.println(m.toString());
		}
	}

}
