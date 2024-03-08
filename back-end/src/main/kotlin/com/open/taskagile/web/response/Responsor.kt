package com.open.taskagile.web.response

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class Responsor {
  companion object {
    fun createSuccess(id: Long, message: String): ResponseEntity<ApiResponse> {
      val response = ApiResponse.createApiResult(HttpStatus.CREATED, message)
        .add("id", id)
      return ResponseEntity.status(201).body(response)
    }

    fun responseConflict(message:String): ResponseEntity<ApiResponse> {
      val response = ApiResponse.createApiResult(HttpStatus.CONFLICT, message)
      return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    fun ok(payload:ApiResponse): ResponseEntity<ApiResponse> {
      return ResponseEntity.ok(payload)
    }

    fun ok(payload:ApiResponse, httpHeaders: HttpHeaders): ResponseEntity<ApiResponse> {
      return ResponseEntity(payload, httpHeaders, HttpStatus.OK)
    }
  }
}
