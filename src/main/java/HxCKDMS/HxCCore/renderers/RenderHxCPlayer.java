package HxCKDMS.HxCCore.renderers;

import HxCKDMS.HxCCore.Events.EventChat;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.network.MessageColor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class RenderHxCPlayer extends RenderPlayer {
    public static HashMap<String, String> nameNicks = new HashMap<String, String>();
    public static HashMap<String, Boolean> isPlayerOp = new HashMap<String, Boolean>();
    private int sendCounter = 0;

    public RenderHxCPlayer(RenderManager renderManager1) {
        super(renderManager1);
    }
    @Override
    protected void renderLivingLabel(Entity entity, String name, double x, double y, double z, int maxRenderDist) {
        String UUID = entity.getUniqueID().toString();
        if(sendCounter == 0)
            HxCCore.packetPipeLine.sendToServer(new MessageColor(UUID));

        sendCounter++;
        
        if(sendCounter >= 10000){
            sendCounter = 0;
        }
        
        try{
            if(isPlayerOp.get(entity.getUniqueID().toString()))
                name = EventChat.CC + "4" + name;
        }catch(NullPointerException ignored){}

        
        String nick;
        try{
            nick = nameNicks.get(UUID);
        }catch(NullPointerException unhandled){
            nick = "";
        }
        
        if(nick != null && !nick.equals("")){
            name = nick;
        }
        
        name = name.replace("&", EventChat.CC) + EventChat.CC + "f";
        
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
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            byte ears = 0;

            if (name.equals("deadmau5")) {
                ears = -10;
            }
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            worldrenderer.startDrawingQuads();
            int xOff = fontrenderer.getStringWidth(name) / 2;
            worldrenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            worldrenderer.addVertex((double) (-xOff - 1), (double) (-1 + ears), 0.0D);
            worldrenderer.addVertex((double) (-xOff - 1), (double) (8 + ears), 0.0D);
            worldrenderer.addVertex((double) (xOff + 1), (double) (8 + ears), 0.0D);
            worldrenderer.addVertex((double) (xOff + 1), (double) (-1 + ears), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, ears, 0x20FFFFFF);

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
