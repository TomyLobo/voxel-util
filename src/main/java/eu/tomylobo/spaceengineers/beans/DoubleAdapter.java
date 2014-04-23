package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.NumberFormat;
import java.util.Locale;

public class DoubleAdapter extends XmlAdapter<String, Double> {
    @Override
    public Double unmarshal(String v) throws Exception {
        return Double.valueOf(v);
    }

    @Override
    public String marshal(Double v) throws Exception {
        return NumberFormat.getNumberInstance(Locale.ROOT).format(v);
    }
}
