package mn.icode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Олон нийт нэвтрэхгүйгээр чөлөөтэй үзэх замууд (/doctors/** нэмэгдсэн)
                .requestMatchers("/", "/doctors/**", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                // 2. Зөвхөн Админ хандах замууд
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 3. Үйлчлүүлэгч цаг захиалах, харах замууд
                .requestMatchers("/customer/**", "/my-appointments", "/appointments/**").hasRole("CUSTOMER")

                // 4. Бусад хүсэлт заавал нэвтэрсэн байх
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}