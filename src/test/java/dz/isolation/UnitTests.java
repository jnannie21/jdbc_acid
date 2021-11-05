package dz.isolation;

import dz.isolation.dao.StudentDao;
import dz.isolation.dao.TeamDao;
import dz.isolation.service.StudentService;
import dz.isolation.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
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
    private StudentsServlet servlet;
    private StudentService studentService;
    private TeamService teamService;
    private HttpServletRequest reqMock;
    private HttpServletResponse resMock;
    private StudentService studentServiceMock;
    private TeamService teamServiceMock;
    private StudentDao studentDaoMock;
    private TeamDao teamDaoMock;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUpServlet() throws Exception {
        servlet = new StudentsServlet();
        reqMock = mock(HttpServletRequest.class);
        resMock = mock(HttpServletResponse.class);
        studentServiceMock = mock(StudentService.class);
        teamServiceMock = mock(TeamService.class);

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(resMock.getWriter()).thenReturn(writer);

        when(reqMock.getParameter(Mockito.any())).thenReturn("1");
        when(reqMock.getRequestDispatcher("students.jsp")).thenReturn(new RequestDispatcher() {
            @Override
            public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                resMock.getWriter().println("Students and teams tables");
            }

            @Override
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

            }
        });

        FieldSetter.setField(servlet, servlet.getClass().getDeclaredField("studentService"), studentServiceMock);
        FieldSetter.setField(servlet, servlet.getClass().getDeclaredField("teamService"), teamServiceMock);
    }

    @BeforeEach
    public void setUpServices() throws Exception {
        studentService = new StudentService();
        teamService = new TeamService();
        studentDaoMock = mock(StudentDao.class);
        teamDaoMock = mock(TeamDao.class);

        FieldSetter.setField(studentService, studentService.getClass().getDeclaredField("studentDao"), studentDaoMock);
        FieldSetter.setField(teamService, teamService.getClass().getDeclaredField("teamDao"), teamDaoMock);
    }



    private void commonFlow() throws Exception {
        Mockito.verify(studentServiceMock).getAll();
        Mockito.verify(teamServiceMock).getAll();
        Mockito.verify(reqMock, Mockito.times(2)).setAttribute(Mockito.any(), Mockito.any());
        Mockito.verify(reqMock).getRequestDispatcher(Mockito.any());
        assertTrue(stringWriter.toString().contains("Students and teams tables"));
    }

    @Test
    public void studentsServlet_CheckDoGetRequestDispatcherForwardMethod_ForwardWritesToWriter() throws Exception {
        servlet.doGet(reqMock, resMock);

        assertTrue(stringWriter.toString().contains("Students and teams tables"));
    }

    @Test
    public void studentsServlet_CheckDoGetMethod_Success() throws Exception {
        servlet.doGet(reqMock, resMock);

        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodUpdateStudentAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/update_student");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(studentServiceMock).update(Mockito.any());
        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodDeleteStudentAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/delete_student");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(studentServiceMock).delete(Mockito.any());
        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodInsertStudentAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/insert_student");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(studentServiceMock).insert(Mockito.any());
        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodUpdateTeamAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/update_team");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(teamServiceMock).update(Mockito.any());
        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodDeleteTeamAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/delete_team");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(teamServiceMock).delete(Mockito.any());
        commonFlow();
    }

    @Test
    public void studentsServlet_CheckDoPostMethodInsertTeamAction_Success() throws Exception {
        when(reqMock.getServletPath()).thenReturn("/insert_team");

        servlet.doPost(reqMock, resMock);

        Mockito.verify(teamServiceMock).insert(Mockito.any());
        commonFlow();
    }


    // students services
    @Test
    public void studentService_CheckGetAllMethod_Success() throws Exception {
        studentService.getAll();

        Mockito.verify(studentDaoMock).getAll();
    }

    @Test
    public void studentService_CheckInsertMethod_Success() throws Exception {
        studentService.insert(reqMock);

        Mockito.verify(reqMock, Mockito.times(5)).getParameter(Mockito.any());
        Mockito.verify(studentDaoMock).insert(Mockito.any());
    }

    @Test
    public void studentService_CheckUpdateMethod_Success() throws Exception {
        studentService.update(reqMock);

        Mockito.verify(reqMock, Mockito.times(6)).getParameter(Mockito.any());
        Mockito.verify(studentDaoMock).update(Mockito.any());
    }

    @Test
    public void studentService_CheckDeleteMethod_Success() throws Exception {
        studentService.delete(reqMock);

        Mockito.verify(studentDaoMock).delete(Mockito.anyInt());
    }


    // teams services
    @Test
    public void teamService_CheckGetAllMethod_Success() throws Exception {
        teamService.getAll();

        Mockito.verify(teamDaoMock).getAll();
    }

    @Test
    public void teamService_CheckInsertMethod_Success() throws Exception {
        teamService.insert(reqMock);

        Mockito.verify(reqMock, Mockito.times(2)).getParameter(Mockito.any());
        Mockito.verify(teamDaoMock).insert(Mockito.any());
    }

    @Test
    public void teamService_CheckUpdateMethod_Success() throws Exception {
        teamService.update(reqMock);

        Mockito.verify(reqMock, Mockito.times(3)).getParameter(Mockito.any());
        Mockito.verify(teamDaoMock).update(Mockito.any());
    }

    @Test
    public void teamService_CheckDeleteMethod_Success() throws Exception {
        teamService.delete(reqMock);

        Mockito.verify(teamDaoMock).delete(Mockito.anyInt());
    }
}