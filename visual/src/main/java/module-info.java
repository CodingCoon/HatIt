module visual {

    requires hatit.data;
    requires hatit.io;

    requires javafx.controls;
    requires javafx.graphics;

    exports com.hatit.visual to javafx.graphics;
    exports com.hatit.visual.blue to javafx.graphics;
    exports com.hatit.visual.black to javafx.graphics;
    exports com.hatit.visual.beige to javafx.graphics;
    exports com.hatit.visual.beige.tournament to javafx.graphics;
    exports com.hatit.visual.beige.criteria to javafx.graphics;
    exports com.hatit.visual.beige.player to javafx.graphics;

    exports com.hatit.visual.common;
}