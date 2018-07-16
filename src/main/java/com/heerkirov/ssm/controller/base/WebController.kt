package com.heerkirov.ssm.controller.base

import javax.servlet.http.HttpServletRequest

abstract class WebController {
    abstract fun request(): HttpServletRequest

}