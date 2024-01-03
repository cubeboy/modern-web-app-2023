package com.open.taskagile.web.response

import org.springframework.http.HttpStatus
import java.util.HashMap

class ApiResponse: HashMap<String, Any>() {
  companion object {
    private const val MESSAGE_KEY = "message"
    private const val STATUS_KEY = "status"

    public fun createApiResult(status:HttpStatus, message:String): ApiResponse {
      val result = ApiResponse()
      result[STATUS_KEY] = status
      result[MESSAGE_KEY] = message
      return result
    }
  }
}
