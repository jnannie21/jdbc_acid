package dz.isolation;

import dz.isolation.dao.StudentDao;
import dz.isolation.dao.TeamDao;
import dz.isolation.model.Student;
import dz.isolation.model.Team;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
class IntegrationTests {

    private PostgreSQLContainer container;
    private DataSource ds;

    @BeforeEach
    public void setUp() {
        container = new PostgreSQLContainer("postgres")
                .withDatabaseName("postgres")
                .withUsername("admin")
                .withPassword("admin");
        container
                .withInitScript("init.sql");
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

    @Test
    public void dataBaseInitialState_AddingStudentRecords_Success() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                17,
                0,
                2
        );
        studentDao.insert(newStudent);

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(3, students.size());
        Assertions.assertEquals("Andrei", students.get(2).getFirstName());
        Assertions.assertEquals(0, students.get(2).getPoints());
    }

    @Test
    public void dataBaseInitialState_GettingStudentRecords_Success() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("dima", students.get(0).getFirstName());
        Assertions.assertEquals(2, students.get(1).getTeamId());
    }

    @Test
    public void dataBaseInitialState_UpdatingStudentRecords_Success() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                1,
                "Andrei",
                "Andreev",
                17,
                0,
                2
        );
        studentDao.update(newStudent);

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
        Assertions.assertEquals("Andrei", students.get(0).getFirstName());
        Assertions.assertEquals(0, students.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_DeletingStudentRecords_Success() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        studentDao.delete(1);

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals("vasia", students.get(0).getFirstName());
        Assertions.assertEquals(2, students.get(0).getTeamId());
    }

    @Test
    public void dataBaseInitialState_ViolateAgeConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                -1,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateForeignKeyConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "Andrei",
                "Andreev",
                17,
                0,
                3
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateFirstNameNotNullConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                null,
                "Andreev",
                17,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateFirstNameNotEmptyConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "",
                "Andreev",
                17,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateLastNameNotNullConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "first_name",
                null,
                17,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateLastNameNotEmptyConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "first_name",
                "",
                17,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_ViolateAgeBiggerThanZeroConstraint_Throws() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        Student newStudent = new Student(
                "first_name",
                "last_name",
                0,
                0,
                2
        );

        Assertions.assertThrows(
                SQLException.class,
                () -> studentDao.insert(newStudent),
                "Expected studentDao.insert() to throw, but it didn't"
        );

        List<Student> students = studentDao.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void dataBaseInitialState_AddingTeamRecords_Success() throws Exception {
        TeamDao teamDao = new TeamDao(ds);

        Team newTeam = new Team(
                "black",
                -123
        );
        teamDao.insert(newTeam);

        List<Team> teams = teamDao.getAll();
        Assertions.assertEquals(3, teams.size());
        Assertions.assertEquals("black", teams.get(2).getColor());
        Assertions.assertEquals(-123, teams.get(2).getPoints());
    }

    @Test
    public void dataBaseInitialState_GettingTeamRecords_Success() throws Exception {
        StudentDao studentDao = new StudentDao(ds);

        TeamDao teamDao = new TeamDao(ds);
        List<Team> teams = teamDao.getAll();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("green", teams.get(0).getColor());
    }

    @Test
    public void dataBaseInitialState_UpdatingTeamRecords_Success() throws Exception {
        TeamDao teamDao = new TeamDao(ds);

        Team newTeam = new Team(
                1,
                "black",
                -19
        );
        teamDao.update(newTeam);

        List<Team> teams = teamDao.getAll();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("black", teams.get(0).getColor());
        Assertions.assertEquals(-19, teams.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_DeletingTeamRecords_Success() throws Exception {
        TeamDao teamDao = new TeamDao(ds);
        StudentDao studentDao = new StudentDao(ds);

        studentDao.delete(1);
        teamDao.delete(1);

        List<Team> teams = teamDao.getAll();
        Assertions.assertEquals(1, teams.size());
        Assertions.assertEquals("red", teams.get(0).getColor());
        Assertions.assertEquals(20, teams.get(0).getPoints());
    }

    @Test
    public void dataBaseInitialState_ViolateForeignKeyConstraintDeletingTeam_Throws() throws Exception {
        TeamDao teamDao = new TeamDao(ds);

        Assertions.assertThrows(
                SQLException.class,
                () -> teamDao.delete(1),
                "Expected teamDao.delete() to throw, but it didn't"
        );


        List<Team> teams = teamDao.getAll();
        Assertions.assertEquals(2, teams.size());
        Assertions.assertEquals("green", teams.get(0).getColor());
        Assertions.assertEquals(10, teams.get(0).getPoints());
    }
}