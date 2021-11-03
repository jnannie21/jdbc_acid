package dz.isolation;

import dz.isolation.model.Student;
import dz.isolation.service.StudentService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class IntegrationTests extends Mockito {

    private static final String URL = "http://localhost:8085/jdbc_acid_war/";
    private static PostgreSQLContainer container;

    @BeforeAll
    public static void setUp() {
        container = new PostgreSQLContainer("postgres")
                .withDatabaseName("postgres")
                .withUsername("admin")
                .withPassword("admin");
        container
                .withInitScript("q.sql");
        container.start();
    }

    @Test
    public void studentService_ChangeTeamColorAndSubmitForm_ChangesApplied() throws Exception {
        StudentService studentService = new StudentService(getDataSource(container.getJdbcUrl()));

        List<Student> students = studentService.getStudents();
        Assertions.assertEquals(3, students.size());
        Assertions.assertEquals("dima", students.get(0).getFirstName());
    }

    private DataSource getDataSource(String jdbcUrl) {
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