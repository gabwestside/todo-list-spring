package br.com.wstsidesolution.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.wstsidesolution.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var servletPath = request.getServletPath();

    if (servletPath.equals("/tasks/")) {

      var authorization = request.getHeader("Authorization");

      var encodedAuthorization = authorization.substring("Basic".length()).trim();

      byte[] decodedAuthorization = Base64.getDecoder().decode(encodedAuthorization);

      var stringAuthorization = new String(decodedAuthorization);

      String[] credentials = stringAuthorization.split(":");
      String username = credentials[0];
      String password = credentials[1];

      var user = userRepository.findByUsername(username);
      var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

      if (user != null && verifiedPassword.verified) {
        filterChain.doFilter(request, response);
      } else {
        response.sendError(401);
      }

    } else {
      filterChain.doFilter(request, response);
    }

  }

}
