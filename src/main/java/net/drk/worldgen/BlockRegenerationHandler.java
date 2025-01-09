package net.drk.worldgen;

import net.drk.block.ModBlocks;
import net.drk.block.voxel.logs.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockRegenerationHandler {

    private final Map<BlockPos, RegenerationData> regeneratingBlocks = new HashMap<>();
    private final int checkRadius;
    private long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 5000; // 60 seconds

    public BlockRegenerationHandler(int checkRadius) {
        this.checkRadius = checkRadius;
    }

    public void tick(ServerWorld world) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime < CHECK_INTERVAL) {
            return; // Skip if 60 seconds haven't passed
        }
        lastCheckTime = currentTime;

        Iterator<Map.Entry<BlockPos, RegenerationData>> iterator = regeneratingBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, RegenerationData> entry = iterator.next();
            BlockPos pos = entry.getKey();
            RegenerationData data = entry.getValue();

            if (currentTime >= data.getRespawnTime()) {
                // Check if there is already a block present
                BlockState existingState = world.getBlockState(pos);
                if (existingState != null && !existingState.isAir()) {
                    continue; // Skip placing a block if one already exists
                }

                // Place the appropriate block
                if (data.block.equals(ModBlocks.OAK_LOG)) {
                    world.setBlockState(pos, getOakBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.BIRCH_LOG)) {
                    world.setBlockState(pos, getBirchBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.SPRUCE_LOG)) {
                    world.setBlockState(pos, getSpruceBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.CHERRY_LOG)) {
                    world.setBlockState(pos, getCherryBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.DARK_OAK_LOG)) {
                    world.setBlockState(pos, getDarkOakBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.MANGROVE_LOG)) {
                    world.setBlockState(pos, getMangroveBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.JUNGLE_LOG)) {
                    world.setBlockState(pos, getJungleBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else if (data.block.equals(ModBlocks.ACACIA_LOG)) {
                    world.setBlockState(pos, getAcaciaBlockState(data.getBlock()), Block.NOTIFY_ALL);
                } else {
                    world.setBlockState(pos, getBlockState(data.getBlock()), Block.NOTIFY_ALL);
                }

                long newRespawnTime = currentTime + data.getOriginalRespawnTime();
                data.setRespawnTime(newRespawnTime);
            }
        }
    }

    public void onBlockBreak(ServerWorld world, BlockPos pos, Block block, long respawnTime) {
        long respawnEndTime = System.currentTimeMillis() + respawnTime;
        regeneratingBlocks.put(pos, new RegenerationData(block, respawnTime, respawnEndTime));
    }

    public void initialize(ServerWorld world) {


    }
    private BlockState getOakBlockState(Block block) {
        if (block.equals(ModBlocks.OAK_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.OAK_LOG.getStateManager().getDefaultState().with(OakLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getSpruceBlockState(Block block) {
        if (block.equals(ModBlocks.SPRUCE_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.SPRUCE_LOG.getStateManager().getDefaultState().with(SpruceLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getJungleBlockState(Block block) {
        if (block.equals(ModBlocks.JUNGLE_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.JUNGLE_LOG.getStateManager().getDefaultState().with(JungleLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getMangroveBlockState(Block block) {
        if (block.equals(ModBlocks.MANGROVE_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.MANGROVE_LOG.getStateManager().getDefaultState().with(MangroveLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getAcaciaBlockState(Block block) {
        if (block.equals(ModBlocks.ACACIA_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.ACACIA_LOG.getStateManager().getDefaultState().with(AcaciaLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getBirchBlockState(Block block) {
        if (block.equals(ModBlocks.BIRCH_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.BIRCH_LOG.getStateManager().getDefaultState().with(BirchLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getCherryBlockState(Block block) {
        if (block.equals(ModBlocks.CHERRY_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.CHERRY_LOG.getStateManager().getDefaultState().with(CherryLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }

    private BlockState getDarkOakBlockState(Block block) {
        if (block.equals(ModBlocks.DARK_OAK_LOG)) {
            int rotation = (int) (Math.random() * 4);
            return ModBlocks.DARK_OAK_LOG.getStateManager().getDefaultState().with(DarkOakLog.ROTATION, rotation);
        }
        return block.getDefaultState();
    }


    private BlockState getBlockState(Block block) {
        return block.getDefaultState();
    }

    private static class RegenerationData {
        private final Block block;
        private final long originalRespawnTime;
        private long respawnTime;

        public RegenerationData(Block block, long originalRespawnTime, long respawnTime) {
            this.block = block;
            this.originalRespawnTime = originalRespawnTime;
            this.respawnTime = respawnTime;
        }

        public Block getBlock() {
            return block;
        }

        public long getOriginalRespawnTime() {
            return originalRespawnTime;
        }

        public long getRespawnTime() {
            return respawnTime;
        }

        public void setRespawnTime(long respawnTime) {
            this.respawnTime = respawnTime;
        }
    }
}
