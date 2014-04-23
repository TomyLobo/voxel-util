package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.XmlAttribute;

public class Orientation {
    @XmlAttribute(name = "Forward")
    public final String forward;

    @XmlAttribute(name = "Up")
    public final String up;

    public Orientation() {
        this("Forward", "Up");
    }

    public Orientation(String forward, String up) {
        this.forward = forward;
        this.up = up;
    }
}
