package de.hochschuletrier.gdw.commons.gdx.ashley;

import java.util.Map;

import de.hochschuletrier.gdw.commons.jackson.JacksonMap;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public class EntityInfo {

    public SafeProperties meta;
    @JacksonMap(value = SafeProperties.class)
    public Map<String, SafeProperties> components;
}
