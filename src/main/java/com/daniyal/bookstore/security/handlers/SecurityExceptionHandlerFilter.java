package com.daniyal.bookstore.security.handlers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class SecurityExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (NoHandlerFoundException ex)
        {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }catch (AuthenticationException ex) {
            // Delegate exception handling to Spring MVC exception resolver
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }catch (AccessDeniedException ex)
        {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }catch (AuthorizationDeniedException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }catch(ServletException ex)
        {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }catch(Exception ex)
        {
            System.out.println(ex.getClass().getName());
        }
        // Catch other exceptions (e.g., AccessDeniedException) as needed
    }
}
