/*
 * This file ("ItemEnergy.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.base;

import cofh.api.energy.ItemEnergyContainer;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.data.PlayerData;
import de.ellpeck.actuallyadditions.mod.util.ItemUtil;
import de.ellpeck.actuallyadditions.mod.util.compat.ItemTeslaWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.NumberFormat;
import java.util.List;

public abstract class ItemEnergy extends ItemEnergyContainer{

    private final String name;

    public ItemEnergy(int maxPower, int transfer, String name){
        super(maxPower, transfer);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.name = name;

        this.register();
    }

    private void register(){
        ItemUtil.registerItem(this, this.getBaseName(), this.shouldAddCreative());

        this.registerRendering();
    }

    protected String getBaseName(){
        return this.name;
    }

    public boolean shouldAddCreative(){
        return true;
    }

    protected void registerRendering(){
        ActuallyAdditions.proxy.addRenderRegister(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    @Override
    public boolean getShareTag(){
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
        NumberFormat format = NumberFormat.getInstance();
        int display = PlayerData.getDataFromPlayer(player).energyDisplayMode;
        list.add(format.format(this.getEnergyStored(stack))+"/"+format.format(this.getMaxEnergyStored(stack))+(display == 1 ? "FU" : (display == 0 ? "RF" : "T")));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack){
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, NonNullList list){
        ItemStack stackFull = new ItemStack(this);
        this.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
        list.add(stackFull);

        ItemStack stackEmpty = new ItemStack(this);
        this.setEnergy(stackEmpty, 0);
        list.add(stackEmpty);
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack){
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double maxAmount = this.getMaxEnergyStored(stack);
        double energyDif = maxAmount-this.getEnergyStored(stack);
        return energyDif/maxAmount;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack){
        int currEnergy = this.getEnergyStored(stack);
        int maxEnergy = this.getMaxEnergyStored(stack);
        return MathHelper.hsvToRGB(Math.max(0.0F, (float)currEnergy/maxEnergy)/3.0F, 1.0F, 1.0F);
    }

    public void setEnergy(ItemStack stack, int energy){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null){
            compound = new NBTTagCompound();
        }
        compound.setInteger("Energy", energy);
        stack.setTagCompound(compound);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
        return ActuallyAdditions.teslaLoaded ? new ItemTeslaWrapper(stack, this) : null;
    }

    public int extractEnergyInternal(ItemStack stack, int maxExtract, boolean simulate){
        int before = this.maxExtract;
        this.setMaxExtract(Integer.MAX_VALUE);

        int toReturn = this.extractEnergy(stack, maxExtract, simulate);

        this.setMaxExtract(before);
        return toReturn;
    }

    public int receiveEnergyInternal(ItemStack stack, int maxReceive, boolean simulate){
        int before = this.maxReceive;
        this.setMaxReceive(Integer.MAX_VALUE);

        int toReturn = this.receiveEnergy(stack, maxReceive, simulate);

        this.setMaxReceive(before);
        return toReturn;
    }
}
