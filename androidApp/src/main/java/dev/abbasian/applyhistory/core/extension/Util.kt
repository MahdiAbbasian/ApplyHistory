package dev.abbasian.applyhistory.core.extension

fun isNumber(value: String): Boolean {
    return value.isEmpty() || Regex("^\\d+\$").matches(value)
}

fun isWebsiteValid(url: String): Boolean {
    val urlPattern = "^https?://[\\w-]+(\\.[\\w-]+)+(/.*)?$"
    return Regex(urlPattern).matches(url)
}

fun isProtocolValid(url: String): Boolean {
    return url.startsWith("http://") || url.startsWith("https://")
}