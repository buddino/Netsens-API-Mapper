package net.sparkworks.mapper.netsens;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NetSensConverter {

    @Value("${netsens.baseURI}")
    String baseURI;
    private Map<String, String> unitMap;
    private Map<String, MeasureMapper> meterMap;
    private Measure current = new Measure("cur", "mA", 10.0);
    private Measure voltage = new Measure("voltage", "mV", 10.0);
    private Measure activePower = new Measure("actp", "mW", 10.0);
    private Measure reactivePower = new Measure("reactp", "mVAR", 10.0);
    private Measure apparentPower = new Measure("appp", "mVA", 10.0);
    private Measure powerfactor = new Measure("pf", "", 0.001);
    private Measure activeEnergy = new Measure("actcon", "mWh", 10.0);
    private Measure reactiveEnergy = new Measure("reactcon", "mVARh", 10.0);
    private Measure apparentEnergy = new Measure("appcon", "mVAh", 10.0);

    //TODO Load mapping from properties file
    public NetSensConverter() {
        unitMap = new HashMap<>();
        meterMap = new HashMap<>();


        //Sensors mapping
        //Geometri - First Floor
        //FIXME parameters names
        String geomFF = "Geom/1F";
        meterMap.put("Geometri-1F-Current1", new MeasureMapper(current, geomFF, 1));
        meterMap.put("Geometri-1F-Current2", new MeasureMapper(current, geomFF, 2));
        meterMap.put("Geometri-1F-Current3", new MeasureMapper(current, geomFF, 3));
        meterMap.put("Geometri-1F-ActivePower", new MeasureMapper(activePower, geomFF));
        meterMap.put("Geometri-1F-ApparentPower", new MeasureMapper(apparentPower, geomFF));
        meterMap.put("Geometri-1F-ReactivePower", new MeasureMapper(reactivePower, geomFF));
        meterMap.put("Geometri-1F-PowerFactor", new MeasureMapper(powerfactor, geomFF));
        meterMap.put("Geometri-1F-ActiveEnergy", new MeasureMapper(activeEnergy, geomFF));
        meterMap.put("Geometri-1F-ReactiveEnergy", new MeasureMapper(reactiveEnergy, geomFF));
        meterMap.put("Geometri-1F-ApparentEnergy", new MeasureMapper(apparentEnergy, geomFF));

        //Geometri - Ground Floor
        String geomFG = "Geom/GF";
        meterMap.put("Geometri-GF-Current1", new MeasureMapper(current, geomFG, 1));
        meterMap.put("Geometri-GF-Current2", new MeasureMapper(current, geomFG, 2));
        meterMap.put("Geometri-GF-Current3", new MeasureMapper(current, geomFG, 3));
        meterMap.put("Geometri-GF-PowerFactor", new MeasureMapper(powerfactor, geomFG));
        meterMap.put("Geometri-GF-ActivePower", new MeasureMapper(activePower, geomFG));
        meterMap.put("Geometri-GF-ReactivePower", new MeasureMapper(reactivePower, geomFG));
        meterMap.put("Geometri-GF-ApparentPower", new MeasureMapper(apparentPower, geomFG));
        meterMap.put("Geometri-GF-ActiveEnergy", new MeasureMapper(activeEnergy, geomFG));
        meterMap.put("Geometri-GF-ReactiveEnergy", new MeasureMapper(reactiveEnergy, geomFG));
        meterMap.put("Geometri-GF-ApparentEnergy", new MeasureMapper(apparentEnergy, geomFG));

        //Geometri - Labs - FM
        String labsFM = "Geom/GF/Labs/MP";

        meterMap.put("FM - Current 1", new MeasureMapper(current, labsFM, 1));
        meterMap.put("FM - Current 2", new MeasureMapper(current, labsFM, 2));
        meterMap.put("FM - Current 3", new MeasureMapper(current, labsFM, 3));
        meterMap.put("FM - Power Factor", new MeasureMapper(powerfactor, labsFM));
        meterMap.put("FM - Active Power", new MeasureMapper(activePower, labsFM));
        meterMap.put("FM - Apparent Power", new MeasureMapper(apparentPower, labsFM));
        meterMap.put("FM - Reactive Power", new MeasureMapper(reactivePower, labsFM));
        meterMap.put("FM - Active Energy", new MeasureMapper(activeEnergy, labsFM));
        meterMap.put("FM - Reactive Energy", new MeasureMapper(reactiveEnergy, labsFM));
        meterMap.put("FM - Apparent Energy", new MeasureMapper(apparentEnergy, labsFM));

        // Geometri - Labs - Lighting
        String labsLight = "Geom/GF/Labs/Lighting";
        meterMap.put("Lighting - Current 1", new MeasureMapper(current, labsLight));
        meterMap.put("Lighting - Power Factor", new MeasureMapper(powerfactor, labsLight));
        meterMap.put("Lighting - Active Power", new MeasureMapper(activePower, labsLight));
        meterMap.put("Lighting - Active Energy", new MeasureMapper(activeEnergy, labsLight));

        //Geometri - Rooms
        String rooms = "Geom/1F/Rooms/Lighting";
        meterMap.put("Active Energy", new MeasureMapper(activeEnergy, rooms));
        meterMap.put("Power Factor", new MeasureMapper(powerfactor, rooms));
        meterMap.put("Current", new MeasureMapper(current, rooms));
        meterMap.put("Active Power", new MeasureMapper(activePower, rooms));


        //Hall
        String hall = "QG/Lighting";
        meterMap.put("Hall-Lighting-Current1", new MeasureMapper(current, hall, 1));
        meterMap.put("Hall-Lighting-Current2", new MeasureMapper(current, hall, 2));
        meterMap.put("Hall-Lighting-Current3", new MeasureMapper(current, hall, 3));
        meterMap.put("Hall-Lighting-ActivePower", new MeasureMapper(activePower, hall));
        meterMap.put("Hall-Lighting-ActiveEnergy", new MeasureMapper(activeEnergy, hall));
        meterMap.put("Hall-Lighting-PowerFactor", new MeasureMapper(powerfactor, hall));

        String QG = "QG";
        meterMap.put("QG - Current 1", new MeasureMapper(current, QG, 1));
        meterMap.put("QG - Current 2", new MeasureMapper(current, QG, 2));
        meterMap.put("QG - Current 3", new MeasureMapper(current, QG, 3));
        meterMap.put("QG - Power Factor", new MeasureMapper(powerfactor, QG));
        meterMap.put("QG - Active Power", new MeasureMapper(activePower, QG));
        meterMap.put("QG - Reactive Power", new MeasureMapper(reactivePower, QG));
        meterMap.put("QG - Apparent Power", new MeasureMapper(apparentPower, QG));
        meterMap.put("QG - Active Energy", new MeasureMapper(activeEnergy, QG));
        meterMap.put("QG - Apparent Energy", new MeasureMapper(apparentEnergy, QG));
        meterMap.put("QG - Reactive Energy", new MeasureMapper(reactiveEnergy, QG));

        String QS = "QS";
        meterMap.put("QS - Current 1", new MeasureMapper(current, QS, 1));
        meterMap.put("QS - Current 2", new MeasureMapper(current, QS, 2));
        meterMap.put("QS - Current 3", new MeasureMapper(current, QS, 3));
        meterMap.put("QS - Power Factor", new MeasureMapper(powerfactor, QS));
        meterMap.put("QS - Active Power", new MeasureMapper(activePower, QS));
        meterMap.put("QS - Reactive Power", new MeasureMapper(reactivePower, QS));
        meterMap.put("QS - Apparent Power", new MeasureMapper(apparentPower, QS));
        meterMap.put("QS - Active Energy", new MeasureMapper(activeEnergy, QS));
        meterMap.put("QS - Apparent Energy", new MeasureMapper(apparentEnergy, QS));
        meterMap.put("QS - Reactive Energy", new MeasureMapper(reactiveEnergy, QS));
        //TODO QG and QS from the Cabinet
    }

    /**
     * @param meterName The name of the meter to be mapped
     * @return String URI the meter is mapped to
     */
    public String getURI(String meterName) {
        if (meterMap.containsKey(meterName)) {
            MeasureMapper measureMapper = meterMap.get(meterName);
            return baseURI + "/" + measureMapper.getName();
        } else {
            System.err.println(meterName + " not found");
            return null;
        }
    }

    /**
     * @param meterName The name of the meter to be mapped
     * @param value The measurement value
     * @return double scaled version of the measurement according to the scaleFactor of the associated Measure object
     */

    public double getValue(String meterName, double value) {
        if (meterMap.containsKey(meterName)) {
            MeasureMapper measureMapper = meterMap.get(meterName);
            return measureMapper.getScaleFactor() * value;
        } else {
            System.err.println("\"" + meterName + "\" not found");
            return 0.0;
        }
    }

    //TODO Timestamp converter?

}
