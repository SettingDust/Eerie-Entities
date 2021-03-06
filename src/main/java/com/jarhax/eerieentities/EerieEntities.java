package com.jarhax.eerieentities;

import com.jarhax.eerieentities.block.BlockCarvedPumpkin;
import com.jarhax.eerieentities.block.BlockCarvedPumpkin.PumpkinType;
import com.jarhax.eerieentities.client.ClientEvents;
import com.jarhax.eerieentities.client.ShaderHandler;
import com.jarhax.eerieentities.client.gui.FontRendererRunelic;
import com.jarhax.eerieentities.client.renderer.entity.RenderCursedArmor;
import com.jarhax.eerieentities.client.renderer.entity.RenderPumpkinSlime;
import com.jarhax.eerieentities.client.renderer.entity.RenderWisp;
import com.jarhax.eerieentities.client.renderer.entity.knight.RenderNetherKnight;
import com.jarhax.eerieentities.config.Config;
import com.jarhax.eerieentities.entities.EntityCursedArmor;
import com.jarhax.eerieentities.entities.EntityNetherKnight;
import com.jarhax.eerieentities.entities.EntityPumpkinSlime;
import com.jarhax.eerieentities.entities.EntityWisp;
import com.jarhax.eerieentities.items.ItemBlockArmor;
import com.jarhax.eerieentities.items.ItemNetherShield;

import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = EerieEntities.MODID, name = EerieEntities.NAME, version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.566,);", certificateFingerprint = "@FINGERPRINT@", guiFactory = "com.jarhax.eerieentities.client.gui.GuiFactoryEerieEntities")
public class EerieEntities {
    
    public static final String MODID = "eerieentities";
    public static final String NAME = "Eerie Entities";
    
    public static final LoggingHelper LOG = new LoggingHelper(NAME);
    public static final RegistryHelper REGISTRY = new RegistryHelper(MODID).setTab(new CreativeTabEerieEntites()).enableAutoRegistration();
    public static final NetworkHandler NETWORK = new NetworkHandler(MODID);
    
    public static final ResourceLocation LOOT_WISP = REGISTRY.registerLootTable("entities/wisp");
    public static final ResourceLocation LOOT_PUMPKIN_SLIME = REGISTRY.registerLootTable("entities/pumpkin_slime");
    public static final ResourceLocation LOOT_NETHER_KNIGHT = REGISTRY.registerLootTable("entities/nether_knight");
    public static final ResourceLocation LOOT_CURSED_ARMOR = REGISTRY.registerLootTable("entities/cursed_armor");
    
    public static Item netherShield;
    
    @SideOnly(Side.CLIENT)
    public static FontRenderer fontRunelic;
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
        
        Config.syncConfigData();
        
        REGISTRY.registerMob(EntityWisp.class, "wisp", 0, 0x00ffff, 0x33ccff);
        REGISTRY.registerMob(EntityPumpkinSlime.class, "pumpkin_slime", 1, 0xB67317, 0x804809);
        REGISTRY.registerMob(EntityNetherKnight.class, "nether_knight", 2, 16775294, 16167425);
        REGISTRY.registerMob(EntityCursedArmor.class, "cursed_armor", 3, 0xffffff, 0x000000);
        
        netherShield = REGISTRY.registerItem(new ItemNetherShield(), "nether_shield");
        
        // Add all the pumpkin variants
        for (final BlockCarvedPumpkin.PumpkinType type : PumpkinType.values()) {
            
            if (type != PumpkinType.NORMAL) {
                
                final String name = type.name().toLowerCase();
                final BlockCarvedPumpkin normal = new BlockCarvedPumpkin(type);
                final ItemBlock itemNormal = new ItemBlockArmor(EntityEquipmentSlot.HEAD, normal, type);
                REGISTRY.registerBlock(normal, itemNormal, "pumpkin_" + name);
                normal.setTranslationKey("pumpkin");
                
                final Block lit = new BlockCarvedPumpkin(type);
                final ItemBlock itemLit = new ItemBlock(lit);
                REGISTRY.registerBlock(lit, itemLit, "pumpkin_lit_" + name);
                lit.setTranslationKey("litpumpkin");
                lit.setLightLevel(1f);
                
                type.setItems(normal, lit);
                
                REGISTRY.addShapelessRecipe("light_pumpkin_" + name, new ItemStack(itemLit), new ItemStack(itemNormal), "torch");
                REGISTRY.addShapelessRecipe("pumpkin_seeds_" + name, new ItemStack(Items.PUMPKIN_SEEDS, 4), new ItemStack(itemNormal));
                REGISTRY.addShapelessRecipe("pumpkin_pie_" + name, new ItemStack(Items.PUMPKIN_PIE), OreDictUtils.EGG, Items.SUGAR, new ItemStack(itemNormal));
            }
        }
    }
    
    @EventHandler
    @SideOnly(Side.CLIENT)
    public void onClientPreInit (FMLPreInitializationEvent event) {
        
        ShaderHandler.registerShaders();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, RenderWisp::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPumpkinSlime.class, RenderPumpkinSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNetherKnight.class, RenderNetherKnight::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCursedArmor.class, RenderCursedArmor::new);
    }
    
    @EventHandler
    @SideOnly(Side.CLIENT)
    public void onClientInit (FMLInitializationEvent event) {
        
        fontRunelic = new FontRendererRunelic();
    }
    
    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event) {
        
        Config.updateSpawnEntries();
    }
}
