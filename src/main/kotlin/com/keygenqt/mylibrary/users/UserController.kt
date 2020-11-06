package com.keygenqt.mylibrary.users

//import com.keygenqt.mylibrary.genres.GenreAssembler
import com.keygenqt.mylibrary.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.domain.*
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.web.*
import org.springframework.data.web.SortDefault.*
import org.springframework.hateoas.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*

@RestController
class UserController {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var assembler: UserAssembler

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var pagedAssembler: PagedResourcesAssembler<User>

    @PostMapping("/login")
    fun login(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String
    ): User {
        repository.findAllByEmail(email)?.let {
            if (BCryptPasswordEncoder().matches(password, it.password)) {
                it.token = it.email.getJWTToken(it.role)
                return it
            }
        }
        throw ResponseStatusException(FORBIDDEN, "Authorization failed")
    }

    @GetMapping(path = ["/users"]) fun all(
        @PageableDefault(page = 0, size = 20)
        @SortDefaults(SortDefault(sort = ["login"], direction = DESC), SortDefault(sort = ["id"], direction = ASC))
        pageable: Pageable = Pageable.unpaged()
    ): ResponseEntity<PagedModel<EntityModel<User>>> {
        val collModel = pagedAssembler
            .toModel<EntityModel<User>>(repository.findAll(pageable), assembler)
        return ResponseEntity(collModel, OK)
    }

    @GetMapping("/users/{id}") fun one(@PathVariable id: Long): EntityModel<User> {
        return assembler.toModel(
            repository.findById(id).orElseThrow {
                throw ResponseStatusException(NOT_FOUND, "Could not find model $id")
            }
        )
    }
}