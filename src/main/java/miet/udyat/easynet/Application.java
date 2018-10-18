package miet.udyat.easynet;

import miet.udyat.easynet.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SpringBootApplication(scanBasePackages = "miet.udyat.easynet")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @EnableWebSecurity
  protected static class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
      // setup authentication for various url end-points
      httpSecurity
          .authorizeRequests()
          .antMatchers("/setup_poll", "/upload_masters").hasAuthority("admin")
          .antMatchers("/view/review", "/question/review").hasAnyAuthority("moderator", "admin")
          .antMatchers("/about").permitAll()
          .anyRequest().authenticated()
          .and().formLogin()
          .loginPage("/login")
          .defaultSuccessUrl("/")
          .failureUrl("/login?s=1")
          .permitAll()
          .and().rememberMe()
          .key("access_token")
          .tokenValiditySeconds(604800)
          .alwaysRemember(true)
          .and().logout()
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/login?s=2")
          .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
      // serve static files without authentication
      web.ignoring().antMatchers("/all.js", "/main.css");
    }


    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
      authBuilder
          .userDetailsService((username) -> userRepository.findByUsername(username))
          .passwordEncoder(new BCryptPasswordEncoder());
    }
  }
}
