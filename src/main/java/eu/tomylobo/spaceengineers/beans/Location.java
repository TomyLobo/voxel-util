package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.XmlElement;

public class Location {
    @XmlElement(name = "Position")
    public Vector3 position = new Vector3();

    @XmlElement(name = "Forward")
    public Vector3 forward = new Vector3(0, 0, -1);

    @XmlElement(name = "Up")
    public Vector3 up = new Vector3(0, 1, 0);
}
