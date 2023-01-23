package fr.su.mentorataffectationportems.dsrouting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DatasourceRoutingFilter implements Filter {


    private static final Logger LOGGER = LogManager.getLogger(DatasourceRoutingFilter.class);

    /**
     * Pattern permettant d'extraire le code entrepot
     */
    private static final Pattern entrepotPatternMatcher = Pattern.compile(".*\\/entrepots\\/([a-zA-Z0-9-]+)(\\/).*");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String contextPath = request.getRequestURI();
        Matcher m = entrepotPatternMatcher.matcher(contextPath);
        if (m.matches()) {
            ContextHolder.getInstance().use(m.group(1));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


}
