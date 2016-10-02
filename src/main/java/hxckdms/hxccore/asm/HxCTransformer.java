package hxckdms.hxccore.asm;

import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.Logger;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class HxCTransformer implements IClassTransformer {
    private static final String[] classesBeingTransformed = {
            "net.minecraft.client.renderer.entity.RendererLivingEntity",
            "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer"
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
                    System.out.println("asdf1");
                    //TransformRendererLivingEntity(classNode);
                    break;
                case 1:
                    System.out.println("asdf2");
                    TransformRendererSign(classNode);
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

    private static void TransformRendererSign(ClassNode classNode) {
        final String RENDERER_SIGN = HxCLoader.RuntimeDeobf ? "func_147500_a" : "renderTileEntityAt";
        final String RENDERER_SIGN_DESC = "(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(RENDERER_SIGN) && methodNode.desc.equals(RENDERER_SIGN_DESC)) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ASTORE && instruction.getNext().getNext().getNext().getOpcode() == ILOAD && instruction.getNext().getNext().getNext().getNext().getOpcode() == ALOAD) {
                        targetNode = instruction.getNext();
                        System.out.println("asdf");
                    }

                if(targetNode != null) {
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
        if(!hasTransformed) Logger.error("Failed to transform: RendererSign.", Constants.MOD_NAME + " ASM");
    }


}
