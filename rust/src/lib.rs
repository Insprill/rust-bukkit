use jni::JNIEnv;
use jni::objects::JObject;

use crate::bukkit::command::Command;
use crate::bukkit::event::EventHandler;

pub mod bukkit;

struct RustBukkit {
    commands: Vec<Command>,
    event_handlers: Vec<EventHandler>,
}

impl RustBukkit {
    fn new() -> Self {
        Self {
            commands: Vec::new(),
            event_handlers: Vec::new(),
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

pub(crate) fn get_event_handler<'a>(env: JNIEnv<'a>, obj: JObject<'a>) -> JObject<'a> {
    let rust_bukkit = env.get_field(obj, "rustBukkit", "Lnet/insprill/rustbukkit/RustBukkit;").expect("Failed to get rust bukkit").l().expect("Failed to get object");
    env.get_field(rust_bukkit, "eventHandler", "Lnet/insprill/rustbukkit/event/RustEventHandler;").expect("Failed to get event handler").l().expect("Failed to get object")
}
