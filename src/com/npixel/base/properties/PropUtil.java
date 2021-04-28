package com.npixel.base.properties;

public class PropUtil {
    public static IProperty getProperty(IUpdateable object, String groupId, String propertyId) {
        for (PropertyGroup propertyGroup : object.getPropertyGroups()) {
            if (!propertyGroup.getId().equals(groupId)) {
                continue;
            }

            for (IProperty property : propertyGroup.getProperties()) {
                if (property.getId().equals(propertyId)) {
                    return property;
                }
            }
        }

        return null;
    }
}
