package hxckdms.hxccore.asm;

import hxckdms.hxccore.api.event.LivingSwingEvent;
import hxckdms.hxccore.crash.CrashHandler;
import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
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

            switch (index) {
                case 0:
                    transformRender(classNode);
                    break;
                case 1:
                    transformRendererSign(classNode);
                    break;
                case 2:
                    transformCommandEmote(classNode);
                    break;
                case 3:
                    transformCommandBase(classNode);
                    break;
                case 4:
                    transformLivingBase(classNode);
                    break;
                case 5:
                case 6:
                    transformCommandTP(classNode);
                    break;
                case 7:
                    transformCommandSpreadPlayers(classNode);
                    break;
                case 8:
                    transformEntityXPOrb(classNode);
                    break;
                case 9:
                    transformCrashReport(classNode);
                    break;
            }

            ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return classBeingTransformed;
    }

    private static void transformRender(ClassNode classNode) {
        final String RENDERER_LIVING_ENTITY = HxCLoader.RuntimeDeobf ? "func_147906_a" : "renderLivingLabel";
        final String RENDERER_LIVING_ENTITY_DESC = "(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(RENDERER_LIVING_ENTITY) && methodNode.desc.equals(RENDERER_LIVING_ENTITY_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == FSUB  && instruction.getNext().getOpcode() == FSTORE)
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

    //TODO: fix colour characters taking up actual text space
    private static void transformRendererSign(ClassNode classNode) {
        final String RENDERER_SIGN = HxCLoader.RuntimeDeobf ? "func_192841_a" : "render";
        final String RENDERER_SIGN_DESC = "(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(RENDERER_SIGN) && methodNode.desc.equals(RENDERER_SIGN_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ASTORE && instruction.getNext().getNext().getNext().getOpcode() == ILOAD)
                        targetNode = instruction;

                if (targetNode != null) {
                    LabelNode newLabelNode = new LabelNode();

                    InsnList toInsert = new InsnList();

                    toInsert.add(new VarInsnNode(ALOAD, 19));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ColorHelper.class), "handleSign", "(Ljava/lang/String;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 19));

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
        final String PROCESS_COMMAND = HxCLoader.RuntimeDeobf ? "func_184881_a" : "execute";
        final String PROCESS_COMMAND_DESC = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(PROCESS_COMMAND) && methodNode.desc.equals(PROCESS_COMMAND_DESC)) {

                AbstractInsnNode targetNode = null;

                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == INVOKEVIRTUAL && instruction.getNext().getNext().getOpcode() == NEW)
                        targetNode = instruction;

                if (targetNode != null) {
                    InsnList toInsert = new InsnList();
                    LabelNode node = new LabelNode();

                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "onEmoteEvent", "(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/util/text/ITextComponent;)Lnet/minecraft/util/text/ITextComponent;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 4));

                    toInsert.add(new LabelNode());

                    toInsert.add(new VarInsnNode(ALOAD, 4));
                    toInsert.add(new JumpInsnNode(IFNONNULL, node));
                    toInsert.add(new InsnNode(RETURN));

                    methodNode.instructions.insertBefore(targetNode, toInsert);
                    methodNode.instructions.insertBefore(targetNode, node);

                    targetNode = targetNode.getNext().getNext();
                    methodNode.instructions.insertBefore(targetNode, new VarInsnNode(ALOAD, 4));

                    for (int i = 0; i < 15; ++i) {
                        targetNode = targetNode.getNext();
                        methodNode.instructions.remove(targetNode.getPrevious());
                    }
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
                AbstractInsnNode targetNode = methodNode.instructions.getFirst();

                if (targetNode != null) {
                    InsnList toInsert = new InsnList();
                    LabelNode node = new LabelNode();

                    toInsert.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(MinecraftForge.class), "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));

                    toInsert.add(new TypeInsnNode(NEW, Type.getInternalName(LivingSwingEvent.class)));
                    toInsert.add(new InsnNode(DUP));

                    toInsert.add(new VarInsnNode(ALOAD, 0));
                    toInsert.add(new VarInsnNode(ALOAD, 1));

                    toInsert.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(LivingSwingEvent.class), "<init>", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/EnumHand;)V", false));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(EventBus.class), "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));

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

    //TODO: fix relative teleporting not giving the correct co-ordinate values.
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
                    toInsert.add(new TypeInsnNode(CHECKCAST, Type.getInternalName(EntityPlayerMP.class)));
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), HxCLoader.RuntimeDeobf ? "func_179628_a" : "getResult", "()D", false));
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), HxCLoader.RuntimeDeobf ? "func_179628_a" : "getResult", "()D", false));
                    toInsert.add(new VarInsnNode(ALOAD, 3));
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(CommandBase.CoordinateArg.class), HxCLoader.RuntimeDeobf ? "func_179628_a" : "getResult", "()D", false));

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
                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 0));
                    toInsert.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityXPOrb", HxCLoader.RuntimeDeobf ? "field_70170_p" : "world", "Lnet/minecraft/world/World;"));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HxCHooks.class), "getXPCoolDown", "(Lnet/minecraft/world/World;)I", false));
                    methodNode.instructions.insertBefore(targetNode, toInsert);
                    methodNode.instructions.remove(targetNode);
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
