package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.BadRequestException
import com.heerkirov.ssm.controller.base.HttpKeyword
import com.heerkirov.ssm.controller.base.NotFoundException
import com.heerkirov.ssm.service.AdminProductService
import com.heerkirov.ssm.service.ProductService
import com.heerkirov.ssm.util.FileManager
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import java.io.FileNotFoundException
import com.sun.deploy.trace.Trace.flush
import java.io.FileOutputStream



@RequestMapping("/api/admin/product")
@RestController
class AdminProductController(@Autowired private val request: HttpServletRequest,
                             @Autowired private val session: HttpSession,
                             @Autowired private val adminProductService: AdminProductService,
                             @Autowired private val fileManager: FileManager): ApiController() {

    override fun request(): HttpServletRequest = request

    @GetMapping("")
    fun list(): Any {
        return adminProductService.list().toMapList()
    }
    @PostMapping("") @ResponseStatus(HttpStatus.CREATED)
    fun create(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        //val id = (content["id"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toInt()
        val name = (content["name"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString()
        val price = (content["price"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toDouble()
        val discount = (content["discount"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toDouble()
        //val sale = (content["sale"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toInt()
        val stock = (content["stock"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toInt()
        val description = (content["description"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString()
        val useful = (content["useful"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)).toString().toBoolean()
        val result = adminProductService.addProduct(0, name, price, discount, 0, stock, description, useful)
        return result.toMap()
    }
    @GetMapping("/{id}")
    fun retrieve(@PathVariable id: String): Any {
        return adminProductService.get(id.toInt())!!.toMap()
    }
    @RequestMapping("/{id}", method = [RequestMethod.PATCH, RequestMethod.PUT])
    fun update(@PathVariable id: String): Any {
        val product = adminProductService.get(id.toInt())!!
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)

        val name = content["name"]?.toString()
        val price = content["price"]?.toString()?.toDouble()
        val discount = content["discount"]?.toString()?.toDouble()
        //val sale = content["sale"]?.toString()?.toInt()
        val stock = content["stock"]?.toString()?.toInt()
        val description = content["description"]?.toString()
        val useful = content["useful"]?.toString()?.toBoolean()

        val result = adminProductService.update(id.toInt(), name, price, discount, product.sale.toInt(), stock, description, useful)?:throw NotFoundException()
        return result.toMap()
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String): Any {
        if(adminProductService.remove(id.toInt())) return emptyMap<String, Any?>()
        else throw NotFoundException()
    }

    @PostMapping("/{id}/upload")
    fun uploadImage(@PathVariable id: String, @RequestParam("img")uploadFile: MultipartFile?): Any {
        adminProductService.get(id.toInt())!!
        val originalName = uploadFile!!.originalFilename!!
        val point = originalName.indexOfLast { it == '.' }
        val ext = if(point >= 0) originalName.substring(point + 1) else ""

        val success = fileManager.saveSafe(uploadFile, "$id.$ext")

        return mapOf("success" to success)
    }

}