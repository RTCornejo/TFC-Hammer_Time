package net.rtc.hammertime.common.items;

import net.dries007.tfc.common.items.CreativeMiningTool;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

public class SledgeItem extends ToolItem implements CreativeMiningTool {

    private final TagKey<Block> blocks;

    public SledgeItem(Tier tier, float attackDamage, float attackSpeed, TagKey<Block> mineableBlocks, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, mineableBlocks, properties);
        this.blocks = mineableBlocks;
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) ? (this.speed * 0.4f) : 1.0F;
    }

    public void mineBlockInCreative(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        this.doSledgeMining(stack, level, state, pos, player);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos origin, LivingEntity entity) {
        this.doSledgeMining(stack, level, state, origin, entity);
        return super.mineBlock(stack, level, state, origin, entity);
    }

    private void dropResourcesAndBreak(BlockState state, BlockPos pos, Player player, ItemStack stack, Level level) {
        if (!player.isCreative()) {
            Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null, player, player.getMainHandItem());
            stack.hurtAndBreak(1, player, (p) -> {
                p.broadcastBreakEvent(player.getUsedItemHand());
            });
        }
    }

    private void doSledgeMining(ItemStack stack, Level level, BlockState state, BlockPos origin, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {

            BlockHitResult hitResult = (BlockHitResult) player.pick(20.0D, 0.0F, false);
            Direction face = hitResult.getDirection();  // Get the face of the block that was hit
            BlockPos startPos;
            BlockPos endPos = switch (face) {
                case UP, DOWN -> {
                    // Vertical plane (XY) - Mining from top or bottom
                    startPos = origin.offset(-1, 0, -1);
                    yield origin.offset(1, 0, 1);
                }
                case EAST, WEST -> {
                    // Vertical plane (YZ) - Mining from north or south
                    startPos = origin.offset(0, -1, -1);
                    yield origin.offset(0, 1, 1);
                }
                case NORTH, SOUTH -> {
                    // Vertical plane (XZ) - Mining from east or west
                    startPos = origin.offset(-1, -1, 0);
                    yield origin.offset(1, 1, 0);
                }
                default -> {
                    // Default to the original 3x3x1 in the XZ plane
                    startPos = origin.offset(-1, 0, -1);
                    yield origin.offset(1, 0, 1);
                }
            };
            // Adjust the 3x3x1 area based on the face the player is mining

            BlockState originBlock = level.getBlockState(origin);
            for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
                BlockState stateAt = level.getBlockState(pos);
                if (!pos.equals(origin)) {
                    if (this.isCorrectToolForDrops(stack, stateAt) && this.isCorrectToolForDrops(stack, originBlock)) {
                        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, stateAt, player);
                        if (!MinecraftForge.EVENT_BUS.post(event)) {
                            if (stateAt.hasProperty(BlockStateProperties.LAYERS)) {
                                int layers = stateAt.getValue(BlockStateProperties.LAYERS);
                                if (layers > 1) {
                                    level.setBlock(pos, stateAt.setValue(BlockStateProperties.LAYERS, layers - 1), 3);
                                } else {
                                    level.destroyBlock(pos, false, player);
                                }
                                dropResourcesAndBreak(stateAt, pos, player, stack, level);
                            } else {
                                dropResourcesAndBreak(stateAt, pos, player, stack, level);
                                level.destroyBlock(pos, false, player);
                            }
                        }
                        if (stack.getDamageValue() >= stack.getMaxDamage()) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
