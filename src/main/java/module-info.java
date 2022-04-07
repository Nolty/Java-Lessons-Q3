module ru.geekbrains.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens ru.geekbrains.client to javafx.fxml;
    exports ru.geekbrains.client;
}