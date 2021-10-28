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

public class PostServletTest {
    @Test
    public void whenCreatePost() throws IOException, ServletException {
        PsqlStore.instOf().truncateTable("post");
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Some Java Job");
        new PostServlet().doPost(req, mock(HttpServletResponse.class));
        assertThat(PsqlStore.instOf().findPostById(1).getName(), is("Some Java Job"));
    }
}