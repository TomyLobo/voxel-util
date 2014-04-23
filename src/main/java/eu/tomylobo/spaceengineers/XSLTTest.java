package eu.tomylobo.spaceengineers;

import eu.tomylobo.spaceengineers.beans.BoneInfo;
import eu.tomylobo.spaceengineers.beans.MyObjectBuilder;
import eu.tomylobo.spaceengineers.beans.Orientation;
import eu.tomylobo.spaceengineers.beans.Vector3;
import eu.tomylobo.spaceengineers.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class XSLTTest {
    public static void main(String[] args) throws Exception {
        final MyObjectBuilder.CubeGrid cubeGrid = new MyObjectBuilder.CubeGrid();

        int r = 5;
        for (int x =-r; x <= r; ++x) for (int y = -r; y <= r; ++y) for (int z = -r; z <= r; ++z) {
            String bitString = "";
            for (int xo = 0; xo < 2; ++xo) for (int yo = 0; yo < 2; ++yo) for (int zo = 0; zo < 2; ++zo) {
                final double xp = x + xo - .5;
                final double yp = y + yo - .5;
                final double zp = z + zo - .5;
                final boolean in = xp * xp + yp * yp + zp * zp <= r * r;
                bitString += in ? "1" : "0";
            }

            switch (bitString) {
                case "00000000":
                    continue;

                case "11111111":
                    cubeGrid.cubeBlocks.add(new MyObjectBuilder.CubeBlock(new Vector3(x, y, z)));
                    continue;

                case "01111111":
                    break;

                default:
                    continue;
            }
            System.out.println(bitString);
            cubeGrid.cubeBlocks.add(new MyObjectBuilder.CubeBlock(new Vector3(x, y, z), new Orientation(), new Vector3(0, 1, 1)));
            cubeGrid.skeleton.add(new BoneInfo(new Vector3(x*2, y*2, z*2), new Vector3(0, 0, 0)));
        }

        writeSectorObject("empty", cubeGrid);
    }

    private static void writeSectorObject(String saveName, MyObjectBuilder.EntityBase bean) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, JAXBException, TransformerException {
        // Find the saved game file
        final String saveFileName = saveName + "\\" + Utils.SAVE_FILE_NAME_SBS;
        final File sourceFile = new File(saveFileName);
        final File targetFile = new File(saveFileName);
        final File backupFileName = new File(saveFileName + "." + System.currentTimeMillis());
        Files.copy(sourceFile.toPath(), backupFileName.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Parse saved game into a DOM
        final Document document = Utils.getDOM(saveName);

        // Find the <SectorObjects> node in the DOM
        final List<Node> nodes = Utils.xPathNodes(document, "/MyObjectBuilder_Sector/SectorObjects");
        final Node sectorObjectsNode = nodes.get(0);

        // Marshal the bean into the <SectorObjects> node
        final JAXBContext jaxbContext = JAXBContext.newInstance(MyObjectBuilder.EntityBase.class);
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        final JAXBElement<MyObjectBuilder.EntityBase> jaxbElement = new JAXBElement<>(new QName(MyObjectBuilder.EntityBase.class.getAnnotation(XmlRootElement.class).name()), MyObjectBuilder.EntityBase.class, bean);
        marshaller.marshal(jaxbElement, sectorObjectsNode);

        // Pretty-print the DOM to stdout
        //Utils.dumpNode(new DOMSource(document), new StreamResult(System.out));

        Utils.dumpNode(new DOMSource(document), new StreamResult(targetFile));
    }

}
