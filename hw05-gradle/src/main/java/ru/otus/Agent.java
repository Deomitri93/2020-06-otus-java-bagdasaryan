package ru.otus;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");


        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {


                return addProxyMethod(classfileBuffer);
                //System.out.println("classBeingRedefined: " + classBeingRedefined.getSimpleName());

                //return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        ClassReader classReader = new ClassReader(originalClass);
        ClassNode classNode=new ClassNode();

        classReader.accept(classNode, 0);

        for(MethodNode methodNode : classNode.methods){
            if(methodNode.visibleAnnotations != null) {
                for (AnnotationNode annotationNode : methodNode.visibleAnnotations) {
                    if(annotationNode.desc.replace('/', '.').contains(Log.class.getName())) {
                        System.out.println("class: " + classNode.name + "; methodName: " + methodNode.name + "; annotationName: " + annotationNode.desc + "; " + annotationNode.getClass());
                        System.out.println();
                        //}
                    }
                }
            }
        }


//        ClassReader cr = new ClassReader(originalClass);
//        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                //if (name.equals("secureAccess")) {
//                //    return super.visitMethod(access, "secureAccessProxied", descriptor, signature, exceptions);
//                //} else {
//                //    return super.visitMethod(access, name, descriptor, signature, exceptions);
//                //}
//                return super.visitMethod(access, name + "proxied", descriptor, signature, exceptions);
//            }
//        };
//        cr.accept(cv, Opcodes.ASM5);
//
//        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "secureAccess", "(Ljava/lang/String;)V", null, null);
//
//        Handle handle = new Handle(
//                H_INVOKESTATIC,
//                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
//                "makeConcatWithConstants",
//                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
//                false);
//
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", handle, "logged param:\u0001");
//
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        mv.visitVarInsn(Opcodes.ALOAD, 0);
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ru/otus/aop/instrumentation/proxy/MyClassImpl", "secureAccessProxied", "(Ljava/lang/String;)V", false);
//
//        mv.visitInsn(Opcodes.RETURN);
//        mv.visitMaxs(0, 0);
//        mv.visitEnd();
//
//        byte[] finalClass = cw.toByteArray();
//
//        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
//            fos.write(finalClass);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return finalClass;

        return originalClass;
    }
}
