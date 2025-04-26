package com.near_reality.network

import com.near_reality.network.NetworkPipelineConstants.HANDLER
import com.near_reality.network.NetworkPipelineConstants.DECODER
import com.near_reality.network.NetworkPipelineConstants.ENCODER
import com.zenyte.net.handshake.codec.HandshakeDecoder
import com.zenyte.net.handshake.codec.HandshakeEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler

/**
 * Client connection = pipeline
 * @author John J. Woloszyk/Kryeus
 */
class PipelineFactory : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        val pipeline: ChannelPipeline = ch.pipeline()
        //pipeline.addLast("timeout", ReadTimeoutHandler(30))
        pipeline.addLast(DECODER, HandshakeDecoder())
        pipeline.addLast(ENCODER, HandshakeEncoder())
        pipeline.addLast(HANDLER, MasterHandler())
    }

}
