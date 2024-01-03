package com.open.taskagile.web.response

import org.springframework.http.HttpStatus
import java.util.HashMap

class ApiResponse: HashMap<String, Any>() {
  companion object {
    private const val MESSAGE_KEY = "message"
    private const val STATUS_KEY = "status"

    fun createApiResult(status:HttpStatus, message:String): ApiResponse {
      val result = ApiResponse()
      result[STATUS_KEY] = status
      result[MESSAGE_KEY] = message
      return result
    }
  }

  fun add(key:String, value: Any): ApiResponse {
    this[key] = value
    return this
  }
}
