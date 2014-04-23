package eu.tomylobo.spaceengineers;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import eu.tomylobo.spaceengineers.beans.Vector3;
import eu.tomylobo.spaceengineers.util.Producer;
import eu.tomylobo.spaceengineers.util.Utils;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathExpressionException;
import java.awt.Color;
import java.io.FileNotFoundException;

import static eu.tomylobo.spaceengineers.util.Utils.xPath;
import static eu.tomylobo.spaceengineers.util.Utils.xPathNodes;

/**
 * Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys.
 */
public class LightPreview extends SimpleApplication {
    private static final String XSI_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema-instance";
    public static final float lightBoxSize = .1f;

    public static void main(String[] args) {
        final LightPreview app = new LightPreview();
        app.setShowSettings(false);

        final AppSettings appSettings = new AppSettings(true);
        appSettings.setFrameRate(60);
        app.setSettings(appSettings);

        app.start();
    }


    private static final Unmarshaller unmarshaller;

    static {
        try {
            unmarshaller = JAXBContext.newInstance(Vector3.class).createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public class SavedGameReader extends Producer<Spatial> {
        private final String saveName;
        private final String filterBeaconName;

        public SavedGameReader(String saveName, String filterBeaconName) {
            this.saveName = saveName;
            this.filterBeaconName = filterBeaconName;
        }

        public void readSavedGame() throws FileNotFoundException, JAXBException, XPathExpressionException, InterruptedException {
            final Material boxMaterial = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
            boxMaterial.getAdditionalRenderState().setPolyOffset(0, 10);

            final Material lightMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            lightMaterial.getAdditionalRenderState().setPolyOffset(0, 10);
            lightMaterial.setColor("Color", ColorRGBA.Yellow);

            final XMLDocument document = new XMLDocument(Utils.getSaveSource(saveName));

            for (XML grid : document.nodes("/MyObjectBuilder_Sector/SectorObjects/MyObjectBuilder_EntityBase[@xsi:type='MyObjectBuilder_CubeGrid']")) {
                final java.util.List<XML> beaconNames = grid.nodes("CubeBlocks/MyObjectBuilder_CubeBlock[@xsi:type='MyObjectBuilder_Beacon']/CustomName");
                if (beaconNames.isEmpty())
                    continue;

                if (!filterBeaconName.equals(beaconNames.get(0).node().getTextContent()))
                    continue;

                int lights = 0;
                for (Node block : xPathNodes(grid.node(), "CubeBlocks/MyObjectBuilder_CubeBlock")) {

                    final Node type = block.getAttributes().getNamedItemNS(XSI_NAMESPACE_URI, "type");
                    final String blockType;
                    if (type == null) {
                        blockType = "MyObjectBuilder_CubeBlock";
                    } else {
                        blockType = type.getTextContent();
                    }

                    final Vector3f position = unmarshaller.unmarshal(xPathNodes(block, "Min").get(0), Vector3.class).getValue().toJMEVector();

                    switch (blockType) {
                        case "MyObjectBuilder_CubeBlock":
                            switch (xPath(block, "SubtypeName/text()")) {
                                case "LargeBlockArmorBlock":
                                case "LargeHeavyBlockArmorBlock":
                                case "SmallBlockArmorBlock":
                                case "SmallHeavyBlockArmorBlock":
                                    final Box box = new Box(position.subtract(.5f, .5f, .5f), position.add(.5f, .5f, .5f));
                                    final Geometry geom = new Geometry("Box", box);
                                    geom.setMaterial(boxMaterial);
                                    produce(geom);
                                    break;
                            }
                            break;

                        case "MyObjectBuilder_InteriorLight":
                            final int lightIndex = ++lights;

                            final Vector3f offset;
                            Vector3f upVector = Vector3f.UNIT_Y;
                            switch (xPath(block, "BlockOrientation/@Forward")) {
                                case "Forward":  offset = Vector3f.UNIT_Z.negate(); break;
                                case "Backward": offset = Vector3f.UNIT_Z; break;
                                case "Left":     offset = Vector3f.UNIT_X.negate(); break;
                                case "Right":    offset = Vector3f.UNIT_X; break;

                                case "Down":
                                    offset = Vector3f.UNIT_Y.negate();
                                    upVector = Vector3f.UNIT_X;
                                    break;

                                case "Up":
                                    offset = Vector3f.UNIT_Y;
                                    upVector = Vector3f.UNIT_X;
                                    break;

                                default:
                                    throw new RuntimeException("Invalid BlockOrientation");
                            }

                            final Vector3f lightBoxCenter = position.subtract(offset.mult(.5f));

                            final Vector3f min = lightBoxCenter.subtract(lightBoxSize, lightBoxSize, lightBoxSize);
                            final Vector3f max = lightBoxCenter.add(lightBoxSize, lightBoxSize, lightBoxSize);

                            final Box box = new Box(min, max);
                            final Geometry geom = new Geometry("Light", box);
                            geom.setMaterial(lightMaterial);
                            produce(geom);

                            final BitmapText labelText = new BitmapText(guiFont);
                            labelText.setQueueBucket(RenderQueue.Bucket.Transparent);
                            labelText.setSize(lightBoxSize * 2);
                            labelText.lookAt(offset, upVector);
                            labelText.setColor(ColorRGBA.Blue);
                            labelText.setText("" + lightIndex);
                            labelText.setLocalTranslation(lightBoxCenter.add(labelText.getLocalRotation().mult(new Vector3f(-.5f * labelText.getLineWidth(), .5f * labelText.getLineHeight(), lightBoxSize))));
                            produce(labelText);

                            break;

                        default:
                            break;
                    }
                }
                break;
            }
        }

        @Override
        public void run() {
            try {
                readSavedGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SavedGameReader savedGameReader;

    @Override
    public void simpleInitApp() {
        final FlyByCamera flyByCamera = this.getFlyByCamera();
        flyByCamera.setMoveSpeed(10);
        flyByCamera.setRotationSpeed(5);

        final Camera camera = getCamera();
        camera.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);

        final InputManager inputManager = this.getInputManager();
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_RCONTROL));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_C));

        final FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        final SSAOFilter ssaoFilter = new SSAOFilter(1, 1, 1, 0);
        fpp.addFilter(ssaoFilter);

        viewPort.addProcessor(fpp);

        final String saveName = "Mine Ink";
        final String filterBeaconName = "Spaaaaace Station";

        savedGameReader = new SavedGameReader(saveName, filterBeaconName);
        savedGameReader.setDaemon(true);
        savedGameReader.start();
    }

    @Override
    public void simpleUpdate(float tpf) {
        try {
            for (long endTime = System.currentTimeMillis() + 100; System.currentTimeMillis() < endTime; ) {
                final Spatial spatial = savedGameReader.consume();
                if (spatial == null)
                    break;

                rootNode.attachChild(spatial);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.simpleUpdate(tpf);
    }
}
