package com.open.taskagile.web.api

import com.open.taskagile.web.response.ApiResponse
import com.open.taskagile.web.response.Responsor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping(API_ROOT)
class CurrentUserController {
  @GetMapping(ME)
  fun current(@AuthenticationPrincipal principal: Mono<UserDetails>): Mono<ResponseEntity<ApiResponse>> {
    return principal.map{
      val response = ApiResponse.createApiResult(HttpStatus.OK, "success")
      response.add("name", it.username)
      response.add("roles", AuthorityUtils.authorityListToSet(it.authorities))
      Responsor.ok(response)
    }
  }
}
