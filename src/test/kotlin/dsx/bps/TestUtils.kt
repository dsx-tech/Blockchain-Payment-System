package dsx.bps

import java.net.URI

class TestUtils {
    companion object {
        fun getResourcePath(resource: String) : String {
            return URI(javaClass.classLoader.getResource(resource).toString()).path;
        }
    }
}