package HxCKDMS.HxCCore.Asm;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class HxCTransformer implements IClassTransformer {
    private static final String[] classesBeingTransformed = {
            "net.minecraft.client.renderer.entity.Render",
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBeingTransformed) {
        boolean isObfuscated = !name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
    }

    private static byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated) {
        LogHelper.info("Transforming: " + classesBeingTransformed[index], References.MOD_NAME + " ASM");
        try{
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBeingTransformed);
            classReader.accept(classNode, 0);
            switch (index){
                case 0:
                    TransformRender(classNode, isObfuscated);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return classBeingTransformed;
    }

    private static void TransformRender(ClassNode classNode, boolean isObfuscated) {
        final String RENDERER = isObfuscated ? "a" : "func_147906_a";
        final String RENDERER_DESC = isObfuscated ? "(Lsa;Ljava/lang/String;DDDI)V" : "(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V";
        for (MethodNode methodNode : classNode.methods){
            if(methodNode.name.equals(RENDERER) && methodNode.desc.equals(RENDERER_DESC)){
                AbstractInsnNode targetNode = methodNode.instructions.get(0);
                if(targetNode != null){
                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 1));

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(RenderHooks.class), "getName", isObfuscated ? "(Ljava/lang/String;Lsa;)Ljava/lang/String;" : "(Ljava/lang/String;Lnet/minecraft/entity/Entity;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 2));

                    methodNode.instructions.insert(targetNode, toInsert);
                    LogHelper.info("Succeeded transforming: Render", References.MOD_NAME + " ASM");
                }else{
                    LogHelper.info("Failed to transform: Render", References.MOD_NAME + " ASM");
                }
            }
        }
    }
}
