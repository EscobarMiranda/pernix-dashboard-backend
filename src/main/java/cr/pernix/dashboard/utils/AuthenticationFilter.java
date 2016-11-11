package cr.pernix.dashboard.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cr.pernix.dashboard.services.LoginService;

public class AuthenticationFilter implements javax.servlet.Filter {
    public static final String AUTHENTICATION_HEADER = "Authorization";
    private List<String> excludedPaths = new ArrayList<String>();
    private static final Log LOGGER = LogFactory.getLog(AuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String path = ((HttpServletRequest) request).getRequestURI();
        // If the url is one of excluded paths, then just continue with next
        // filter
        if (this.excludedPaths.contains(path.replaceAll("[0-9]+","*"))) {
            LOGGER.info("Excluded end-point: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

            boolean authenticationStatus = LoginService.getInstance().authenticate(authCredentials);

            if (authenticationStatus) {
                System.out.println("User authenticated, applying next filters..");
                filterChain.doFilter(request, response);
            } else {
                if (response instanceof HttpServletResponse) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.excludedPaths = Arrays.asList(config.getInitParameter("excludedPaths").split(","));
    }
}