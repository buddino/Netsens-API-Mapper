package net.sparkworks.mapper.netsens;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Netsens")
public class Meters {

    @XmlElement(name = "Measure")
    private List<Meter> meters;

    public List<Meter> getMeters() {
        return meters;
    }

    @Override
    public String toString() {
        return "Meters{" +
				"meters.txt=" + meters +
				'}';
    }
}
