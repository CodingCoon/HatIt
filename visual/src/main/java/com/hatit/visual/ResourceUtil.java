package com.hatit.visual;

import javafx.scene.image.Image;

import java.net.URL;

public class ResourceUtil {
    //_______________________________________________ Parameters
    public static final Image POWER_1 = load("/power.png");
    public static final Image POWER_2 = load("/power_2.png");
    public static final Image SETTINGS_1 = load("/settings_1.png");
    public static final Image SETTINGS_2 = load("/settings_2.png");
    public static final Image ADD_ITEM = load("/add_item.png");
    public static final Image STAR = load("/star.png");
    public static final Image STAR_OUTLINED = load("/star-outline.png");
    public static final Image ADD_TOURNAMENT = load("/tournament.png");
    public static final Image ADD_PLAYER = load("/account-plus.png");
    public static final Image ADD_CRITERIA = load("/star-plus.png");
    public static final Image ADD_TAG = load("/tag-plus.png");
    public static final Image SAVE = load("/content-save.png");
    public static final Image DOWNLOAD = load("/download.png");
    public static final Image UPLOAD = load("/upload.png");
    public static final Image NEXT = load("/arrow-right-bold.png");
    public static final Image PREVIOUS = load("/arrow-left-bold.png");
    public static final Image DELETE = load("/delete.png");

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    private static Image load(String resourceName) {
        URL resource = ResourceUtil.class.getResource(resourceName);
        assert resource != null;
        return new Image(resource.toExternalForm());
    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
