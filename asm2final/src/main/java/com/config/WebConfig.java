package com.config;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com")
public class WebConfig implements WebMvcConfigurer {

	 // Spring + Thymeleaf need this
	  @Autowired
	  private ApplicationContext applicationContext;

	  @Override
	  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	      registry.addResourceHandler("/assets/css/**").addResourceLocations("/assets/css/");
	      registry.addResourceHandler("/assets/js/**").addResourceLocations("/assets/js/");
	      registry.addResourceHandler("/assets/fonts/**").addResourceLocations("/assets/fonts/");
	      registry.addResourceHandler("/assets/images/**").addResourceLocations("/assets/images/");
	      registry.addResourceHandler("/assets/file-upload/**").addResourceLocations("/assets/file-upload/");
	      registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
	  }


	  // Spring + Thymeleaf
	  @Bean
	  public SpringResourceTemplateResolver templateResolver() {
	      SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	      templateResolver.setApplicationContext(this.applicationContext);
	      templateResolver.setPrefix("/WEB-INF/view/");
	      templateResolver.setSuffix(".html");
	      templateResolver.setTemplateMode(TemplateMode.HTML);
	      templateResolver.setCacheable(true);
	      templateResolver.setCharacterEncoding("UTF-8");
	      return templateResolver;
	  }

	  // Spring + Thymeleaf
	  @Bean
	  public SpringTemplateEngine templateEngine() {
	      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	      templateEngine.setTemplateResolver(templateResolver());
	      templateEngine.setEnableSpringELCompiler(true);
	      
	      templateEngine.setAdditionalDialects(new HashSet<IDialect>(Arrays.asList(securityDialect())));;
	      return templateEngine;
	  }

	  // Spring + Thymeleaf
	  // Configure Thymeleaf View Resolver
	  @Bean
	  public ThymeleafViewResolver viewResolver() {
	      ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	      viewResolver.setTemplateEngine(templateEngine());
	      viewResolver.setCharacterEncoding("UTF-8");

	      return viewResolver;
	  }

	    @Bean
	    public SpringSecurityDialect securityDialect() {
	         return new SpringSecurityDialect();
	    }
	    
	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    

	    @Bean
	    public CommonsMultipartResolver multipartResolver() {
	        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	        resolver.setDefaultEncoding("utf-8");
	        resolver.setMaxUploadSize(50000000);
	        resolver.setMaxUploadSizePerFile(5000000);
	        resolver.setMaxInMemorySize(5000000);
	        return resolver;
	    }

}
