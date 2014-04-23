package eu.tomylobo.spaceengineers.beans;

import com.jme3.math.Vector3f;

import javax.xml.bind.annotation.XmlAttribute;

public class Vector3 {
    @XmlAttribute
    public double x;

    @XmlAttribute
    public double y;

    @XmlAttribute
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3f toJMEVector() {
        return new Vector3f((float) x, (float) y, (float) z);
    }
}
