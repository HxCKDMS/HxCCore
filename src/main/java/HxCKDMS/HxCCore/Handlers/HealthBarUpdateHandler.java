package HxCKDMS.HxCCore.Handlers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Queue;
import java.util.Random;

public class HealthBarUpdateHandler {
    /* Most of this code was from Tinkers' Construct
     * I grabbed some from the Github and then Modified it
     * Intentions are to improve the system that already exist
     * but this is quite difficult for me since I never touched
     * render code. :/
     */
    Minecraft mc = Minecraft.getMinecraft();
    GameSettings gs = Minecraft.getMinecraft().gameSettings;
    Random rand = new Random();
    private static final ResourceLocation hearts = new ResourceLocation("HxCCore", "textures/gui/64Hearts.png");

    /* HUD */
@SubscribeEvent
public void renderHealthbar (RenderGameOverlayEvent.Pre event)
    {
        if(Loader.isModLoaded("rpghud"))
        return;
        if (!Loader.isModLoaded("tukmc_Vz") || Loader.isModLoaded("borderlands"))
        {
            if (event.type == RenderGameOverlayEvent.ElementType.HEALTH)
            {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int scaledWidth = scaledresolution.getScaledWidth();
                int scaledHeight = scaledresolution.getScaledHeight();
                int xBasePos = scaledWidth / 2 - 91;
                int yBasePos = scaledHeight - 39;
                boolean highlight = mc.thePlayer.hurtResistantTime / 3 % 2 == 1;
                if (mc.thePlayer.hurtResistantTime < 10)
                {
                    highlight = false;
                }
                IAttributeInstance attrMaxHealth = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
                int health = MathHelper.ceiling_float_int(mc.thePlayer.getHealth());
                int healthLast = MathHelper.ceiling_float_int(mc.thePlayer.prevHealth);
                float healthMax = (float) attrMaxHealth.getAttributeValue();
                if (healthMax > 20)
                healthMax = 20;
                float absorb = this.mc.thePlayer.getAbsorptionAmount();
                int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
                int rowHeight = Math.max(10 - (healthRows - 2), 3);
                int left = scaledWidth / 2 - 91;
                int top = scaledHeight - GuiIngameForge.left_height;
                if (!GuiIngameForge.renderExperiance)
                {
                    top += 7;
                    yBasePos += 7;
                }

            final int TOP = 9 * (mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
            final int BACKGROUND = (highlight ? 25 : 16);
                int MARGIN = 16;
                if (mc.thePlayer.isPotionActive(Potion.poison))
                    MARGIN += 36;
                else if (mc.thePlayer.isPotionActive(Potion.wither))
                    MARGIN += 72;
                float absorbRemaining = absorb;
                for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i)
                {
                    int b0 = (highlight ? 1 : 0);
                    int row = MathHelper.ceiling_float_int((float) (i + 1) / 10.0F) - 1;
                    int x = left + i % 10 * 8;
                    int y = top - row * rowHeight;
                    if (health <= 4)
                    y += rand.nextInt(2);
                    drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);
                    if (highlight)
                    {
                        if (i * 2 + 1 < healthLast)
                            drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9); // 6
                        else if (i * 2 + 1 == healthLast)
                            drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9); // 7
                    }
                    if (absorbRemaining > 0.0F)
                    {
                        if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
                            drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9); // 17
                        else
                            drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9); // 16
                            absorbRemaining -= 2.0F;
                    }
                    else
                    {
                        if (i * 2 + 1 < health)
                        drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9); // 4
                        else if (i * 2 + 1 == health)
                        drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9); // 5
                    }
                }
                    int potionOffset = 0;
                    PotionEffect potion = mc.thePlayer.getActivePotionEffect(Potion.wither);
                if (potion != null)
                    potionOffset = 18;
                    potion = mc.thePlayer.getActivePotionEffect(Potion.poison);
                if (potion != null)
                    potionOffset = 9;
                if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    potionOffset += 27;
                // Extra hearts
                this.mc.getTextureManager().bindTexture(hearts);
                int hp = MathHelper.ceiling_float_int(this.mc.thePlayer.getHealth());
                    for (int iter = 0; iter < hp / 20; iter++)
                    {
                        int renderHearts = (hp - 20 * (iter + 1)) / 2;
                        if (renderHearts > 10)
                        renderHearts = 10;
                        for (int i = 0; i < renderHearts; i++)
                            {
                                int y = 0;
                                Queue pots = (Queue)mc.thePlayer.getActivePotionEffects();
                                Potion derp = (Potion)pots.poll();
                                if (derp == Potion.regeneration){
                                    int asdf = (int)derp.getEffectiveness();
                                    y += 1;
                                }
                                this.drawTexturedModalRect(xBasePos + 8 * i, yBasePos + y, 18 * iter, potionOffset, 9, 9);
                            }
                            if (hp % 2 == 1 && renderHearts < 10)
                            {
                                this.drawTexturedModalRect(xBasePos + 8 * renderHearts, yBasePos, 9 + 18 * iter, potionOffset, 9, 9);
                            }
                    }
                    if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && gs.thirdPersonView != 0)
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
    public void drawTexturedModalRect (int par1, int par2, int par3, int par4, int par5, int par6)
        {
            float f = 0.00390625F;
            float f1 = 0.00390625F;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (par1), (double) (par2 + par6), this.zLevel, (double) ((float) (par3) * f), (double) ((float) (par4 + par6) * f1));
            tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), this.zLevel, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + par6) * f1));
            tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2), this.zLevel, (double) ((float) (par3 + par5) * f), (double) ((float) (par4) * f1));
            tessellator.addVertexWithUV((double) (par1), (double) (par2), this.zLevel, (double) ((float) (par3) * f), (double) ((float) (par4) * f1));
            tessellator.draw();
        }
    double zLevel = 0;
}