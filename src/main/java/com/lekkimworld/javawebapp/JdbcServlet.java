package com.lekkimworld.javawebapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.MessageFormat;

import org.apache.commons.dbcp2.BasicDataSource;

@WebServlet("/jdbc")
public class JdbcServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private BasicDataSource connectionPool = new BasicDataSource();

   public JdbcServlet() {
      try {
         final boolean isMariaDb = System.getenv("JDBC_DATABASE_URL").indexOf("jdbc:mysql://") == 0;
         System.out.println(MessageFormat.format("Read JDBC_DATABASE_URL as <{0}> - isMariaDb <{1}>", System.getenv("JDBC_DATABASE_URL"), isMariaDb));
         if (isMariaDb) {
            connectionPool.setDriverClassName("org.mariadb.jdbc.Driver");
         } else {
            connectionPool.setDriverClassName("org.postgresql.Driver");
         }
         connectionPool.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
         connectionPool.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
         connectionPool.setUrl(System.getenv("JDBC_DATABASE_URL"));
         connectionPool.setInitialSize(1);

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      resp.setContentType("text/plain");
      StringBuilder b = new StringBuilder();

      try {
         Connection connection = this.connectionPool.getConnection();
         Statement stmt = connection.createStatement();
         stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
         stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
         ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
         while (rs.next()) {
            b.append("Read from DB: " + rs.getTimestamp("tick") + "\n");
         }
         resp.getWriter().write(b.toString());
         
      } catch (Exception e) {
         throw new ServletException(e);
      }
   }

}
