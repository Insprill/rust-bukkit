use jni::JNIEnv;
use jni::objects::JObject;

use crate::bukkit::command::Command;

pub mod bukkit;

struct RustBukkit {
    commands: Vec<Command>,
}

impl RustBukkit {
    fn new() -> Self {
        Self {
            commands: Vec::new()
        }
    }
}

static mut RUST_BUKKIT: Option<RustBukkit> = None;

pub fn enable(env: JNIEnv, _obj: JObject) {
    let jvm = env.get_java_vm().expect("Failed to get JVM");
    jvm.attach_current_thread_permanently().expect("Failed to attach to JVM thread");
    unsafe {
        RUST_BUKKIT = Some(RustBukkit::new())
    }
}

pub(crate) fn get_bukkit() -> &'static mut RustBukkit {
    unsafe {
        RUST_BUKKIT.as_mut().expect("get_instance called before enable!")
    }
}
