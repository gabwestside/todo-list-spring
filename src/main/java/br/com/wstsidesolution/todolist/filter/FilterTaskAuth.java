package br.com.wstsidesolution.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var authorization = request.getHeader("Authorization");

    var encodedAuthorization = authorization.substring("Basic".length()).trim();

    byte[] decodedAuthorization = Base64.getDecoder().decode(encodedAuthorization);

    var stringAuthorization = new String(decodedAuthorization);

    String[] credentials = stringAuthorization.split(":");
    String username = credentials[0];
    String password = credentials[1];

    filterChain.doFilter(request, response);
  }

}
