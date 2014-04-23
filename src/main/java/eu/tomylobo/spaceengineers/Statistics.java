package eu.tomylobo.spaceengineers;

import eu.tomylobo.spaceengineers.util.Utils;

import javax.xml.transform.stream.StreamResult;

public class Statistics {
    public static void main(String[] args) throws Exception {
        Utils.transform(
                "/statistics.xsl",
                Utils.getSaveSource("Mine Ink"),
                new StreamResult(System.out)
        );
    }

}
