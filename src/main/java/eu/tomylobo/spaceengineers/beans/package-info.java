@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = double.class, value = DoubleAdapter.class)
})
package eu.tomylobo.spaceengineers.beans;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
