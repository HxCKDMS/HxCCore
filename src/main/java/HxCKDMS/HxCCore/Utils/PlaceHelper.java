package HxCKDMS.HxCCore.Utils;

@SuppressWarnings("unused")
public class PlaceHelper {
    public static int[] getCoordsForPlacement(int x, int y, int z, int side){
        if(side == 0)
            return new int[]{x, y - 1, z};
        else if(side == 1)
            return new int[]{x, y + 1, z};
        else if(side == 2)
            return new int[]{x, y, z - 1};
        else if(side == 3)
            return new int[]{x, y, z + 1};
        else if(side == 4)
            return new int[]{x - 1, y, z};
        else if(side == 5)
            return new int[]{x + 1, y, z};
        else
            return null;
    }
}
