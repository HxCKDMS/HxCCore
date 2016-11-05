package hxckdms.hxccore.asm;

import hxckdms.hxccore.crash.CrashHandler;
import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
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
            "net.minecraft.command.server.CommandTeleport",
            "net.minecraft.command.CommandSpreadPlayers",
            "net.minecraft.entity.item.EntityXPOrb",
            "net.minecraft.crash.CrashReport"
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
                    transformRender(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 1:
                    transformRendererSign(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 2:
                    transformCommandEmote(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 3:
                    transformCommandBase(classNode);
                    classWriter = new ClassWriter(0);
                    break;
                case 4:
                    transformLivingBase(classNode);
                    classWriter = new ClassWriter(0);
                    break;
                case 5:
                    transformCommandTP(classNode);
                    classWriter = new ClassWriter(0);
                    break;
                case 6:
                    transformCommandSpreadPlayers(classNode);
                    classWriter = new ClassWriter(0);
                    break;
                case 7:
                    transformEntityXPOrb(classNode);
                    classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    break;
                case 8:
                    transformCrashReport(classNode);
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
        }
        return classBeingTransformed;
    }

    private static void transformRender(ClassNode classNode) {
        final String RENDERER_LIVING_ENTITY = HxCLoader.RuntimeDeobf ? "func_147906_a" : "func_147906_a";
        final String RENDERER_LIVING_ENTITY_DESC = "(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(RENDERER_LIVING_ENTITY) && methodNode.desc.equals(RENDERER_LIVING_ENTITY_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == BIPUSH && instruction.getNext().getOpcode() == ISTORE)
                        targetNode = instruction.getNext().getNext();

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

    private static void transformRendererSign(ClassNode classNode) {
        final String RENDERER_SIGN = HxCLoader.RuntimeDeobf ? "func_147500_a" : "renderTileEntityAt";
        final String RENDERER_SIGN_DESC = "(Lnet/minecraft/tileentity/TileEntitySign;DDDF)V";
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

                    toInsert.add(new VarInsnNode(ALOAD, 15));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ColorHelper.class), "handleSign", "(Ljava/lang/String;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 15));

                    methodNode.instructions.insert(targetNode, toInsert);
                    methodNode.instructions.insert(targetNode, newLabelNode);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: RendererSign.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: RendererSign.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandEmote(ClassNode classNode) {

        final String PROCESS_COMMAND = HxCLoader.RuntimeDeobf ? "func_71515_b" : "processCommand";
        final String PROCESS_COMMAND_DESC = "(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(PROCESS_COMMAND) && methodNode.desc.equals(PROCESS_COMMAND_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.getNext().getOpcode() == NEW)
                        targetNode = instruction.getPrevious();

                if (targetNode != null) {
                    for (int i = 0; i < 18; ++i) {
                        targetNode = targetNode.getNext();
                        methodNode.instructions.remove(targetNode.getPrevious());
                    }

                    LabelNode node = new LabelNode();

                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new VarInsnNode(ALOAD, 3));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onEmoteEvent", "(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/util/IChatComponent;)Lnet/minecraft/util/IChatComponent;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 4));

                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new JumpInsnNode(IFNONNULL, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftServer.class), HxCLoader.RuntimeDeobf ? "func_71276_C" : "getServer", "()Lnet/minecraft/server/MinecraftServer;", false));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(MinecraftServer.class), HxCLoader.RuntimeDeobf ? "func_71203_ab" : "getConfigurationManager", "()Lnet/minecraft/server/management/ServerConfigurationManager;", false));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(ServerConfigurationManager.class), HxCLoader.RuntimeDeobf ? "func_148539_a" : "sendChatMsg", "(Lnet/minecraft/util/IChatComponent;)V", false));

                    methodNode.instructions.insert(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandEmote.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandEmote.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandBase(ClassNode classNode) {
        final String CAN_COMMAND_SENDER_USE_COMMAND = HxCLoader.RuntimeDeobf ? "func_71519_b" : "canCommandSenderUseCommand";
        final String CAN_COMMAND_SENDER_USE_COMMAND_DESC = "(Lnet/minecraft/command/ICommandSender;)Z";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(CAN_COMMAND_SENDER_USE_COMMAND) && methodNode.desc.equals(CAN_COMMAND_SENDER_USE_COMMAND_DESC)) {
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
        final String SWING_ITEM = HxCLoader.RuntimeDeobf ? "func_71038_i" : "swingItem";
        final String SWING_ITEM_DESC = "()V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(SWING_ITEM) && methodNode.desc.equals(SWING_ITEM_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.getNext().getOpcode() == ASTORE)
                        targetNode = instruction.getNext().getNext();

                if (targetNode != null) {
                    LabelNode node = new LabelNode();
                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 0));
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onLivingSwingEvent", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Z", false));
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
        final String PROCESS_COMMAND = HxCLoader.RuntimeDeobf ? "func_71515_b" : "processCommand";
        final String PROCESS_COMMAND_DESC = "(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V";

        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(PROCESS_COMMAND) && methodNode.desc.equals(PROCESS_COMMAND_DESC)) {
                AbstractInsnNode targetNode1 = null;
                AbstractInsnNode targetNode2 = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray()) {
                    if (instruction.getOpcode() == ACONST_NULL && instruction.getNext().getNext().getNext().getNext().getNext().getNext().getOpcode() == GETFIELD)
                        targetNode1 = instruction.getNext().getNext().getNext();
                    if (instruction.getOpcode() == ACONST_NULL && instruction.getNext().getNext().getNext().getNext().getNext().getNext().getOpcode() == DLOAD)
                        targetNode2 = instruction.getNext().getNext().getNext();
                }

                if (targetNode1 != null) {
                    LabelNode node = new LabelNode();
                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 3));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new FieldInsnNode(GETFIELD, Type.getInternalName(EntityPlayerMP.class), HxCLoader.RuntimeDeobf ? "" : "posX", "D"));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new FieldInsnNode(GETFIELD, Type.getInternalName(EntityPlayerMP.class), HxCLoader.RuntimeDeobf ? "" : "posY", "D"));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new FieldInsnNode(GETFIELD, Type.getInternalName(EntityPlayerMP.class), HxCLoader.RuntimeDeobf ? "" : "posZ", "D"));

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onPlayerTeleportEvent", "(Lnet/minecraft/entity/player/EntityPlayer;DDD)Z", false));
                    toInsert.add(new JumpInsnNode(IFEQ, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);
                    methodNode.instructions.insert(targetNode1, toInsert);
                }

                if (targetNode2 != null) {
                    LabelNode node = new LabelNode();
                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 3));
                    toInsert.add(new VarInsnNode(DLOAD, 5));
                    toInsert.add(new VarInsnNode(DLOAD, 7));
                    toInsert.add(new VarInsnNode(DLOAD, 9));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onPlayerTeleportEvent", "(Lnet/minecraft/entity/player/EntityPlayer;DDD)Z", false));
                    toInsert.add(new JumpInsnNode(IFEQ, node));
                    toInsert.add(new InsnNode(RETURN));
                    toInsert.add(node);
                    methodNode.instructions.insert(targetNode2, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandTP.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandTP.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandSpreadPlayers(ClassNode classNode) {
        final String SET_PLAYER_POSITIONS = HxCLoader.RuntimeDeobf ? "func_110671_a" : "func_110671_a";
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

    private static void transformEntityXPOrb(ClassNode classNode) {
        final String ON_COLLIDE_WITH_PLAYER = HxCLoader.RuntimeDeobf ? "func_70100_b_" : "onCollideWithPlayer";
        final String ON_COLLIDE_WITH_PLAYER_DESC = "(Lnet/minecraft/entity/player/EntityPlayer;)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(ON_COLLIDE_WITH_PLAYER) && methodNode.desc.equals(ON_COLLIDE_WITH_PLAYER_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == ICONST_2 && instruction.getNext().getNext().getOpcode() == PUTFIELD)
                        targetNode = instruction.getNext();

                if (targetNode != null) {
                    methodNode.instructions.set(targetNode, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "getXPCoolDown", "()I", false));
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: EntityXPOrb.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: EntityXPOrb.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCrashReport(ClassNode classNode) {
        final String MAKE_CRASH_REPORT = HxCLoader.RuntimeDeobf ? "func_85055_a" : "makeCrashReport";
        final String MAKE_CRASH_REPORT_DESC = "(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/crash/CrashReport;";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(MAKE_CRASH_REPORT) && methodNode.desc.equals(MAKE_CRASH_REPORT_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == ARETURN)
                        targetNode = instruction;

                if (targetNode != null) {
                    InsnList toInsert = new InsnList();

                    toInsert.insert(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(CrashHandler.class), "handleCrash", "(Lnet/minecraft/crash/CrashReport;)V", false));
                    toInsert.insert(new VarInsnNode(ALOAD, 2));

                    methodNode.instructions.insertBefore(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CrashReport.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CrashReport.", Constants.MOD_NAME + " ASM");
    }
}
