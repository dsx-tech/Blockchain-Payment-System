package dsx.bps.crypto.eth

import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer

class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

class KFixedHostPortGenericContainer(imageName: String) : FixedHostPortGenericContainer<KFixedHostPortGenericContainer>(imageName)