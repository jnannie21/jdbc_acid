package dz.isolation;

import dz.isolation.model.Student;
import dz.isolation.model.Team;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.postgresql.util.PSQLException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class IntegrationTests {

    private static final String URL = "http://localhost:8085/jdbc_acid_war/";
    private static PostgreSQLContainer container;
    private static DataSource ds;

    @BeforeEach
    public void setUp() {
        container = new PostgreSQLContainer("postgres")
                .withDatabaseName("postgres")
                .withUsername("admin")
                .withPassword("admin");
//        container
//                .withInitScript("q.sql");
        container.start();

        ds = getDataSource(container.getJdbcUrl());
    }

    private static DataSource getDataSource(String jdbcUrl) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");
        dataSource.setUrl(jdbcUrl);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(10);
        dataSource.setMaxWaitMillis(-1);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

//    @AfterEach
//    public static void cleanUp() {
//        container.stop();
//    }

    @Test
    public void dataBaseInitialState_GettingRecords_Success() {
        StudentService studentService = new StudentService(ds);
        List<Student> students = studentService.getStudents();

        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("dima", students.get(0).getFirstName());
        Assertions.assertEquals("2", students.get(1).getTeamId());

        TeamService teamService = new TeamService(ds);
        List<Team> teams = teamService.getTeams();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("green", teams.get(0).getColor());
    }

    @Test
    public void dataBaseInitialState_UpdatingRecords_Success() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "1",
                "Andrei",
                "Andreev",
                "17",
                "0",
                "2"
        );
        studentService.updateStudent(newStudent);

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Andrei", students.get(0).getFirstName());
        Assertions.assertEquals("0", students.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_DeletingRecords_Success() {
        StudentService studentService = new StudentService(ds);

        studentService.deleteStudent("1");

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals("vasia", students.get(0).getFirstName());
        Assertions.assertEquals("2", students.get(0).getTeamId());
    }

    @Test
    public void dataBaseInitialState_AddingRecords_Success() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                "17",
                "0",
                "2"
        );
        studentService.addStudent(newStudent);

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(3, students.size());
        Assertions.assertEquals("Andrei", students.get(2).getFirstName());
        Assertions.assertEquals("0", students.get(2).getPoints());
    }

    @Test
    public void dataBaseInitialState_ViolateAgeConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                "-1",
                "0",
                "2"
        );

        studentService.addStudent(newStudent);

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateForeignKeyConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                "17",
                "0",
                "3"
        );

        studentService.addStudent(newStudent);

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(2, students.size());
    }

//    @Test
//    public void dataBaseInitialState_ViolateNullConstraint_Failure() {
//        StudentService studentService = new StudentService(ds);
//
//        Student newStudent = new Student(
//                null,
//                "Andreev",
//                "17",
//                "0",
//                "2"
//        );
//
//        studentService.addStudent(newStudent);
//
//        List<Student> students = studentService.getStudents();
//        Assertions.assertEquals(2, students.size());
//    }






//    @Test
//    public void mainPage_CheckingTitle_EqualsToPredefinedValues() throws Exception {
//        try (final WebClient webClient = new WebClient()) {
//            HtmlPage page = webClient.getPage(URL);
//            Assertions.assertEquals("Students and teams table", page.getTitleText());
//        }
//    }
//
//    @Test
//    public void mainPage_ChangeTeamColorAndSubmitForm_ChangesApplied() throws Exception {
//        try (WebClient webClient = new WebClient()) {
//            HtmlPage page1 = webClient.getPage(URL);
//
//            HtmlForm form = page1.getFormByName("change_team1");
//            HtmlButton button = form.getButtonByName("submit_change_team");
//            HtmlTextInput textField = form.getInputByName("color");
//
//            textField.setText("white");
//
//            HtmlPage page2 = button.click();
//            String pageAsText = page2.asText();
//            Assertions.assertTrue(pageAsText.contains("white"));
//        }
//    }
}