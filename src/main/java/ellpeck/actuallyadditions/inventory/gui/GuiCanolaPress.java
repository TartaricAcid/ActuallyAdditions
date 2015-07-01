package ellpeck.actuallyadditions.inventory.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ellpeck.actuallyadditions.inventory.ContainerCanolaPress;
import ellpeck.actuallyadditions.tile.TileEntityBase;
import ellpeck.actuallyadditions.tile.TileEntityCanolaPress;
import ellpeck.actuallyadditions.util.AssetUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiCanolaPress extends GuiContainer{

    private TileEntityCanolaPress press;

    private static final ResourceLocation resLoc = AssetUtil.getGuiLocation("guiCanolaPress");

    public GuiCanolaPress(InventoryPlayer inventory, TileEntityBase tile){
        super(new ContainerCanolaPress(inventory, tile));
        this.press = (TileEntityCanolaPress)tile;
        this.xSize = 176;
        this.ySize = 93+86;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y){
        AssetUtil.displayNameString(this.fontRendererObj, xSize, -10, this.press.getInventoryName());
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop+93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(resLoc);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

        if(this.press.getEnergyStored(ForgeDirection.UNKNOWN) > 0){
            int i = this.press.getEnergyScaled(83);
            drawTexturedModalRect(this.guiLeft + 43, this.guiTop+89-i, 176, 29, 16, i);
        }

        if(this.press.tank.getFluidAmount() > 0){
            int i = this.press.getTankScaled(83);
            drawTexturedModalRect(this.guiLeft + 117, this.guiTop+89-i, 192, 29, 16, i);
        }

        if(this.press.currentProcessTime > 0){
            int i = this.press.getProcessScaled(29);
            drawTexturedModalRect(this.guiLeft + 83, this.guiTop+32, 176, 0, 12, i);
        }
    }

    @Override
    public void drawScreen(int x, int y, float f){
        super.drawScreen(x, y, f);
        String text1 = this.press.storage.getEnergyStored() + "/" + this.press.storage.getEnergyStored() + " RF";
        if(x >= guiLeft+43 && y >= guiTop+6 && x <= guiLeft+58 && y <= guiTop+88){
            this.func_146283_a(Collections.singletonList(text1), x, y);
        }

        String text2 = this.press.tank.getFluidAmount() + "/" + this.press.tank.getCapacity() + " mB " +StatCollector.translateToLocal("fluid.canolaoil");
        if(x >= guiLeft+117 && y >= guiTop+6 && x <= guiLeft+132 && y <= guiTop+88){
            this.func_146283_a(Collections.singletonList(text2), x, y);
        }
    }
}