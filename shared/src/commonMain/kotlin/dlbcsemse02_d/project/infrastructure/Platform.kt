package dlbcsemse02_d.project.infrastructure

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
