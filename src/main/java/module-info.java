module ru.geekbrains.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ru.geekbrains.client to javafx.fxml;
    exports ru.geekbrains.client;
}