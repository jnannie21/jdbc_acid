package dz.isolation;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UnitTests extends Mockito {
    static private StudentsServlet servlet;
    static private HttpServletRequest req;
    static private HttpServletResponse res;
    static private StudentService studentService;
    static private TeamService teamService;

    private static final String URL = "http://localhost:8085/jdbc_acid_war/";


    @BeforeAll
    static public void setUp() {
        req = mock(HttpServletRequest.class);
        res = mock(HttpServletResponse.class);
        studentService = mock(StudentService.class);
        teamService = mock(TeamService.class);
    }

    @Test
    public void studentsServletDoGet_CheckRequestDispatcherForward_ForwardWritesToWriter() throws Exception {

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

        servlet.doGet(req, res);

//        verify(req, atLeast(1)).getParameter("first_name");
//        writer.flush();
        assertTrue(stringWriter.toString().contains("Students and teams tables"));
    }

}