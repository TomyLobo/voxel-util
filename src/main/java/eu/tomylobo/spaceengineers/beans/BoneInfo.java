package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.XmlElement;

public class BoneInfo {
    @XmlElement(name = "BonePosition")
    public Vector3 bonePosition;

    @XmlElement(name = "BoneOffset")
    public Vector3 boneOffset;

    public BoneInfo() {
        this(new Vector3(), new Vector3());
    }
    public BoneInfo(Vector3 bonePosition, Vector3 boneOffset) {
        this.bonePosition = bonePosition;
        this.boneOffset = boneOffset;
    }
}
