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
    requires ormlite.jdbc;
    requires java.sql;
    requires static lombok;

    opens org.openapitools.client.model to com.fasterxml.jackson.databind;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.domain to ormlite.jdbc;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.domain;
    exports org.openapitools.client.model;
}