package com.keygenqt.mylibrary.common

import com.keygenqt.mylibrary.users.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.http.HttpMethod.*
import org.springframework.security.config.annotation.authentication.builders.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.security.crypto.password.*
import org.springframework.security.web.authentication.*
import javax.annotation.*
import javax.sql.*

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        const val SECRET_KEY = "8e5ONnewmseOnHvxEFuNxeUxeM9jvYWUKlkhdBdjkcDdDfu1azypNiN409y1QaDCFV"

        const val ROLE_USER = "USER"
        const val ROLE_ADMIN = "ADMIN"
    }

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @PostConstruct
    fun addFirstUser() {
        if (repository.count() == 0L) {
            repository.save(User(
                image = "https://www.amazon.com/avatar/default/amzn1.account.AF53E22MXLWU7FIMB27VQXVQQPHQ?square=true&max_width=460",
                email = "admin@gmail.com",
                login = "admin",
                password = passwordEncoder().encode("12345"),
                role = ROLE_ADMIN)
            )
            repository.save(User(
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
            .usersByUsernameQuery("select email,password,enabled from users where email=?")
            .authoritiesByUsernameQuery("select email, role from users where email=?")
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .addFilterAfter(JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(POST, "/login").permitAll()
            .anyRequest().authenticated()
    }
}