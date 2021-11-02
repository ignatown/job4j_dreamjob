package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CandidateServletTest {
    @Test
    public void whenCreateCandidate() throws IOException, ServletException {
        PsqlStore.instOf().truncateTable("candidate");
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Some Java Developer");
        when(req.getParameter("city")).thenReturn("1");
        new CandidateServelet().doPost(req, mock(HttpServletResponse.class));
        assertThat(PsqlStore.instOf().findCandidateById(1).getName(), is("Some Java Developer"));
    }
}