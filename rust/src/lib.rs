use jni::JNIEnv;
use jni::objects::{JObject};

pub mod bukkit;

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_RustBukkit_enable(env: JNIEnv, _obj: JObject) {
    let jvm = env.get_java_vm().expect("Failed to get JVM");
    jvm.attach_current_thread_permanently().expect("Failed to attach to JVM thread");
}
