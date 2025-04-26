@file:JvmName("IPv4AddressExtensions")

package com.near_reality.network

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress

/**
 * Helper extension functions for retrieving the IPv4 address.
 *
 * @author Jire
 */

fun InetAddress.ipv4Address(): Inet4Address =
    if (this is Inet4Address) this
    else throw UnsupportedOperationException("Only IPv4 addresses supported")

fun SocketAddress.ipv4Address(): Inet4Address =
    if (this is InetSocketAddress) address.ipv4Address()
    else throw UnsupportedOperationException("Only IP addresses supported")

fun Channel.ipv4Address(): Inet4Address =
    remoteAddress().ipv4Address()

fun ChannelHandlerContext.ipv4Address(): Inet4Address =
    channel().ipv4Address()