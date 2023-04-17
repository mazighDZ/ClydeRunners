module com.clyderunners.clyderunners {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.clyderunners to javafx.fxml;
    exports com.clyderunners;
}