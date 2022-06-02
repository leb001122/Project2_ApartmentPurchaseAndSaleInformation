module com.creative.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires org.apache.commons.lang3;

    opens com.creative.gui to javafx.fxml;
    exports com.creative.gui;
    exports network;
    opens network to javafx.fxml;
}