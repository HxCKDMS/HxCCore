package HxCKDMS.HxCCore.renderers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class RenderHxCPlayer extends RenderPlayer {
    private Pattern p;
    
    public RenderHxCPlayer() {
        super();
        p = Pattern.compile("\u00A72.");
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
            
            int color = 0x20FFFFFF;
            if (entity instanceof EntityPlayer && name.equals("TehPers\u00A7r")) name = "\u00A72" + name;
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
}
