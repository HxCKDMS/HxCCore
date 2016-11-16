package hxckdms.hxccore.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerTeleportEvent extends PlayerEvent {
    private final double newX;
    private final double newY;
    private final double newZ;
    private final int newDimension;

    public PlayerTeleportEvent(EntityPlayer player, double newX, double newY, double newZ, int newDimension) {
        super(player);
        this.newX = newX;
        this.newY = newY;
        this.newZ = newZ;
        this.newDimension = newDimension;
    }

    public PlayerTeleportEvent(EntityPlayer player,  BlockPos newPos, int newDimension) {
        super(player);
        this.newX = newPos.getX();
        this.newY = newPos.getY();
        this.newZ = newPos.getZ();
        this.newDimension = newDimension;
    }

    public double getNewX() {
        return newX;
    }

    public double getNewY() {
        return newY;
    }

    public double getNewZ() {
        return newZ;
    }

    public int getNewDimension() {
        return newDimension;
    }
}
