package HxCKDMS.HxCCore.renderers;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;

public class RenderHxCPlayer extends RenderPlayer {
    public static HashMap<String, Character> nameColors = new HashMap<String, Character>();
    public int tmr = 0;
    
    public RenderHxCPlayer() {
        super();
    }
    
    protected void func_147906_a(Entity entity, String name, double x, double y, double z, int maxRenderDist) {
        double dist = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        
        if (dist <= (double) (maxRenderDist * maxRenderDist)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float n = 0.016666668F * 1.6F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.0F, (float) y + entity.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-n, -n, n);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.instance;
            byte ears = 0;
            
            if (name.equals("deadmau5")) {
                ears = -10;
            }
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int xOff = fontrenderer.getStringWidth(name) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex((double) (-xOff - 1), (double) (-1 + ears), 0.0D);
            tessellator.addVertex((double) (-xOff - 1), (double) (8 + ears), 0.0D);
            tessellator.addVertex((double) (xOff + 1), (double) (8 + ears), 0.0D);
            tessellator.addVertex((double) (xOff + 1), (double) (-1 + ears), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            
            if (++tmr >= 40) {
                tmr = 0;
                loadColors();
            }
            
            int color = 0x20FFFFFF;
            if (entity instanceof EntityPlayer && nameColors.containsKey(entity.getUniqueID().toString())) {
                name = "\u00A7" + nameColors.get(entity.getUniqueID().toString()).toString() + name;
            }
            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, ears, color);
            
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, ears, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
    
    public static void loadColors() {
        File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
        NBTTagCompound data = NBTFileIO.getData(colorData);
        if (data != null) {
            Set<String> keySet = (Set<String>) data.func_150296_c();
            for (String key : keySet) {
                String s = data.getString(key);
                if (s.length() == 1) {
                    RenderHxCPlayer.nameColors.put(key, s.toCharArray()[0]);
                }
            }
        }
    }
}
