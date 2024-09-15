package com.example.movistarapp

import java.math.BigDecimal

data class ResponseDTO (val name: String,
                   val mobilNumber: String,
                   val balance: BigDecimal,
                   val data: Long,
                   val platform:Platform)


