module ECAManagementSystem {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.media;
    requires java.sql;
    requires mysql.connector.java;
    requires org.apache.poi.poi;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires AnimateFX;
    requires controlsfx;
    requires TrayTester;

    opens project;

    opens project.views;
    opens project.views.ui.cf_views;
    opens project.views.ui.student_views;
    opens project.views.ui.admin_views;

    opens project.database;
    opens project.database.profile_pics;

    opens project.models;

    opens project.middlewares;

    opens project.controllers.cf_controllers;
    opens project.controllers.student_controllers;
    opens project.controllers.admin_controllers;

    opens project.resources;

    opens project.enums;

    opens project.services.student_services;
    opens project.services.cf_services;
    opens project.services.admin_services;
}