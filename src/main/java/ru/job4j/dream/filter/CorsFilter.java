package ru.job4j.dream.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс CORSFilter - фильтр CORS, обрабатывает междоменные запросы.
 *
 * @author Evgeniy Zaytsev
 * @version 1.0
 */
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader(
                "Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        chain.doFilter(request, servletResponse);
    }
}