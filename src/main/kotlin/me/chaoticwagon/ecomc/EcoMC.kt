package me.chaoticwagon.ecomc

import me.chaoticwagon.ecomc.claiming.ClaimHandler
import me.chaoticwagon.ecomc.events.*
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.*
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block


class EcoMC {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Initialization
            val minecraftServer: MinecraftServer = MinecraftServer.init()
            val instanceManager: InstanceManager = MinecraftServer.getInstanceManager()

            // Create the instance
            val instanceContainer: InstanceContainer = instanceManager.createInstanceContainer()
            instanceContainer.chunkGenerator = GeneratorDemo()
            instanceContainer.timeRate = 0 // Stop default time
            MojangAuth.init()

            // Add an event callback to specify the spawning instance (and the spawn position)
            val globalEventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()
            globalEventHandler.addListener(PlayerLoginEvent::class.java) { event ->
                val player: Player = event.player
                event.setSpawningInstance(instanceContainer)
                player.respawnPoint = Pos(0.0, 42.0, 0.0)
            }

            // Start the server on port 25565
            minecraftServer.start("0.0.0.0", 25565)

            val claimHandler = ClaimHandler()


            // Events
            val globalEventNode = MinecraftServer.getGlobalEventHandler()

            val instanceEventNode = EventNode.type("instance-listener", EventFilter.INSTANCE)
            val playerEventNode = EventNode.type("player-listener", EventFilter.PLAYER)

            instanceEventNode.addListener(DayNightChange()) // Day cycle listener.
            playerEventNode.addListener(ChatListener()) // Chat listener.
            playerEventNode.addListener(GroupChatListener(claimHandler)) // Group chat listener.
            playerEventNode.addListener(PlayerInteractAtBlock(claimHandler)) // Player interact at block listener.
            playerEventNode.addListener(BlockPlaceEvent(claimHandler)) // Block place listener.
            playerEventNode.addListener(BlockBreakEvent(claimHandler)) // Block break listener.

            globalEventNode.addChild(instanceEventNode)
            globalEventNode.addChild(playerEventNode)

            val dayCycle = DayCycle(instanceContainer, instanceEventNode)
            dayCycle.start() // poo ( you )


        }
    }

    private class GeneratorDemo : ChunkGenerator {
        override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
            // Set chunk blocks
            for (x in 0 until Chunk.CHUNK_SIZE_X) {
                for (z in 0 until Chunk.CHUNK_SIZE_Z) {
                    for (y in 0..39) {
                        batch.setBlock(x, y, z, Block.STONE)
                    }
                }
            }
        }

        override fun getPopulators(): MutableList<ChunkPopulator>? {
            return null
        }

    }
}
