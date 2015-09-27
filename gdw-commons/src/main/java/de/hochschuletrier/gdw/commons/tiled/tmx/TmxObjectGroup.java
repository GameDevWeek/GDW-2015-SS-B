package de.hochschuletrier.gdw.commons.tiled.tmx;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("objectgroup")
public class TmxObjectGroup extends TmxLayerBase {

    @XStreamImplicit(itemFieldName = "object")
    protected List<TmxObject> objects;

    public List<TmxObject> getObjects() {
        if (objects == null) {
            objects = new ArrayList();
        }
        return this.objects;
    }
}
