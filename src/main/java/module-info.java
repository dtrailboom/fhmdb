module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires jakarta.annotation;
    requires spring.web;
    requires spring.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires spring.beans;
    requires jackson.databind.nullable;
    requires jakarta.validation;

    opens org.openapitools.client.model to com.fasterxml.jackson.databind;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
}