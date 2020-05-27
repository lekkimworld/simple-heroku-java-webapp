package com.lekkimworld.javawebapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      resp.setContentType("text/plain");

      StringBuilder b = new StringBuilder();
      b.append("Hello World! Maven Web Project Example.\n\n");

      b.append("Configuration:\n");
      b.append("mykey = ").append(new Environment().getProperty("mykey")).append("\n\n");
      
      b.append("Environment variables:\n");
      for (String key : System.getenv().keySet()) {
         final String value = System.getenv(key);
         b.append(key).append(" = ").append(value).append('\n');
      }
      resp.getWriter().write(b.toString());
   }

}
