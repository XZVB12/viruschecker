package sk.csirt.viruschecker.client.web.template

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.HtmlContent
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.util.date.*
import kotlinx.html.*
import sk.csirt.viruschecker.client.web.routing.WebRoutes

/**
 * Function that generates HTML for the structure of the page and allows to provide a [block] that will be placed
 * in the content place of the page.
 */
@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.respondDefaultHtml(versions: List<Version> = emptyList(),
                                               visibility: CacheControl.Visibility = CacheControl.Visibility.Private,
                                               title: String = "Virus Checker",
                                               block: DIV.() -> Unit) {
    val content = HtmlContent(HttpStatusCode.OK) {
//        val session = sessions.get<YouKubeSession>()
        head {
            title { +title }
            styleLink("http://yui.yahooapis.com/pure/0.6.0/pure-min.css")
            styleLink("http://yui.yahooapis.com/pure/0.6.0/grids-responsive-min.css")
            styleLink(url(WebRoutes.MainCss()) {
                protocol = URLProtocol.createOrDefault(request.origin.scheme)
            })
        }
        body {
            div("pure-g") {
                div("sidebar pure-u-1 pure-u-md-1-4") {
                    div("header") {
                        div("brand-title") { +title }
//                        div("brand-tagline") {
//                            +"Virus Checker"
//                        }

                        nav("nav") {
                            ul("nav-list") {
                                li("nav-item") {
                                    a(classes = "pure-button",
                                        href = locations.href(WebRoutes.Index())) { +"Home" }
                                }
                                li("nav-item") {
                                        a(classes = "pure-button",
                                            href = locations.href(WebRoutes.ScanFile())) { +"Scanner" }
                                }
                            }
                        }
                    }
                }

                div("content pure-u-1 pure-u-md-3-4") {
                    block()
                }
            }
        }
    }
    content.versions = versions
    content.caching = CachingOptions(
        cacheControl = CacheControl.MaxAge(3600 * 24 * 7, mustRevalidate = true, visibility = visibility, proxyMaxAgeSeconds = null, proxyRevalidate = false),
        expires = (null as? GMTDate?)
    )
    respond(content)
}

