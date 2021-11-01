package dz.isolation;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentsServletTest extends Mockito {
    static private StudentsServlet servlet;
    static private HttpServletRequest req;
    static private HttpServletResponse res;
    static private StudentService studentService;
    static private TeamService teamService;

    private static final String URL = "http://localhost:8085";

    @BeforeAll
    static public void setUp() {
        req = mock(HttpServletRequest.class);
        res = mock(HttpServletResponse.class);
        studentService = mock(StudentService.class);
        teamService = mock(TeamService.class);
    }

    @Test
    public void testServlet() throws Exception {

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);

        when(req.getRequestDispatcher("students.jsp")).thenReturn(new RequestDispatcher() {
            @Override
            public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                res.getWriter().println("Students and teams tables");
            }

            @Override
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

            }
        });

        StudentsServlet servlet = new StudentsServlet();

        FieldSetter.setField(servlet, servlet.getClass().getDeclaredField("studentService"), studentService);
        FieldSetter.setField(servlet, servlet.getClass().getDeclaredField("teamService"), teamService);

//        servlet.init();
        servlet.doGet(req, res);

//        verify(req, atLeast(1)).getParameter("first_name");
//        writer.flush();
//        System.out.println(stringWriter.toString());
        assertTrue(stringWriter.toString().contains("Students and teams tables"));
    }

    @Test
    void doGet() {

//        StudentsServlet dataServiceMock = Mockito.mock(StudentsServlet.class);
//
//        List<String> data = new ArrayList<>();
//        data.add("dataItem");
//        Mockito.when(dataServiceMock.doGet(HttpServletRequest req, HttpServletResponse resp)).thenReturn(data);
//
//        dataService.getDataById("a");
//        dataService.getDataById("b");
//        Mockito.verify(dataService, Mockito.times(2)).getDataById(Mockito.any());
//        Mockito.verify(dataService, Mockito.times(1)).getDataById("a");
//        Mockito.verify(dataService, Mockito.never()).getDataById("c");
//
//        dataService.getDataById("c");
//        Mockito.verify(dataService, Mockito.times(1)).getDataById("c");
//        Mockito.verifyNoMoreInteractions(dataService);
    }

    @Test
    void doPost() {
        TeamService teamServiceMock = Mockito.mock(TeamService.class);
    }




//integration tests

    @Test
    public void mainPage_CheckingTitle_EqualsToPredefinedValues() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage(URL);
            Assertions.assertEquals("Students and teams table", page.getTitleText());
        }
    }

    @Test
    public void mainPage_ChangeTeamColorAndSubmitForm_ChangesApplied() throws Exception {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page1 = webClient.getPage(URL);

            HtmlForm form = page1.getFormByName("change_team1");
            HtmlButton button = form.getButtonByName("submit_change_team");
            HtmlTextInput textField = form.getInputByName("color");

            textField.setText("white");

            HtmlPage page2 = button.click();
            String pageAsText = page2.asText();
            Assertions.assertTrue(pageAsText.contains("white"));
        }
    }

}