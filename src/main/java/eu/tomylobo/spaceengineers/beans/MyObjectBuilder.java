package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyObjectBuilder {
    @XmlRootElement(name = "MyObjectBuilder_CubeBlock")
    public static class CubeBlock {
        @XmlElement(name = "SubtypeName")
        public String subtypeName;

        @XmlElement(name = "Min")
        public Vector3 min;

        @XmlElement(name = "BlockOrientation")
        public Orientation blockOrientation;

        @XmlElement(name = "ColorMaskHSV")
        public Vector3 colorMaskHSV;

        public CubeBlock() {
            this(new Vector3());
        }

        public CubeBlock(Vector3 min) {
            this(min, new Orientation());
        }

        public CubeBlock(Vector3 min, Orientation blockOrientation) {
            this(min, blockOrientation, new Vector3(0, -1, 0));
        }

        public CubeBlock(Vector3 min, Orientation blockOrientation, Vector3 colorMaskHSV) {
            this(min, blockOrientation, colorMaskHSV, "LargeBlockArmorBlock");
        }

        public CubeBlock(Vector3 min, Orientation blockOrientation, Vector3 colorMaskHSV, String subtypeName) {
            this.subtypeName = subtypeName;
            this.min = min;
            this.blockOrientation = blockOrientation;
            this.colorMaskHSV = colorMaskHSV;
        }
    }

    @XmlRootElement(name = "MyObjectBuilder_EntityBase")
    @XmlSeeAlso(CubeGrid.class)
    public static class EntityBase {
        @XmlElement(name = "EntityId")
        public long entityId = new Random().nextLong();

        @XmlElement(name = "PersistentFlags")
        @XmlList
        public List<String> persistentFlags = Arrays.asList("CastShadows", "InScene"); // TODO: enum

        @XmlElement(name = "PositionAndOrientation")
        public Location positionAndOrientation = new Location();
    }

    @XmlType(name = "MyObjectBuilder_CubeGrid")
    public static class CubeGrid extends EntityBase {
        @XmlElement(name = "GridSizeEnum")
        public GridSize gridSizeEnum = GridSize.Large;

        @XmlElementWrapper(name = "CubeBlocks")
        @XmlElement(name = "MyObjectBuilder_CubeBlock")
        public List<CubeBlock> cubeBlocks = new ArrayList<>();

        @XmlElement(name = "IsStatic")
        public boolean isStatic = true;

        @XmlElementWrapper(name = "Skeleton")
        @XmlElement(name = "BoneInfo")
        public List<BoneInfo> skeleton = new ArrayList<>();

        @XmlElement(name = "LinearVelocity")
        public Vector3 linearVelocity = new Vector3();

        @XmlElement(name = "AngularVelocity")
        public Vector3 angularVelocity = new Vector3();

        @XmlElement(name = "XMirroxPlane", nillable = true)
        public Vector3 xMirrorPlane = null;

        @XmlElement(name = "YMirroxPlane", nillable = true)
        public Vector3 yMirrorPlane = null;

        @XmlElement(name = "ZMirroxPlane", nillable = true)
        public Vector3 zMirrorPlane = null;
    }
}
