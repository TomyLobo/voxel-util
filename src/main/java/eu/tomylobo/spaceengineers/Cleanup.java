package eu.tomylobo.spaceengineers;

import eu.tomylobo.spaceengineers.util.Utils;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Cleanup {
    public static void main(String[] args) throws Exception {
        final String saveFileName = Utils.getSaveFileName("Mine Ink");
        final String backupFileName = saveFileName + ".bak";
        Files.move(Paths.get(saveFileName), Paths.get(backupFileName), StandardCopyOption.REPLACE_EXISTING);

        Utils.transform(
                "/cleanup.xsl",
                new StreamSource(new FileInputStream(backupFileName)),
                new StreamResult(new FileOutputStream(saveFileName))
        );
    }
}
