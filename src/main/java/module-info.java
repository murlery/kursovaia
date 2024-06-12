module org.example.kursovaia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.kursovaia to javafx.fxml;
    opens org.example.kursovaia.domain to javafx.base;
    exports org.example.kursovaia;
    exports org.example.kursovaia.view;
    opens org.example.kursovaia.view to javafx.fxml;
}