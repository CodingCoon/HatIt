package com.hatit.visual;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class ResourceUtil {
    //_______________________________________________ Parameters
    public static final Image POWER = load("/power.png");
    public static final Image ADD_TOURNAMENT = load("/tournament.png");
    public static final Image ADD_CRITERIA = load("/star-plus.png");
    public static final Image ADD_TAG = load("/tag-plus.png");
    public static final Image ADD_PLAYER = load("/account-plus.png");

    public static final Image SAVE = load("/content-save.png");
    public static final Image DELETE = load("/delete.png");

    public static final Image NEXT = load("/arrow-right-bold.png");
    public static final Image PREVIOUS = load("/arrow-left-bold.png");

    public static final Image STAR = load("/star.png");
    public static final Image STAR_OUTLINED = load("/star-outline.png");

    //_______________________________________________ Initialize
    //_______________________________________________ Methods
    private static Image load(String resourceName) {
        URL resource = ResourceUtil.class.getResource(resourceName);
        assert resource != null;
        return new Image(resource.toExternalForm());
    }

    private void toss(String resourceName) {
        URL actualURI = ResourceUtil.class.getResource("");
        assert actualURI != null;
        System.out.println("Resource  " + resourceName);
//        System.out.println("ResourceLoader  " + ResourceUtil.class.getResource(resourceName));
//        System.out.println("Uri       " + actualURI);
//        System.out.println("CL leer   " + ClassLoader.getSystemResource(""));
//        System.out.println("CL res    " + ClassLoader.getSystemResource(resourceName));
//        System.out.println("CL .res   " + ClassLoader.getSystemResource("." + resourceName));
        String resourceUtilFolder = actualURI.toExternalForm();
//        int i = resourceUtilFolder.indexOf("classes/com/hatit/visual");
//        if (i == -1) {
//            return null;
//        }
//        String productionOutFolder = resourceUtilFolder.substring(0, i);
//        String resourceFolder = productionOutFolder + "resources/com/hatit/visual";
//
//
//        File dir = new File(URI.create(resourceFolder));
//        if (dir.exists()) {
//            for (File nextFile : Objects.requireNonNull(dir.listFiles())) {
//                System.out.println(nextFile);
//            }
//        }



    }

    //_______________________________________________ Inner CLasses
    //_______________________________________________ End
}
