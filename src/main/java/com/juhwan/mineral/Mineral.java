package com.juhwan.mineral;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class Mineral implements ModInitializer {

    // 각 미네랄 아이템 정의
    public static final Item STEEL = new Item(new Item.Settings());
    public static final Item RUBY = new Item(new Item.Settings());
    public static final Item SAPPHIRE = new Item(new Item.Settings());
    public static final Item RED_DIAMOND = new Item(new Item.Settings());
    public static final Item PLATINUM = new Item(new Item.Settings());
    public static final Item MANA_STONE = new Item(new Item.Settings());

    @Override
    public void onInitialize() {
        // 아이템 레지스트리에 등록
        Registry.register(Registries.ITEM, new Identifier("mineral", "steel"), STEEL);
        Registry.register(Registries.ITEM, new Identifier("mineral", "ruby"), RUBY);
        Registry.register(Registries.ITEM, new Identifier("mineral", "sapphire"), SAPPHIRE);
        Registry.register(Registries.ITEM, new Identifier("mineral", "red_diamond"), RED_DIAMOND);
        Registry.register(Registries.ITEM, new Identifier("mineral", "platinum"), PLATINUM);
        Registry.register(Registries.ITEM, new Identifier("mineral", "mana_stone"), MANA_STONE);

        // 아이템 그룹에 추가
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(STEEL);
            entries.add(RUBY);
            entries.add(SAPPHIRE);
            entries.add(RED_DIAMOND);
            entries.add(PLATINUM);
            entries.add(MANA_STONE);
        });

        // 광석 채굴 시 드롭 이벤트 등록
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!(player instanceof ServerPlayerEntity)) return;
            Random random = Random.create();
            // World 변수 중복 방지
World serverWorld = world;

            // 철광석을 캘 때 강철 드롭 (12% 확률)
            if (state.getBlock() == Blocks.IRON_ORE || state.getBlock() == Blocks.DEEPSLATE_IRON_ORE) {
                if (random.nextFloat() < 0.12) {
                    dropItem(serverWorld, pos, new ItemStack(STEEL));
                }
            }

            // 청금석 광석을 캘 때 사파이어 드롭 (2% 확률)
            if (state.getBlock() == Blocks.LAPIS_ORE || state.getBlock() == Blocks.DEEPSLATE_LAPIS_ORE) {
                if (random.nextFloat() < 0.02) {
                    dropItem(serverWorld, pos, new ItemStack(SAPPHIRE));
                }
            }

            // 레드스톤 광석을 캘 때 루비 드롭 (2% 확률)
            if (state.getBlock() == Blocks.REDSTONE_ORE || state.getBlock() == Blocks.DEEPSLATE_REDSTONE_ORE) {
                if (random.nextFloat() < 0.02) {
                    dropItem(serverWorld, pos, new ItemStack(RUBY));
                }
            }

            // 다이아몬드 광석을 캘 때 레드 다이아몬드 드롭 (1% 확률)
            if (state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() == Blocks.DEEPSLATE_DIAMOND_ORE) {
                if (random.nextFloat() < 0.01) {
                    dropItem(serverWorld, pos, new ItemStack(RED_DIAMOND));
                }
            }

            // 네더 석영 광석을 캘 때 백금 드롭 (4% 확률)
            if (state.getBlock() == Blocks.NETHER_QUARTZ_ORE) {
                if (random.nextFloat() < 0.04) {
                    dropItem(serverWorld, pos, new ItemStack(PLATINUM));
                }
            }

            // 에메랄드 광석을 캘 때 마나 스톤 드롭 (1% 확률)
            if (state.getBlock() == Blocks.EMERALD_ORE || state.getBlock() == Blocks.DEEPSLATE_EMERALD_ORE) {
                if (random.nextFloat() < 0.01) {
                    dropItem(serverWorld, pos, new ItemStack(MANA_STONE));
                }
            }
        });
    }

    // 아이템을 드롭하는 메소드
    private void dropItem(World world, BlockPos pos, ItemStack stack) {
        if (!world.isClient) {
            world.spawnEntity(new net.minecraft.entity.ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }
}
