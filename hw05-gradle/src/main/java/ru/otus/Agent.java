package ru.otus;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {
    static int proxyCnt = 0;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");


        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassNode classNode = new ClassNode();

                classReader.accept(classNode, 0);

                boolean classHasLoggedMethod = false;
                List<String> loggedMethodsList = new ArrayList<>();
                List<String> loggedMethodsDescList = new ArrayList<>();
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode annotationNode : methodNode.visibleAnnotations) {
                            if (annotationNode.desc.equals(Log.class.descriptorString())) {
                                System.out.println("class: " + classNode.name + "; methodName: " + methodNode.name + "; annotationName: " + annotationNode.desc + "; " + annotationNode.getClass());
                                System.out.println();
                                classHasLoggedMethod = true;
                                loggedMethodsList.add(methodNode.name);
                                loggedMethodsDescList.add(methodNode.desc);


                            }
                        }
                    }
                }

                if (classHasLoggedMethod) {
                    return addProxyMethod(classfileBuffer, loggedMethodsList, loggedMethodsDescList);
                }

                //System.out.println("classBeingRedefined: " + classBeingRedefined.getSimpleName());

                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass, List<String> targetMethods, List<String> targetMethodsDesc) {
        //System.out.println("addProxyMethod call");

        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("method name: " + name);
                if (targetMethods.contains(name)) {
                    return super.visitMethod(access, name.concat("Proxied"), descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        for (int i = 0; i < targetMethods.size(); i++) {
            MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, targetMethods.get(i), targetMethodsDesc.get(i), null, null);

            Handle handle = new Handle(
                    H_INVOKESTATIC,
                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants",
                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                    false);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", handle, "logged param:\u0001");

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            mv.visitVarInsn(Opcodes.ALOAD, 0); // this.

            for (Type type : Type.getArgumentTypes(targetMethodsDesc.get(i))) {
                System.out.println("type: " + type);
            }


//            for (int j = 0; j <= Type.getArgumentTypes(targetMethodsDesc.get(i)).length; j++) {
//                mv.visitVarInsn(Opcodes.ALOAD, j);
//
//            }
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ru/otus/LoggedMethodsClass", targetMethods.get(i).concat("Proxied"), targetMethodsDesc.get(i), false);

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }


        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(proxyCnt);
        return finalClass;
    }
}
