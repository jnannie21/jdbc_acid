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
    public void dataBaseInitialState_AddingStudentRecords_Success() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                "17",
                "0",
                "2"
        );
        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(3, students.size());
        Assertions.assertEquals("Andrei", students.get(2).getFirstName());
        Assertions.assertEquals("0", students.get(2).getPoints());
    }

    @Test
    public void dataBaseInitialState_GettingStudentRecords_Success() {
        StudentService studentService = new StudentService(ds);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("dima", students.get(0).getFirstName());
        Assertions.assertEquals("2", students.get(1).getTeamId());
    }

    @Test
    public void dataBaseInitialState_UpdatingStudentRecords_Success() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "1",
                "Andrei",
                "Andreev",
                "17",
                "0",
                "2"
        );
        studentService.update(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Andrei", students.get(0).getFirstName());
        Assertions.assertEquals("0", students.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_DeletingStudentRecords_Success() {
        StudentService studentService = new StudentService(ds);

        studentService.delete("1");

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals("vasia", students.get(0).getFirstName());
        Assertions.assertEquals("2", students.get(0).getTeamId());
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

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
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

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateFirstNameNotNullConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                null,
                "Andreev",
                "17",
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateFirstNameNotEmptyConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "",
                "Andreev",
                "17",
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateLastNameNotNullConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                null,
                "17",
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateLastNameNotEmptyConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                "",
                "17",
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateAgeNotNullConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                "last_name",
                null,
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateAgeBiggerThanZeroConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                "last_name",
                "0",
                "0",
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolatePointsNotNullConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                "last_name",
                "123",
                null,
                "2"
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateTeamIdNotNullConstraint_Failure() {
        StudentService studentService = new StudentService(ds);

        Student newStudent = new Student(
                "first_name",
                "last_name",
                "123",
                "0",
                null
        );

        studentService.insert(newStudent);

        List<Student> students = studentService.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_AddingTeamRecords_Success() {
        TeamService teamService = new TeamService(ds);

        Team newStudent = new Team(
                "black",
                "-123"
        );
        teamService.insert(newStudent);

        List<Team> teams = teamService.getAll();
        Assertions.assertEquals(3, teams.size());
        Assertions.assertEquals("black", teams.get(2).getColor());
        Assertions.assertEquals("-123", teams.get(2).getPoints());
    }

    @Test
    public void dataBaseInitialState_GettingTeamRecords_Success() {
        StudentService studentService = new StudentService(ds);

        TeamService teamService = new TeamService(ds);
        List<Team> teams = teamService.getAll();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("green", teams.get(0).getColor());
    }

    @Test
    public void dataBaseInitialState_UpdatingTeamRecords_Success() {
        TeamService teamService = new TeamService(ds);

        Team newTeam = new Team(
                "1",
                "black",
                "-19"
        );
        teamService.update(newTeam);

        List<Team> teams = teamService.getAll();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("black", teams.get(0).getColor());
        Assertions.assertEquals("-19", teams.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_DeletingTeamRecords_Success() {
        TeamService teamService = new TeamService(ds);

        teamService.delete("1");

        List<Team> teams = teamService.getAll();
        Assertions.assertEquals(1, teams.size());
        Assertions.assertEquals("red", teams.get(0).getColor());
        Assertions.assertEquals("15", teams.get(0).getPoints());
    }


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