package dlbcsemse02_d.project.domain

import dlbcsemse02_d.project.infrastructure.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
