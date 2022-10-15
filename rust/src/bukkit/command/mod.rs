use jni::JNIEnv;
use jni::objects::{JObject, JString};
use jni::sys::jobjectArray;
use crate::{get_bukkit};

pub struct CommandArgs {
    pub label: String,
    pub args: Vec<String>,
}

pub struct Command {
    pub name: String,
    executor: Option<Box<dyn Fn(&CommandArgs)>>,
    tab_completer: Option<Box<dyn Fn(&CommandArgs) -> Vec<String>>>,
}

impl Command {
    pub fn new(name: &str) -> Self {
        Self {
            name: name.to_string(),
            executor: None,
            tab_completer: None,
        }
    }

    pub fn executor(mut self, func: impl Fn(&CommandArgs) + 'static) -> Self {
        self.executor = Some(Box::new(func));
        self
    }

    pub fn tab_completer(mut self, func: impl Fn(&CommandArgs) -> Vec<String> + 'static) -> Self {
        self.tab_completer = Some(Box::new(func));
        self
    }

    pub fn register(self) {
        get_bukkit().commands.push(self);
    }
}

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_command_RustCommandHandler_execute(env: JNIEnv, _obj: JObject, j_name: JString, j_label: JString, j_args: jobjectArray) {
    let name: String = env.get_string(j_name).expect("Failed to get command name!").into();
    let label: String = env.get_string(j_label).expect("Failed to get command label!").into();
    let len = env.get_array_length(j_args).expect("Failed to get args length!");
    let mut args: Vec<String> = Vec::new();
    for x in 0..len {
        let element = env.get_object_array_element(j_args, x).expect("Failed to get array element");
        args.push(env.get_string(JString::from(element)).expect("Failed to get arg string!").into())
    }
    for command in &get_bukkit().commands {
        if !(command.name.eq(&name)) {
            continue;
        }
        if let Some(executor) = &command.executor {
            (executor)(&CommandArgs {
                label,
                args
            });
        }
        break
    }
}
