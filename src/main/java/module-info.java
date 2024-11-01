module com.kss.studentmanagementdesktopclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.kss.studentmanagementdesktopclient to javafx.fxml;
    opens com.kss.studentmanagementdesktopclient.controller to javafx.fxml;
    exports com.kss.studentmanagementdesktopclient;
    opens com.kss.studentmanagementdesktopclient.controller.student to javafx.fxml;
}