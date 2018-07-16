package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.BadRequestException
import com.heerkirov.ssm.controller.base.HttpKeyword
import com.heerkirov.ssm.controller.base.NotFoundException
import com.heerkirov.ssm.service.ProductService
import com.heerkirov.ssm.util.FileManager
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RequestMapping("/api/product")
@Controller
class ProductController(@Autowired private val request: HttpServletRequest,
                        @Autowired private val session: HttpSession,
                        @Autowired private val productService: ProductService,
                        @Autowired private val fileManager: FileManager): ApiController() {
    override fun request(): HttpServletRequest = request
    @GetMapping("") @ResponseBody
    fun list(): Any {
        val result = productService.list()
        return result.toMapList()
    }
    @GetMapping("/{id}") @ResponseBody
    fun retrieve(@PathVariable id: String): Any {
        val result = productService.get(id.toInt())?:throw NotFoundException()
        return result.toMap()
    }
    @GetMapping("/{id}/image.{ext}") @ResponseBody
    fun image(@PathVariable id: String, @PathVariable ext: String, response: HttpServletResponse): Any {
        response.contentType = "image/$ext"
        val os = response.outputStream
        val data = fileManager.get("$id.$ext")!!
        os.write(data)
        os.flush()
        os.close()
        return "success"
    }
}