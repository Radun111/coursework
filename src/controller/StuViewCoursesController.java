package controller;

import models.CourseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Course;

public class StuViewCoursesController {

    @FXML private TableView<Course> tblCourses;
@FXML private TableColumn<Course, String> colCourseID;
@FXML private TableColumn<Course, String> colCourseName;
@FXML private TableColumn<Course, Integer> colCredits;
@FXML private TableColumn<Course, String> colInstructor;
@FXML private TableColumn<Course, String> colDepartment;
@FXML private TableColumn<Course, String> colPrerequisites;
@FXML private TableColumn<Course, Integer> colMaxCapacity;

    @FXML
    private TextField searchField;
    
    private ObservableList<Course> courseList;

    @FXML
public void initialize() {
    if (colCourseID == null || colCourseName == null) {
        System.out.println("TableColumn is null. Check FXML bindings!");
        return;
    }
    
    colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
    colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));

    loadCourseData();
}


    private void loadCourseData() {
        courseList = FXCollections.observableArrayList(CourseDAO.getAllCourses());

        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPrerequisites.setCellValueFactory(new PropertyValueFactory<>("prerequisites"));
        colMaxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        tblCourses.setItems(courseList);
    }

    private void filterCourses(String keyword) {
        if (keyword.isEmpty()) {
            tblCourses.setItems(courseList);
            return;
        }

        ObservableList<Course> filteredList = FXCollections.observableArrayList();
        for (Course course : courseList) {
            if (course.getCourseName().toLowerCase().contains(keyword.toLowerCase()) ||
                course.getDepartment().toLowerCase().contains(keyword.toLowerCase()) ||
                course.getInstructor().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(course);
            }
        }
        tblCourses.setItems(filteredList);
    }
}
