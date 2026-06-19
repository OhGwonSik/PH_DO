package com.kbph.logistics.configuration;

import java.io.IOException;
import java.util.Collections;

import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kbph.logistics.common.constant.Constants;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-10-28
 * @note : root path servlet config
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-10-28					t.s.park							root path servlet config
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Configuration
public class RootServletConfig {
	@Bean
	public TomcatServletWebServerFactory servletWebServerFactory() { // servlet 생성
		return new TomcatServletWebServerFactory() {
			@Override
			protected void prepareContext(Host host, ServletContextInitializer[] initializers) {
				super.prepareContext(host, initializers);
				StandardContext child = new StandardContext();
				child.addLifecycleListener(new Tomcat.FixContextListener());
				child.setPath("");
				ServletContainerInitializer initializer = getServletContextInitializer(getContextPath());
				child.addServletContainerInitializer(initializer, Collections.emptySet());
				child.setCrossContext(true);
				host.addChild(child);
			}
		};
	}

	private ServletContainerInitializer getServletContextInitializer(String contextPath) {
		return (c, context) -> {
			Servlet servlet = new HttpServlet() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
					try {
						res.sendRedirect(contextPath + Constants.Uri.LOGIN_URI); // 로그인 페이지로 redirect
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

			context.addServlet("root", servlet).addMapping("/*");
		};
	}
}
