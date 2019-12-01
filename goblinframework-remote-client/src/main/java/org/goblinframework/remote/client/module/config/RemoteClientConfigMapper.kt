package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.io.Serializable

class RemoteClientConfigMapper : Serializable {

  var serializer: SerializerMode? = null
  var compressor: CompressorMode? = null
  var compressionThreshold: CompressionThreshold? = null

  companion object {
    private const val serialVersionUID = 2349386765518738752L
  }
}