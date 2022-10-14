use jni::JNIEnv;
use jni::objects::{JClass, JValue};

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_RustBukkit_attachJvm(env: JNIEnv, _class: JClass) {
    let jvm = env.get_java_vm().expect("Failed to get JVM");
    jvm.attach_current_thread_permanently().expect("Failed to attach to JVM thread");
    let value = JValue::Int(1);
    env.call_static_method("net/insprill/rustbukkit/RustBukkit", "working", "(I)V", &[value]).expect("Failed to invoke method");
}
