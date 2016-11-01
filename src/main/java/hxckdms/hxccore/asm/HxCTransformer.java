package hxckdms.hxccore.asm;

import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.server.management.PlayerList;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class HxCTransformer implements IClassTransformer {
    private static final String[] classesBeingTransformed = {
            "net.minecraft.client.renderer.entity.Render",
            "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer",
            "net.minecraft.command.server.CommandEmote",
            "net.minecraft.command.CommandBase",
            "net.minecraft.entity.EntityLivingBase",
            "net.minecraft.command.CommandTP",
            "net.minecraft.command.server.CommandTeleport",
            "net.minecraft.command.CommandSpreadPlayers"
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBeingTransformed) {
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, classBeingTransformed) : classBeingTransformed;
    }

    private static byte[] transform(int index, byte[] classBeingTransformed) {
        Logger.info("Transforming: " + classesBeingTransformed[index], Constants.MOD_NAME + " ASM");
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBeingTransformed);
            classReader.accept(classNode, 0);
            ClassWriter classWriter;

            switch (index) {
                case 0:
                    TransformRender(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 1:
                    TransformRendererSign(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 2:
                    TransformCommandEmote(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 3:
                    transformCommandBase(classNode);
                    classWriter = new ClassWriter(0);
                    break;
                case 4:
                    transformLivingBase(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 5:
                case 6:
                    transformCommandTP(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 7:
                    transformCommandSpreadPlayers(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                default:
                    classWriter = new ClassWriter(0);
                    break;
            }

            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("asdf: " + classesBeingTransformed[index]);
        }
        return classBeingTransformed;
    }

    private static void TransformRender(ClassNode classNode) {
        final String RENDERER_LIVING_ENTITY = HxCLoader.RuntimeDeobf ? "func_147906_a" : "renderLivingLabel";
        final String RENDERER_LIVING_ENTITY_DESC = "(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(RENDERER_LIVING_ENTITY) && methodNode.desc.equals(RENDERER_LIVING_ENTITY_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ISTORE && instruction.getNext().getNext().getNext().getNext().getOpcode() == INVOKEVIRTUAL)
                        targetNode = instruction.getNext();

                if (targetNode != null) {
                    LabelNode newLabelNode = new LabelNode();

                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 1));

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ColorHelper.class), "getTagName", "(Ljava/lang/String;Lnet/minecraft/entity/Entity;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 2));

                    methodNode.instructions.insert(targetNode, toInsert);
                    methodNode.instructions.insert(targetNode, newLabelNode);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: Render.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: Render.", Constants.MOD_NAME + " ASM");
    }

    private static void TransformRendererSign(ClassNode classNode) {
        final String RENDERER_SIGN = HxCLoader.RuntimeDeobf ? "func_180535_a" : "renderTileEntityAt";
        final String RENDERER_SIGN_DESC = "(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(RENDERER_SIGN) && methodNode.desc.equals(RENDERER_SIGN_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ASTORE && instruction.getNext().getNext().getNext().getOpcode() == ILOAD && instruction.getNext().getNext().getNext().getNext().getOpcode() == ALOAD)
                        targetNode = instruction.getNext();

                if (targetNode != null) {
                    LabelNode newLabelNode = new LabelNode();

                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 18));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ColorHelper.class), "handleSign", "(Ljava/lang/String;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 18));

                    methodNode.instructions.insert(targetNode, toInsert);
                    methodNode.instructions.insert(targetNode, newLabelNode);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: RendererSign.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: RendererSign.", Constants.MOD_NAME + " ASM");
    }

    private static void TransformCommandEmote(ClassNode classNode) {
        final String EXECUTE = HxCLoader.RuntimeDeobf ? "func_184881_a" : "execute";
        final String EXECUTE_DESC = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(EXECUTE) && methodNode.desc.equals(EXECUTE_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.getNext().getOpcode() == NEW)
                        targetNode = instruction.getNext();

                if (targetNode != null) {
                    AbstractInsnNode tempNode = targetNode.getPrevious().getPrevious().getPrevious().getPrevious();
                    LabelNode node = new LabelNode();

                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onEmoteEvent", "(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/util/text/ITextComponent;)Lnet/minecraft/util/text/ITextComponent;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 5));

                    toInsert.add(new VarInsnNode(ALOAD, 5));
                    toInsert.add(new JumpInsnNode(IFNONNULL, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);

                    methodNode.instructions.insert(tempNode, toInsert);

                    for (int i = 0; i < 16; ++i) {
                        targetNode = targetNode.getNext();
                        methodNode.instructions.remove(targetNode.getPrevious());
                    }

                    toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 5));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(PlayerList.class), HxCLoader.RuntimeDeobf ? "func_148539_a" : "sendChatMsg", "(Lnet/minecraft/util/text/ITextComponent;)V", false));

                    methodNode.instructions.insert(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandEmote.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandEmote.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandBase(ClassNode classNode) {
        final String CHECK_PERMISSION = HxCLoader.RuntimeDeobf ? "func_184882_a" : "checkPermission";
        final String CHECK_PERMISSION_DESC = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/command/ICommandSender;)Z";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(CHECK_PERMISSION) && methodNode.desc.equals(CHECK_PERMISSION_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == ALOAD)
                        targetNode = instruction.getNext().getNext();

                if (targetNode != null) {
                    for (int i = 0; i < 4; ++i) {
                        targetNode = targetNode.getNext();
                        methodNode.instructions.remove(targetNode.getPrevious());
                    }

                    methodNode.instructions.insert(targetNode.getPrevious(), new MethodInsnNode(INVOKESTATIC, Type.getInternalName(PermissionHandler.class), "canUseCommand", "(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/command/ICommand;)Z", false));
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandBase.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandBase.", Constants.MOD_NAME + " ASM");
    }


    private static void transformLivingBase(ClassNode classNode) {
        final String SWING_ARM = HxCLoader.RuntimeDeobf ? "func_184609_a" : "swingArm";
        final String SWING_ARM_DESC = "(Lnet/minecraft/util/EnumHand;)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(SWING_ARM) && methodNode.desc.equals(SWING_ARM_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.getNext().getOpcode() == ASTORE)
                        targetNode = instruction.getNext().getNext();

                if (targetNode != null) {
                    LabelNode node = new LabelNode();
                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 0));
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onLivingSwingEvent", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumHand;)Z", false));
                    toInsert.add(new JumpInsnNode(IFEQ, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);
                    methodNode.instructions.insert(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: EntityLivingBase.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: EntityLivingBase.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandTP(ClassNode classNode) {
        final String TELEPORT_ENTITY_TO_COORDINATES = HxCLoader.RuntimeDeobf ? "func_189863_a" : "teleportEntityToCoordinates";
        final String TELEPORT_ENTITY_TO_COORDINATES_DESC = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;)V";

        final String DO_TELEPORT = HxCLoader.RuntimeDeobf ? "func_189862_a" : "doTeleport";
        final String DO_TELEPORT_DESC = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;Lnet/minecraft/command/CommandBase$CoordinateArg;)V";

        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if((methodNode.name.equals(TELEPORT_ENTITY_TO_COORDINATES) && methodNode.desc.equals(TELEPORT_ENTITY_TO_COORDINATES_DESC)) || (methodNode.name.equals(DO_TELEPORT) && methodNode.desc.equals(DO_TELEPORT_DESC))) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == INSTANCEOF && instruction.getNext().getNext().getNext().getNext().getNext().getNext().getOpcode() == INVOKESTATIC)
                        targetNode = instruction.getNext().getNext().getNext();

                if (targetNode != null) {
                    LabelNode node = new LabelNode();
                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 0));
                    toInsert.add(new TypeInsnNode(CHECKCAST, "net/minecraft/entity/player/EntityPlayerMP"));
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), "getAmount", "()D", false));
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), "getAmount", "()D", false));
                    toInsert.add(new VarInsnNode(ALOAD, 3));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), "getAmount", "()D", false));

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onPlayerTeleportEvent", "(Lnet/minecraft/entity/player/EntityPlayer;DDD)Z", false));
                    toInsert.add(new JumpInsnNode(IFEQ, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);
                    methodNode.instructions.insert(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandTP.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandTP.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandSpreadPlayers(ClassNode classNode) {
        final String SET_PLAYER_POSITIONS = HxCLoader.RuntimeDeobf ? "func_110671_a" : "setPlayerPositions";
        final String SET_PLAYER_POSITIONS_DEC = "(Ljava/util/List;Lnet/minecraft/world/World;[Lnet/minecraft/command/CommandSpreadPlayers$Position;Z)D";

        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(SET_PLAYER_POSITIONS) && methodNode.desc.equals(SET_PLAYER_POSITIONS_DEC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == DADD && instruction.getNext().getOpcode() == INVOKEVIRTUAL)
                        targetNode = instruction.getNext();

                if (targetNode != null)
                    methodNode.instructions.set(targetNode, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onSpreadPlayersTeleport", "(Lnet/minecraft/entity/Entity;DDD)V", false));

                hasTransformed = true;
                Logger.info("Successfully transformed: CommandSpreadPlayers.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandSpreadPlayers.", Constants.MOD_NAME + " ASM");
    }
}
