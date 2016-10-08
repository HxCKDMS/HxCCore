package hxckdms.hxccore.asm;

import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.PermissionHandler;
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
            "net.minecraft.command.CommandHandler"
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
                    TransformRender(classNode);
                    break;
                case 1:
                    TransformRendererSign(classNode);
                    break;
                case 2:
                    TransformCommandEmote(classNode);
                    break;
                case 3:
                    transformCommandHandler(classNode);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
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
                    toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(PlayerList.class), "sendChatMsg", "(Lnet/minecraft/util/text/ITextComponent;)V", false));

                    methodNode.instructions.insert(targetNode, toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandEmote.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandEmote.", Constants.MOD_NAME + " ASM");
    }

    private static void transformCommandHandler(ClassNode classNode) {
        final String EXECUTE_COMMAND = HxCLoader.RuntimeDeobf ? "func_71556_a" : "executeCommand";
        final String EXECUTE_COMMAND_DESC = "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(EXECUTE_COMMAND) && methodNode.desc.equals(EXECUTE_COMMAND_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && instruction.getNext().getNext().getNext().getOpcode() == IFEQ)
                        targetNode = instruction.getPrevious().getPrevious();

                if (targetNode != null) {
                    for (int i = 0; i < 5; ++i) {
                        targetNode = targetNode.getNext();
                        methodNode.instructions.remove(targetNode.getPrevious());
                    }

                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new VarInsnNode(ALOAD, 5));
                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(PermissionHandler.class), "canUseCommand", "(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/command/ICommand;)Z", false));
                    methodNode.instructions.insert(targetNode.getPrevious(), toInsert);
                }
                hasTransformed = true;
                Logger.info("Successfully transformed: CommandHandler.", Constants.MOD_NAME + " ASM");
            }
        }
        if (!hasTransformed) Logger.error("Failed to transform: CommandHandler.", Constants.MOD_NAME + " ASM");
    }
}
