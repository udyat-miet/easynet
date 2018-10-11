package miet.udyat.easynet;

import miet.udyat.easynet.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @EnableWebSecurity
  static class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
      // setup authentication for various url end-points
      httpSecurity
          .authorizeRequests()
            .antMatchers("/polls/setup", "/upload_masters").hasAuthority("admin")
            .antMatchers("/views/review", "/questions/review").hasAnyAuthority("moderator", "admin")
            .antMatchers("/about").permitAll()
            .anyRequest().authenticated()
          .and().formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .permitAll()
          .and().rememberMe()
            .key("access_token")
            .tokenValiditySeconds(604800)
            .alwaysRemember(true)
          .and().logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout=true")
            .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
      // serve static files without authentication
      web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    public UserDetailsService userDetailsService() {
      return (username) -> {
        try {
          return userRepository.findByUsername(username).get(0);
        } catch (Exception e) {
          throw new UsernameNotFoundException(e.getLocalizedMessage());
        }
      };
    }

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
      authBuilder
          .userDetailsService(userDetailsService())
          .passwordEncoder(new BCryptPasswordEncoder());
    }
  }
}
