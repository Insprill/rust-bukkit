use std::fmt;

use jni::objects::{JObject, JValue};
use jni::JNIEnv;
use jni::sys::jint;
use rand::Rng;

use crate::{get_bukkit, get_event_handler};

#[derive(Debug)]
pub enum EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    MONITOR,
}

impl fmt::Display for EventPriority {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{:?}", self)
    }
}

pub struct Event {}

pub struct EventHandler {
    id: i32,
    event: String,
    priority: EventPriority,
    ignore_cancelled: bool,
    executor: Option<Box<dyn Fn(&Event)>>,
}

impl EventHandler {
    pub fn new(event: &str) -> Self {
        Self {
            id: rand::thread_rng().gen::<i32>(),
            event: event.to_string(),
            priority: EventPriority::NORMAL,
            ignore_cancelled: false,
            executor: None,
        }
    }
    pub fn priority(mut self, priority: EventPriority) -> Self {
        self.priority = priority;
        self
    }
    pub fn ignore_cancelled(mut self, ignore_cancelled: bool) -> Self {
        self.ignore_cancelled = ignore_cancelled;
        self
    }
    pub fn executor(mut self, executor: impl Fn(&Event) + 'static) -> Self {
        self.executor = Some(Box::new(executor));
        self
    }
    pub fn register(self, env: JNIEnv, obj: JObject) {
        let j_event = JValue::from(
            env.new_string(&self.event)
                .expect("Failed to create event string"),
        );
        let j_priority = JValue::from(
            env.new_string(&self.priority.to_string())
                .expect("Failed to create priority string"),
        );

        env.call_method(
            get_event_handler(env, obj),
            "registerEvent",
            "(ILjava/lang/String;Ljava/lang/String;Z)V",
            &[
                JValue::from(self.id),
                j_event,
                j_priority,
                JValue::from(self.ignore_cancelled),
            ],
        )
        .expect("Failed to register event");

        get_bukkit().event_handlers.push(self);
    }
}

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_event_RustEventHandler_execute(
    _env: JNIEnv,
    _event_hander: JObject,
    j_id: jint,
    _j_event: JObject,
) {
    for handler in &get_bukkit().event_handlers {
        if handler.id != j_id {
            continue;
        }
        let event = Event {};
        if let Some(executor) = &handler.executor {
            (executor)(&event);
        }
    }
}
