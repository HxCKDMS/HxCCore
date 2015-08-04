package HxCKDMS.HxCCore.Asm;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
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
            "net.minecraft.client.renderer.entity.RendererLivingEntity"
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBeingTransformed) {
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, classBeingTransformed) : classBeingTransformed;
    }

    private static byte[] transform(int index, byte[] classBeingTransformed) {
        LogHelper.info("Transforming: " + classesBeingTransformed[index], References.MOD_NAME + " ASM");
        try{
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBeingTransformed);
            classReader.accept(classNode, 0);
            switch (index){
                case 0:
                    TransformRendererLivingEntity(classNode);
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

    private static void TransformRendererLivingEntity(ClassNode classNode) {
        final String RENDERER_LIVING_ENTITY = HxCLoader.RuntimeDeobf ? "func_77033_b" : "passSpecialRender";
        final String RENDERER_LIVING_ENTITY_DESC = "(Lnet/minecraft/entity/EntityLivingBase;DDD)V";
        boolean hasTransformed = false;
        for (MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(RENDERER_LIVING_ENTITY) && methodNode.desc.equals(RENDERER_LIVING_ENTITY_DESC)) {
                AbstractInsnNode targetNode = null;
                for(AbstractInsnNode instruction : methodNode.instructions.toArray())
                    if (instruction.getOpcode() == ALOAD && instruction.getNext().getOpcode() == INVOKEVIRTUAL && instruction.getNext().getNext().getOpcode() == INVOKEINTERFACE && instruction.getNext().getNext().getNext().getOpcode() == ASTORE)
                        targetNode = instruction.getNext().getNext().getNext().getNext();

                if(targetNode != null) {
                    LabelNode newLabelNode = new LabelNode();

                    InsnList toInsert = new InsnList();
                    toInsert.add(new VarInsnNode(ALOAD, 11));
                    toInsert.add(new VarInsnNode(ALOAD, 1));

                    toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(RenderHooks.class), "getName", "(Ljava/lang/String;Lnet/minecraft/entity/Entity;)Ljava/lang/String;", false));
                    toInsert.add(new VarInsnNode(ASTORE, 11));

                    methodNode.instructions.insert(targetNode, toInsert);
                    methodNode.instructions.insert(targetNode, newLabelNode);
                }
                hasTransformed = true;
                LogHelper.info("Successfully transformed: RendererLivingEntity.", References.MOD_NAME + " ASM");
            }
        }
        if(!hasTransformed) LogHelper.error("Failed to transform: RendererLivingEntity.", References.MOD_NAME + " ASM");
    }
}