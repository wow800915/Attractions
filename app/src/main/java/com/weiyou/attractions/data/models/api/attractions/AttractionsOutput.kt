package com.weiyou.attractions.data.models.api.attractions

data class AttractionsOutput(
    val total: Int,
    val data: List<Attraction>
)

data class Attraction(
    val id: Int,
    val name: String,
    val name_zh: String?,
    val open_status: Int,
    val introduction: String,
    val open_time: String?,
    val zipcode: String,
    val distric: String,
    val address: String,
    val tel: String?,
    val fax: String?,
    val email: String?,
    val months: String,
    val nlat: Double,
    val elong: Double,
    val official_site: String?,
    val facebook: String?,
    val ticket: String?,
    val remind: String?,
    val staytime: String?,
    val modified: String,
    val url: String,
    val category: List<Category>,
    val target: List<Target>,
    val service: List<Service>,
    val friendly: List<Friendly>,
    val images: List<Image>,
    val files: List<File>,
    val links: List<Link>
)

data class Category(
    val id: Int,
    val name: String
)

data class Target(
    val id: Int,
    val name: String
)

data class Service(
    val id: Int,
    val name: String
)

data class Friendly(
    val id: Int,
    val name: String
)

data class Image(
    val src: String,
    val subject: String,
    val ext: String
)

data class File(
    val src: String,
    val subject: String
)

data class Link(
    val src: String,
    val subject: String
)