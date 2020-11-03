package com.keygenqt.mylibrary.common

import com.keygenqt.mylibrary.users.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.security.config.annotation.authentication.builders.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.core.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.security.crypto.password.*
import org.springframework.security.web.authentication.logout.*
import org.springframework.security.web.header.writers.*
import javax.annotation.*
import javax.servlet.http.*
import javax.sql.*

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        const val ROLE_USER = "USER"
        const val ROLE_ADMIN = "ADMIN"
    }

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @PostConstruct
    fun addFirstUser() {
        if (userRepository.count() == 0L) {
            userRepository.save(User(
                image = "https://www.amazon.com/avatar/default/amzn1.account.AF53E22MXLWU7FIMB27VQXVQQPHQ?square=true&max_width=460",
                email = "admin@gmail.com",
                login = "admin",
                password = passwordEncoder().encode("12345"),
                role = ROLE_ADMIN)
            )
            userRepository.save(User(
                image = "https://www.amazon.com/avatar/default/amzn1.account.AGMEOBVVJKNYMOJRQUZVDMXXE5OA?square=true&max_width=460",
                email = "keygenqt@gmail.com",
                login = "keygenqt",
                password = passwordEncoder().encode("12345"),
                role = ROLE_USER)
            )
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select login,password,enabled from users where login=?")
            .authoritiesByUsernameQuery("select login, role from users where login=?")
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/").hasAnyAuthority(ROLE_ADMIN)
            .antMatchers("/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)

            // not working logout, @todo oauth2
            .and()
            .formLogin()

            .and()
            .logout()
            .logoutUrl("/logout")
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .addLogoutHandler(HeaderWriterLogoutHandler(ClearSiteDataHeaderWriter(
                ClearSiteDataHeaderWriter.Directive.CACHE,
                ClearSiteDataHeaderWriter.Directive.EXECUTION_CONTEXTS,
                ClearSiteDataHeaderWriter.Directive.COOKIES,
                ClearSiteDataHeaderWriter.Directive.STORAGE)))
            .logoutSuccessUrl("/login")
    }

}