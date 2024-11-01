module com.kss.studentmanagementdesktopclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.kss.studentmanagementdesktopclient to javafx.fxml;
    opens com.kss.studentmanagementdesktopclient.controller to javafx.fxml;

    opens com.kss.studentmanagementdesktopclient.controller.student to javafx.fxml;
    opens com.kss.studentmanagementdesktopclient.controller.subject to javafx.fxml;
    opens com.kss.studentmanagementdesktopclient.controller.teacher to javafx.fxml;
    opens com.kss.studentmanagementdesktopclient.controller.grade to javafx.fxml;

    exports com.kss.studentmanagementdesktopclient;
}