package com.ralphevmanzano.catlibrary.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

val catRetrofitQualifier = object : Qualifier {
    override val value: QualifierValue
        get() = "CatRetrofit"
}

val imageRetrofitQualifier = object : Qualifier {
    override val value: QualifierValue
        get() = "ImageRetrofit"
}