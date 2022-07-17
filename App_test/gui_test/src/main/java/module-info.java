module org.openjfx {
    requires javafx.controls;
    requires ormlite.core;
    requires ormlite.jdbc;
    requires java.sql;
    requires sqlite.jdbc;
    requires javafx.base;
    requires javafx.fxml;

    exports org.fazioMonchieri;
    //exports org.fazioMonchieri.models;
    //exports org.fazioMonchieri.data;
    exports org.fazioMonchieri.controllers;


    //opens org.fazioMonchieri.models;
    //opens org.fazioMonchieri.data;
    opens org.fazioMonchieri.controllers;
    
}
